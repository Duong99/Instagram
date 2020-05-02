package com.example.instagram.model;

public class HomePost {
    private Person person;
    private Picture picture;

    public HomePost(Person person, Picture picture) {
        this.person = person;
        this.picture = picture;
    }

    public Person getPerson() {
        return person;
    }

    public Picture getPicture() {
        return picture;
    }
}
