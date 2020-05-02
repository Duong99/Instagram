package com.example.instagram.model;

public class Theme {
    private String imvTheme;
    private String urlHTMLTheme;
    private String encryptKey;

    public Theme(String imvTheme, String urlHTMLTheme) {
        this.imvTheme = imvTheme;
        this.urlHTMLTheme = urlHTMLTheme;
    }

    public Theme(String imvTheme, String urlHTMLTheme, String encryptKey) {
        this.imvTheme = imvTheme;
        this.urlHTMLTheme = urlHTMLTheme;
        this.encryptKey = encryptKey;
    }

    public String getImvTheme() {
        return imvTheme;
    }

    public String getUrlHTMLTheme() {
        return urlHTMLTheme;
    }
}
