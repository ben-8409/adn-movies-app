package de.benjamingeese.themovies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewEntry implements Parcelable {
    private String id; //0
    private String author; //1
    private String content; //2
    private String url; //3

    @SuppressWarnings("unused")
    public static final Creator<ReviewEntry> CREATOR = new Creator<ReviewEntry>() {
        @Override
        public ReviewEntry createFromParcel(Parcel in) {
            return new ReviewEntry(in);
        }

        @Override
        public ReviewEntry[] newArray(int size) {
            return new ReviewEntry[size];
        }
    };

    public ReviewEntry() {}

    private ReviewEntry(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
}
