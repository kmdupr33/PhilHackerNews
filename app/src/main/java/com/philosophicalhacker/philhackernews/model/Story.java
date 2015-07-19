package com.philosophicalhacker.philhackernews.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MattDupree on 7/18/15.
 */
public class Story {
    private int id;
    private int score;
    private String title;
    @SerializedName("by")
    private String author;

    //For Gson
    @SuppressWarnings("unused")
    public Story() {}

    public Story(int id, int score, String title, String author) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Story)) {
            return false;
        }

        Story story = (Story) o;
        return id == story.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
