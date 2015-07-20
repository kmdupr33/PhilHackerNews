package com.philosophicalhacker.philhackernews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class Story implements Parcelable {
    private int id;
    private int score;
    private String title;
    @SerializedName("by")
    private String author;
    private String url;

    public Story(int id, int score, String title, String author, String url) {
        this.id = id;
        this.score = score;
        this.title = title;
        this.author = author;
        this.url = url;
    }

    protected Story(Parcel in) {
        id = in.readInt();
        score = in.readInt();
        title = in.readString();
        author = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(score);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
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
