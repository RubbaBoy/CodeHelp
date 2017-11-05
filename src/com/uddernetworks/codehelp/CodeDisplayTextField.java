package com.uddernetworks.codehelp;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

public class CodeDisplayTextField extends EditorTextField {

    private EditorImpl editor;
    private Color background;

    public CodeDisplayTextField() {
        this("");
    }

    public CodeDisplayTextField(@NotNull String text) {
        this(EditorFactory.getInstance().createDocument(text), null, FileTypes.PLAIN_TEXT, false);
    }

    public CodeDisplayTextField(@NotNull String text, Project project, FileType fileType, boolean oneLineMode) {
        super(EditorFactory.getInstance().createDocument(text), project, fileType, true, oneLineMode);
    }

    public CodeDisplayTextField(Document document, Project project, FileType fileType, boolean oneLineMode) {
        super(document, project, fileType, true, oneLineMode);
    }

    public Color getNormalBackgroundColor() {
        return this.background;
    }

    @Override
    protected EditorEx createEditor() {
        EditorImpl editor = (EditorImpl) super.createEditor();
        editor.setHorizontalScrollbarVisible(true);
        this.background = editor.getBackgroundColor();

        this.editor = editor;

//        editor.getComponent().setMinimumSize(new Dimension(750, 750));
//        editor.getComponent().setMaximumSize(new Dimension(750, 750));
//        editor.getComponent().setSize(750, 750);
//
//        editor.getScrollPane().setMinimumSize(new Dimension(750, 750));
//        editor.getScrollPane().setMaximumSize(new Dimension(750, 750));
//        editor.getScrollPane().setSize(750, 750);
//
//        System.out.println(editor.getScrollPane().getSize().getHeight());
//        System.out.println(editor.getScrollPane().getSize().getWidth());
//
//        new Thread(() -> {
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            System.out.println(editor.getScrollPane().getSize().getHeight());
//            System.out.println(editor.getScrollPane().getSize().getWidth());
//        }).start();

//
//        try {
//            Field field = editor.getClass().getDeclaredField("a8");
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        return editor;
    }

    public void setTheSize(int width, int height) {
        this.setPreferredWidth(width);
        this.setMaximumSize(new Dimension(width, height));
        super.setSize(width, height);

        editor.getComponent().setMinimumSize(new Dimension(width, height));
        editor.getComponent().setMaximumSize(new Dimension(width, height));
        editor.getComponent().setSize(width, height);

        editor.getScrollPane().setMinimumSize(new Dimension(width, height));
        editor.getScrollPane().setMaximumSize(new Dimension(width, height));
        editor.getScrollPane().setSize(width, height);
    }

}
