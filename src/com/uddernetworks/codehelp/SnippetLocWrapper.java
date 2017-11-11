package com.uddernetworks.codehelp;

import com.uddernetworks.snippet.JSONSnippet;

public class SnippetLocWrapper {

    private final String location;
    private final JSONSnippet jsonSnippet;

    public SnippetLocWrapper(String location, JSONSnippet jsonSnippet) {
        this.location = location;
        this.jsonSnippet = jsonSnippet;
    }

    public String getLocation() {
        return location;
    }

    public JSONSnippet getJsonSnippet() {
        return jsonSnippet;
    }

    @Override
    public String toString() {
        return "SnippetLocWrapper[location = " + location + ", jsonSnippet = " + jsonSnippet + "]";
    }
}