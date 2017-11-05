package com.uddernetworks.codehelp;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.awt.*;

public class InfoDialog extends DialogWrapper {
    private JPanel panel;
    private StringBuilder text = new StringBuilder();

    @Override
    protected void doOKAction() {
        super.doOKAction();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected InfoDialog(boolean canBeParent) {
        super(canBeParent);
        panel = new JPanel();

        init();

//        setSize(300, 200);
    }

    private void addText(String stat, String info) {
        text.append(stat).append(":   ").append(info).append("<br>");
    }

    public void addTitle(String title) {
        addText("Title", title);
    }

    public void addAuthor(String author) {
        addText("Author", author);
    }

    public void addId(String id) {
        addText("Snippet Id", id);
    }

    public void addDate(String date) {
        addText("Date Created", date);
    }

    public void addTags(String tags) {
        addText("Snippet Tags", tags);
    }

    @Override
    protected JComponent createCenterPanel() {
        JLabel label = new JLabel("<html>" + text.toString() + "</html>");
//        label.setPreferredSize(new Dimension(250, 200));
        panel.add(label);
        return panel;
    }


}