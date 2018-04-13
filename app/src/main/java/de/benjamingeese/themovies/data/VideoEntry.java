package de.benjamingeese.themovies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoEntry implements Parcelable {

    public static final String SITE_YOUTUBE = "YouTube";
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VideoEntry> CREATOR = new Parcelable.Creator<VideoEntry>() {
        @Override
        public VideoEntry createFromParcel(Parcel in) {
            return new VideoEntry(in);
        }

        @Override
        public VideoEntry[] newArray(int size) {
            return new VideoEntry[size];
        }
    };
    private String id;
    private String key;
    private String language; //iso_639_1
    private String name;
    private String region; //iso_3166_1
    private String site;
    private int size;
    private String type; //Allowed values: Trailer, Teaser, Clip, Featurette

    public VideoEntry() {
    }

    private VideoEntry(Parcel in) {
        id = in.readString();
        key = in.readString();
        language = in.readString();
        name = in.readString();
        region = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name + " (Id: " + id + ")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
        dest.writeString(language);
        dest.writeString(name);
        dest.writeString(region);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

}
