package com.example.instagram.model;

public class AlbumPicture {
    private String id;
    private String name;

    public AlbumPicture(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
