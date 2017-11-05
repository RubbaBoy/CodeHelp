package com.uddernetworks.snippet;

public class JSONSnippet implements SnippetObject {

    private int id;
    private long date;
    private String Title;
    private String Author;
    private String description;
    private String[] tags;
    private Snippet[] snippets;


    public JSONSnippet(int id, long date, String title, String author, String description, String[] tags, Snippet[] snippets) {
        this.id = id;
        this.date = date;
        this.Title = title;
        this.Author = author;
        this.description = description;
        this.tags = tags;
        this.snippets = snippets;
    }


//    @Override
//    public String toString() {
//        return "[JSONSnippet id = " + id + " date = " + date + " Title = \"" + Title + "\" Author = \"" + Author + "\" description = \"" + description + "\" tags = \"" + Arrays.toString(snippets) + "\" snippets = \"" + Arrays.toString(snippets) + "\"]";
//    }

    public int getId() {
        return id;
    }

    public long getDate() {
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
