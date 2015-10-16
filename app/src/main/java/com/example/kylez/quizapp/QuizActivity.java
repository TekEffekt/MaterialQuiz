package com.example.kylez.quizapp;

import android.content.Context;
import android.os.Bundle;
<<<<<<< HEAD
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
=======
import android.support.v7.app.AppCompatActivity;
>>>>>>> origin/master
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.w3c.dom.Text;

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
        int answerNumber = Integer.parseInt((String)((View)v.getParent()).getTag());

        // GAME MODEL NEEDED
        final Boolean answerCorrect = false;

        if(!answerCorrect)
        {
            shakePhone();
        }

        performSplash(v, answerCorrect);

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

    public void handleQuestionAnswer(Boolean wasCorrect)
    {
        if(wasCorrect)
        {
            currentPoints += 10;
            updatePointsView();
        }

        refresh();
    }

    public void refresh()
    {
        for(View button:buttons)
        {
            TextView text = (TextView)button.findViewWithTag("answerText");
            View splash = button.findViewWithTag("splash");

            splash.setAlpha(0);
            text.setText("");
        }

        setupCountdownTimer();
    }

    public void shakePhone()
    {
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
    }

    // UI CANDY
    public void updatePointsView()
    {
        pointsView.setText(currentPoints + " points");
    }

    public void performSplash(View answerButton, Boolean correct)
    {
        styleAnswerButtonForCorrectness(answerButton, correct);

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

}
