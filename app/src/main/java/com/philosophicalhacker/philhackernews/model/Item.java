package com.philosophicalhacker.philhackernews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class Item implements Parcelable {
    private int id;
    private int score;
    private String title;
    @SerializedName("by")
    private String author;
    private String url;
    private String text;
    @SerializedName("kids")
    private int[] comments;

    public Item(int id, int score, String title, String author, String url, String text, int[] commentIds) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.author = author;
        this.url = url;
        this.text = text;
        this.comments = commentIds;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        score = in.readInt();
        title = in.readString();
        author = in.readString();
        url = in.readString();
        text = in.readString();
        comments = in.createIntArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(score);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(url);
        dest.writeString(text);
        dest.writeIntArray(comments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

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

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Item)) {
            return false;
        }

        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int[] getComments() {
        return comments;
    }
}
