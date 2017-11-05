package com.uddernetworks.search;

import com.uddernetworks.codehelp.HelpToolWindow;
import com.uddernetworks.codehelp.SnippetUtil;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class SearchThread {

    private Thread thread;
    private static String currentSearching = null;
    private static String oldSearching = null;

    public SearchThread() {
        thread = new Thread(() -> {
            while (true) {
                if (SearchThread.currentSearching != null && !SearchThread.currentSearching.equalsIgnoreCase(SearchThread.oldSearching)) {
                    SearchThread.oldSearching = SearchThread.currentSearching;
                    SnippetUtil.searchTextChanged(SearchThread.oldSearching);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            System.out.println("Ending thread! Oh no!");
        });

        thread.start();
    }

    public void search(String search) {
        if (search == null || search.trim().equals("")) {
            SwingUtilities.invokeLater(() -> {
                HelpToolWindow.jTree.clearSelection();
                HelpToolWindow.jTree.setModel(new DefaultTreeModel(HelpToolWindow.root));
                HelpToolWindow.jTree.revalidate();
                HelpToolWindow.jTree.repaint();
            });
            return;
        }
        System.out.println("Searching " + search);
        SearchThread.currentSearching = search.trim();
    }

}
