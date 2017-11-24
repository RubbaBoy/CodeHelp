package com.uddernetworks.snippet;

public class Snippet {

    private String description;
    private String code;
    private String lang;


    public Snippet(String description, String code, String lang) {
        this.description = description;
        this.code = code;
        this.lang = lang;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getLanguage() {
        return lang;
    }

    @Override
    public String toString() {
        return description + " - " + code;
    }

}