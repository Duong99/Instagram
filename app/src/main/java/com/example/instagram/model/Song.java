package com.example.instagram.model;

public class Song {
    private String songKey;
    private String avatar;
    private String title;
    private String singer;
    private String location;
    private String time;
    private String encryptKey;

    public Song(String songKey, String avatar, String title, String singer, String location, String time) {
        this.songKey = songKey;
        this.avatar = avatar;
        this.title = title;
        this.singer = singer;
        this.location = location;
        this.time = time;

    }

    public String getSongKey() {
        return songKey;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getTitle() {
        return title;
    }

    public String getSinger() {
        return singer;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getEncryptKey() {
        return encryptKey;
    }
}
