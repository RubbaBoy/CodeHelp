package com.uddernetworks.codehelp;

import com.google.gson.Gson;
import com.uddernetworks.snippet.CreateIndex;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private Bookmarks bookmarks;
    private BookmarkChange onChange;
    private File bookmarkFile;
    private Gson gson;
    private DefaultMutableTreeNode bookmarkNode;

    public BookmarkManager(File base) throws IOException {
        gson = new Gson();
        bookmarkFile = new File(base.getAbsolutePath() + File.separator + "index.chbookmarks");
        if (!bookmarkFile.exists()) {
            bookmarkFile.createNewFile();
        } else {
            bookmarks = gson.fromJson(new String(Files.readAllBytes(Paths.get(bookmarkFile.toURI()))), Bookmarks.class);
        }
        if (bookmarks == null) {
            bookmarks = new Bookmarks();
        }
    }

    public void addBookmark(int bookmark) {
        if (!bookmarks.getBookmarks().contains(new Integer(bookmark))) {
            bookmarks.getBookmarks().add(new Integer(bookmark));
            saveBookmarks();
            if (onChange != null) {
                onChange.run(bookmarks);
            }
        }
    }

    public void removeBookmark(int bookmark) {
        if (bookmarks.getBookmarks().contains(new Integer(bookmark))) {
            bookmarks.getBookmarks().remove(new Integer(bookmark));
            saveBookmarks();
            if (onChange != null) {
                onChange.run(bookmarks);
            }
        }
    }

    public boolean isBookmarked(int id) {
        return bookmarks.getBookmarks().contains(id);
    }

    private void saveBookmarks() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(bookmarkFile));
            writer.write(gson.toJson(bookmarks));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeAllChildren(JTree jTree, DefaultMutableTreeNode node) {
        if (node.getChildCount() == 0) {
            return;
        }

        final int size = node.getChildCount();
        for (int i = 0; i < size; i++) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node.getLastChild();
            ((DefaultTreeModel) jTree.getModel()).removeNodeFromParent(treeNode);
        }
    }

    public DefaultMutableTreeNode addToRoot(DefaultMutableTreeNode root, JTree jTree, List<CreateIndex.IndexFile> snippets) {
        if (jTree == null) return bookmarkNode;
        if (bookmarkNode == null) {
            bookmarkNode = new DefaultMutableTreeNode("Bookmarks");
            ((DefaultTreeModel) jTree.getModel()).insertNodeInto(bookmarkNode, root, root.getChildCount());
        }

        removeAllChildren(jTree, bookmarkNode);

        for (Integer bookmark : bookmarks.getBookmarks()) {
            for (CreateIndex.IndexFile snippet : snippets) {
                if (snippet.getId() == bookmark.intValue()) {
                    ((DefaultTreeModel) jTree.getModel()).insertNodeInto(new DefaultMutableTreeNode(snippet), bookmarkNode, bookmarkNode.getChildCount());
                }
            }
        }

        return bookmarkNode;
    }

    public void setOnChange(BookmarkChange onChange) {
        this.onChange = onChange;
    }

    @FunctionalInterface
    public interface BookmarkChange {
        void run(Bookmarks bookmarks);
    }

    public class Bookmarks {
        private List<Integer> bookmarks;

        public Bookmarks() {
            bookmarks = new ArrayList<>();
        }

        public List<Integer> getBookmarks() {
            return bookmarks;
        }
    }
}