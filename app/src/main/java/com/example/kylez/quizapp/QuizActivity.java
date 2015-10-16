package com.example.kylez.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.codetail.widget.RevealFrameLayout;

public class QuizActivity extends AppCompatActivity {

    TextView pointsView;
    DonutProgress countdownView;
    CountDownTimer timer;
    int currentPoints;
    ImageView animalImage;
    View[] buttons = new View[4];
    QuizGame game;
    String currentAnimal;
    ArrayList<String> animalOptions = new ArrayList<>();
    boolean soundQuestion;
    MediaPlayer player;
    int correctAnswerIndex;
    int remainingTime;
    boolean pausedTimer;

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null)
        {
            timer.cancel();
            pausedTimer = true;

            if(player != null)
            {
                player.stop();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pausedTimer)
        {
            timer = new CountDownTimer(remainingTime, 50) {
                @Override
                public void onTick(long l) {
                    countdownView.setProgress((int)l);
                    remainingTime = (int)l;
                }

                @Override
                public void onFinish() {
                    countdownView.setProgress(0);
                    handleQuestionAnswer(false);
                }
            };
            timer.start();
        }
        pausedTimer = false;
    }

    // INITIALIZATION:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        pointsView = (TextView)findViewById(R.id.pointsView);
        animalImage = (ImageView)findViewById(R.id.animalImage);

        buttons[0] = (View)findViewById(R.id.button1);
        buttons[1] = (View)findViewById(R.id.button2);
        buttons[2] = (View)findViewById(R.id.button3);
        buttons[3] = (View)findViewById(R.id.button4);

        setupCountdownView();
        setupCountdownTimer();

        game = new QuizGame(R.array.animal_names_array,this.getBaseContext(), 10);

        refresh();
    }

    public void setupAnimalImage(String fileName)
    {
        int id = getResources().getIdentifier(fileName, "drawable", getPackageName());

        animalImage.setBackgroundResource(id);
    }

    public void setupCountdownView()
    {
        countdownView = (DonutProgress)findViewById(R.id.donut_progress);
        countdownView.setMax(10000);
    }

    public void setupCountdownTimer()
    {
        if(timer != null)
        {
            timer.cancel();
        }

        timer = new CountDownTimer(10000,50) {
            @Override
            public void onTick(long l){
                countdownView.setProgress((int)l);
                remainingTime = (int)l;
            }

            @Override
            public void onFinish() {
                countdownView.setProgress(0);
                handleQuestionAnswer(false);
            }
        };
        timer.start();
    }

    // ACTION HANDLERS
    public void answerClicked(View v)
    {
        View parent = (View)v.getParent();
        String chosenAnimal = ((String)((TextView)parent.findViewWithTag("answerText")).getText()).toLowerCase();
        Log.d("Debug", chosenAnimal);

        // GAME MODEL NEEDED
        final Boolean answerCorrect = game.isCurrentTitle(chosenAnimal);

        if(!answerCorrect)
        {
            shakePhone();
        }

        performSplash(v, answerCorrect);

        if(!answerCorrect)
        {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            revealCorrectAnswer();

                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            handleQuestionAnswer(answerCorrect);
                                        }
                                    });
                                }
                            };

                            new Timer().schedule(task, 1000);
                        }
                    });
                }
            };

            new Timer().schedule(task, 1000);
        } else
        {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handleQuestionAnswer(answerCorrect);
                        }
                    });
                }
            };
            new Timer().schedule(task, 1000);
        }
    }

    public void handleQuestionAnswer(Boolean wasCorrect)
    {
        timer.cancel();
        if(player != null) {
            player.stop();
        }
        if(wasCorrect)
        {
            currentPoints += 10;
            updatePointsView();
        }

        game.next();
        if(game.isGameOver())
        {
            this.finish();
        } else
        {
            refresh();
        }
    }

    public int getButtonRandomIndex()
    {
        return (new Random()).nextInt(4);
    }

    // UI CANDY
    public void revealCorrectAnswer()
    {
        View correctButton = ((View)buttons[correctAnswerIndex]).findViewWithTag("splash");
        Log.d("Debug", correctAnswerIndex + "");

        performSplash(correctButton, true);
    }

    public void refresh()
    {
        ArrayList<String> questions = game.getRandomAnimalNames();
        int index = getButtonRandomIndex();
        int count = 0;
        for(View button:buttons)
        {
            TextView text = (TextView)button.findViewWithTag("answerText");
            View splash = button.findViewWithTag("splash");
            text.setTextColor(Color.BLACK);

            splash.setAlpha(0);

            String capitalAnimalName = questions.get(questions.size() - 1);
            capitalAnimalName = capitalAnimalName.substring(0, 1).toUpperCase() + capitalAnimalName.substring(1);

            if(index == count)
            {
                String title = game.getQuizItemTitle();
                title = title.substring(0, 1).toUpperCase() + title.substring(1);

                text.setText(title);
                correctAnswerIndex = count;
            } else
            {
                text.setText(capitalAnimalName);
                questions.remove(questions.size() - 1);
            }

            count++;
        }

        setupCountdownTimer();
        currentAnimal = game.getQuizItemTitle();

        soundQuestion = (new Random()).nextBoolean();

        if(soundQuestion)
        {
            playSound();
        } else
        {
            setupAnimalImage(game.getQuizItemTitle());
        }
    }

    public void playSound()
    {
        int id = getResources().getIdentifier(game.getQuizItemTitle(), "raw", getPackageName());
        player = MediaPlayer.create(getBaseContext(),id);

        animalImage.setBackgroundResource(R.drawable.sound_effect);

        player.start();
    }

    public void updatePointsView()
    {
        pointsView.setText(currentPoints + " points");
    }

    public void performSplash(View answerButton, Boolean correct)
    {
        styleAnswerButtonForCorrectness(answerButton, correct);

        TextView text = (TextView)((View)answerButton.getParent()).findViewWithTag("answerText");
        text.setTextColor(Color.WHITE);

        int cx = (answerButton.getLeft() + answerButton.getRight()) / 2;
        int cy = (answerButton.getTop() + answerButton.getBottom()) / 2;

        int radius = Math.max(answerButton.getWidth(), answerButton.getHeight());

        SupportAnimator animator =
                ViewAnimationUtils.createCircularReveal(answerButton, cx, cy, 0, radius);

        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.start();
    }

    public void styleAnswerButtonForCorrectness(View button, Boolean correct)
    {
        View parent = (View)button.getParent();
        TextView textView = (TextView)(parent.findViewWithTag("answerText"));

        if(correct)
        {
            button.setBackgroundResource(R.color.colorCorrect);
            textView.setText("Correct");
        } else
        {
            button.setBackgroundResource(R.color.colorWrong);
            textView.setText("Wrong");
        }

        button.setAlpha(1);
    }

    // OTHER
    public void shakePhone()
    {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
    }
}
