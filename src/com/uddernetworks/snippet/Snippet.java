package com.uddernetworks.snippet;

public class Snippet {

    private String description;
    private String code;


    public Snippet(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return description + " - " + code;
    }

}
