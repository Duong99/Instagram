package com.example.instagram.model;

public class Picture {
    private String shortcode;
    private String display_url;
    private String edge_liked_by_count;
    private String edge_media_to_comment;

    public Picture(String shortcode, String display_url, String edge_liked_by_count, String edge_media_to_comment) {
        this.shortcode = shortcode;
        this.display_url = display_url;
        this.edge_liked_by_count = edge_liked_by_count;
        this.edge_media_to_comment = edge_media_to_comment;
    }

    public String getEdge_media_to_comment() {
        return edge_media_to_comment;
    }

    public String getShortcode() {
        return shortcode;
    }

    public String getDisplay_url() {
        return display_url;
    }

    public String getEdge_liked_by_count() {
        return edge_liked_by_count;
    }
}
