package com.uddernetworks.snippet;

public class JSONSnippet implements SnippetObject {

    private int id;
    private String date;
    private String Title;
    private String Author;
    private String description;
    private String[] tags;
    private String[] references;
    private Snippet[] snippets;


    public JSONSnippet(int id, String date, String title, String author, String description, String[] tags, String[] references, Snippet[] snippets) {
        this.id = id;
        this.date = date;
        this.Title = title;
        this.Author = author;
        this.description = description;
        this.tags = tags;
        this.references = references;
        this.snippets = snippets;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return Title;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDescription() {
        return description;
    }

    public String[] getTags() {
        return tags;
    }

    public String[] getReferences() {
        return references;
    }

    public Snippet[] getSnippets() {
        return snippets;
    }

    public boolean idEquals(JSONSnippet jsonSnippet) {
        return jsonSnippet.getId() == getId();
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public String toString() {
        return Title;
    }
}
