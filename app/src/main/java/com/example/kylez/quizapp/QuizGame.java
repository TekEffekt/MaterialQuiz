

package com.example.kylez.quizapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Quinn on 10/15/15.
 */
public class QuizGame {

    private int collectionSource;//the source Id for where our quiz titles are

    private Random randomness = new Random();//a handy dandy random goes a long way

    private HashMap<Integer, QuizItem> quizItems;//Hash map to be able to map the entries

    private QuizItem currentQuizItem;//kept seperate from the quiz items to expidite switches

    private ArrayList<String> quizItemTitles;//just the titles for easy tracking

    public Context context;//in order to find our resources

    public int nOfRounds;

    public boolean gameOver = false;


    /**
     * create and init a quiz game
     * @param newCollectionSource //the id of the resource containing titles
     * @param context //so we can access our resources
     * @param numberOfRounds //constructor defining an explicit number of rounds
     */
    public QuizGame(int newCollectionSource, Context context, int numberOfRounds) {
        this.nOfRounds = numberOfRounds;
        collectionSource = newCollectionSource;
        quizItemTitles = new ArrayList<>();
        quizItems = new HashMap<>();
        this.context = context;
        populateFromXml();//populate our fields.
    }

    /**
     * create and init a quiz game
     * @param newCollectionSource //the id of the resource containing titles
     * @param  //so we can access our resources
     */
    public QuizGame(int newCollectionSource, Context context) {
        this.nOfRounds = 5;
        collectionSource = newCollectionSource;
        quizItemTitles = new ArrayList<>();
        quizItems = new HashMap<>();
        this.context = context;
        populateFromXml();//populate our fields.
    }

    /**
     * populate our collections from the xml, and set a currentQuizItem
     */
    public void populateFromXml() {
        String[] items = context.getResources().getStringArray(collectionSource);//get titles

        for (int i = 0; i < items.length; i++) {
            quizItems.put(i, new QuizItem(items[i], items[i] + ".jpg", items[i] + ".mp3"));
            quizItemTitles.add(items[i]);
        }
        currentQuizItem = quizItems.remove(randomness.nextInt(quizItems.size()));
    }

    /**
     * this method takes our current quiz item, and swaps it with a random one at any index
     * and will return whether or not the next question could be selected.
     */
    public boolean next() {
        if (0 != nOfRounds++) {
            currentQuizItem = quizItems.put(randomness.nextInt(quizItems.size()), currentQuizItem); //replace at a random index
            return true;
        } else {
            gameOver = true;
            return false;
        }
    }

    /**
     * Game state assesment utility, to check if the game has ended
     * @return boolean whether or not game is over
     */
    public boolean isGameOver(){
        return gameOver;
    }

    /**
     * getter for the current title
     * @return current title
     */
    public String getQuizItemTitle() {
        return currentQuizItem.title;
    }

    /**
     * check whether or not a certain string, for instance a user decision, matches the current item
     * @param toCheck the string we're checking against
     * @return whether or not it matches with the current quiz item
     */
    public Boolean isCurrentTitle(String toCheck) {
        return currentQuizItem.isCorrect(toCheck);
    }

    /**
     * get the file name of the current items corresponding sound.
     * @return current items sound source file name
     */
    public String getQuizItemSoundFileName() {
        return currentQuizItem.getSoundSource();
    }

    /**
     * this allows the game to know whether or not the current question was answered
     * @return whether or not the currentQuizItem was answered
     */
    public boolean wasAnswered(){
        return !(currentQuizItem.getState() == 0);
    }

    /**
     * get the current item's image file name
     * @return current images file name
     */
    public String getQuizItemImageFileName() {
        return currentQuizItem.getImageSource();
    }

    /**
     * reset the current collections to reflect he items from a different source.
     * @param source the source of the new items
     */
    public void setCollectionSource(int source) {
        collectionSource = source;
        populateFromXml();//update our collections
    }

    /**
     * get the current games source for quiz item titles
     * @return the id of the current collection source
     */
    public int getCollectionSource() {
        return collectionSource;
    }

    /**
     * returns the count of all the current quiz Items
     * @return the count of all the current quiz Items
     */
    public int getCount() {
        return quizItems.size() + 1;//+1 because of including currentQuizItem
    }

    /**
     * determine 3 random item names, each unique to each other and different from the current item name
     * @return an array list of 3 unique but random item names
     */
    public ArrayList<String> getRandomAnimalName() {
        ArrayList<String> otherTitles = new ArrayList<>(3);
        for (int i = 0, x = 0; i < otherTitles.size(); i++) {
            do {
                x = randomness.nextInt(quizItemTitles.size());
            }
            while (otherTitles.contains(quizItemTitles.get(x)) && !quizItemTitles.get(x).equals(currentQuizItem.getTitle()));
            otherTitles.add(quizItemTitles.get(x));
        }
        return otherTitles;
    }
}