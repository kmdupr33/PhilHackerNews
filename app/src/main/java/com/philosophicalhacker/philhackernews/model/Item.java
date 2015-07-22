package com.philosophicalhacker.philhackernews.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * Created by MattDupree on 7/18/15.
 */
public class Item implements Parcelable {

    private int id;
    private String type;
    private int score;
    private String title;
    @SerializedName("by")
    private String author;
    private String url;
    private String text;
    @SerializedName("kids")
    private int[] comments;
    private int parent;
    private boolean deleted;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({TYPE_STORY, TYPE_COMMENT})
    public @interface ItemType {}
    public static final String TYPE_STORY = "story";
    public static final String TYPE_COMMENT = "comment";

    public Item(int id, @ItemType String type, int score, String title, String author, String url, String text, int[] commentIds, int parent, boolean deleted) {
        this.id = id;
        this.type = type;
        this.score = score;
        this.title = title;
        this.author = author;
        this.url = url;
        this.text = text;
        this.comments = commentIds;
        this.parent = parent;
        this.deleted = deleted;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        type = in.readString();
        score = in.readInt();
        title = in.readString();
        author = in.readString();
        url = in.readString();
        text = in.readString();
        comments = in.createIntArray();
        parent = in.readInt();
        deleted = in.readInt() == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeInt(score);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(url);
        dest.writeString(text);
        dest.writeIntArray(comments);
        dest.writeInt(parent);
        dest.writeInt(deleted ? 1 : 0);
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

    //----------------------------------------------------------------------------------
    // Public Methods
    //----------------------------------------------------------------------------------
    public boolean isDeleted() {
        return deleted;
    }

    public int getParent() {
        return parent;
    }

    public String getType() {
        return type;
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
