package com.uddernetworks.codehelp;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

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

    public void addReferences(String[] refs) {
        if (refs.length == 0 || (refs.length == 1 && refs[0].trim().equals(""))) return;
        text.append("References:").append("<br>");
        for (String ref : refs) {
            text.append("<a href=\"").append(ref).append("\">").append(ref).append("<br>");
        }
    }

    @Override
    protected JComponent createCenterPanel() {
        JLabel label = new JLabel();

        JEditorPane output = new JEditorPane("text/html", "<html>" + text.toString() + "</html>");
        output.setOpaque(false);
        output.setEditable(false);
        output.setFont(label.getFont());
        output.addHyperlinkListener(hle -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                System.out.println(hle.getURL());
                BrowserUtil.browse(hle.getURL());
            }
        });


        panel.add(output);
        return panel;
    }

}