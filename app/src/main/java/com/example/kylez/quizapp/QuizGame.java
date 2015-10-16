package com.example.kylez.quizapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Quinn on 10/15/15.
 */
public class QuizGame
{
    private int collectionSource;

    private Random randomness= new Random();

    private HashMap <Integer , QuizItem> quizItems;

    private QuizItem currentQuizItem;

    private ArrayList<String> quizItemTitles;

    public Context context;

    public QuizGame(int newCollectionSource, Context context){
        collectionSource = newCollectionSource;
        quizItemTitles = new ArrayList<>();
        quizItems = new HashMap<>();
        this.context = context;
        populateFromXml();
    }

    public void populateFromXml(){
        String[] items = context.getResources().getStringArray(collectionSource);

        for(int i = 0; i < items.length; i++){
            quizItems.put(i, new QuizItem(items[i], items[i] + ".jpg", items[i] + ".mp3"));
            quizItemTitles.add(items[i]);
        }
        currentQuizItem = quizItems.remove(randomness.nextInt(quizItems.size()));
    }

    public void next(){
        currentQuizItem = quizItems.put(randomness.nextInt(quizItems.size()), currentQuizItem);
    }

    public String getQuizItemTitle(){
        return currentQuizItem.title;
    }

    public Boolean isCurrentTitle(String toCheck){
        return currentQuizItem.isCorrect(toCheck);
    }

    public String getQuizeItemSoundFileName(){
        return currentQuizItem.getSoundSource();
    }

    public String getQuizItemImageFileName(){
        return currentQuizItem.getImageSource();
    }

    public void setCollectionSource(int source){
         collectionSource = source;
         populateFromXml();
    }

    public int getCollectionSource(){
        return collectionSource;
    }

    public int getCount(){
        return quizItems.size()+1;//+1 because of including currentQuizItem
    }

    public ArrayList<String> getRandomAnimalName(){
        ArrayList<String> otherTitles = new ArrayList<>(3);
        for (int i = 0,x = 0; i < otherTitles.size(); i++){
            do{
                x = randomness.nextInt(quizItemTitles.size());
            } while (otherTitles.contains(quizItemTitles.get(x)) && !quizItemTitles.get(x).equals(currentQuizItem.getTitle()));
            otherTitles.add(quizItemTitles.get(x));
        }
        return otherTitles;
    }
}