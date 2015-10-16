package com.example.kylez.quizapp;

/**
 * Created by Quinn on 10/15/15.
 */
public class QuizItem
{
    public int quizItemId;
    public String title;
    public String  imageSource = null;
    public String soundSource = null;

    /**
     * default constructor used maybe for testing purposes, without an image or sound source
     */
    public QuizItem(){
        title = "Quiz Item";
    }

    /**
     * Constructor for a quiz item that only contains an imageSource
     * @param newTitle new title for this quiz item
     * @param newImageSource source of image representing this item
     */
    public QuizItem(String newTitle, String newImageSource){
        title = newTitle;
        imageSource =newImageSource;
    }

    /**
     * Constructor for a quiz item that contains both an imageSource and soundSource representing it
     * @param newTitle new title for this quiz item
     * @param newImageSource source of image representing this item
     * @param newSoundSource source of sound representing this item
     */
    public QuizItem(String newTitle, String newImageSource, String newSoundSource){
        title = newTitle;
        imageSource =newImageSource;
        soundSource = newSoundSource;
    }

    /**
     * determines whether or not the passed in string matches this items title.
     * @param titleToCheck string that may or may not be the title
     * @return whether or not we found a match of title and the passed in string
     */
    public  boolean isCorrect (String titleToCheck){
        return title.equals(titleToCheck);
    }

    /**
     * getter for image files source
     * @return imageSource
     */
    public String getImageSource(){
        return imageSource;
    }

    /**
     * getter for the soundFiles source
     * @return soundSource;
     */
    public String getSoundSource(){
        return soundSource;
    }

    /**
     * getter for this item's title
     * @return title
     */
    public String getTitle(){
        return title;
    }

    public static void main(String[] args)
    {

    }
}
