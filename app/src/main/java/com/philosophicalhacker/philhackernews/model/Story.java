package com.philosophicalhacker.philhackernews.model;

/**
 * Created by MattDupree on 7/18/15.
 */
public class Story {
    private int id;
    private int score;

    //For Gson
    @SuppressWarnings("unused")
    public Story() {}

    public Story(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }
}
