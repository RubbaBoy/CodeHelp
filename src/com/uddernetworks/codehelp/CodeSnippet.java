package com.uddernetworks.codehelp;

public class CodeSnippet {

    private final CodeDisplayTextField textField;
    private final String description;

    public CodeSnippet(CodeDisplayTextField textField, String description) {
        this.textField = textField;
        this.description = description;
    }

    public CodeDisplayTextField getTextField() {
        return textField;
    }

    public String getDescription() {
        return description;
    }
}
