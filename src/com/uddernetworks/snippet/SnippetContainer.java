package com.uddernetworks.snippet;

public class SnippetContainer implements SnippetObject {

    private String sectionName;
    private Object[] snippetObjects;


    public SnippetContainer(String sectionName, Object... snippetObjects) {
        this.sectionName = sectionName;
        this.snippetObjects = snippetObjects;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Object[] getSnippetObjects() {
        return snippetObjects;
    }

    @Override
    public boolean isContainer() {
        return true;
    }
}
