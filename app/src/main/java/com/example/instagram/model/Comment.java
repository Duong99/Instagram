package com.example.instagram.model;

public class Comment {
    private Person person;
    private String text;
    private String idComment;
    private String totalLike;

    public Comment(Person person, String text, String idComment, String totalLike) {
        this.person = person;
        this.text = text;
        this.idComment = idComment;
        this.totalLike = totalLike;
    }

    public Person getPerson() {
        return person;
    }

    public String getText() {
        return text;
    }

    public String getIdComment() {
        return idComment;
    }

    public String getTotalLike() {
        return totalLike;
    }
}
