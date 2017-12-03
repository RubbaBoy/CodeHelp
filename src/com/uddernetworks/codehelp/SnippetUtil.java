package com.uddernetworks.codehelp;

import com.uddernetworks.snippet.CreateIndex;
import com.uddernetworks.vfile.TypeFile;
import com.uddernetworks.vfile.VDirectory;
import com.uddernetworks.vfile.VFile;
import org.apache.commons.lang.math.NumberUtils;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.*;

public class SnippetUtil {

    private String getRelPath(File path, File base) {
        return base.toURI().relativize(path.toURI()).getPath();
    }

    public DefaultMutableTreeNode getTreeFromIndex(List<CreateIndex.IndexFile> indexList, File base) {
        List<CreateIndex.IndexFile> data = new ArrayList<>(indexList);

        // order by path
        data.sort(Comparator.comparing(index -> getRelPath(index.getPath(), base)));
        Collections.reverse(data);

        String lastFolder = "";
        for (CreateIndex.IndexFile index : data) {
            String path = getRelPath(index.getPath(), base);
            String[] split = path.split("/");

            int length = split.length - 1; // -1 means.. without empty string
            if (lastFolder.equals("") || !split[length - 1].equals(lastFolder)) {
                lastFolder = split[length - 1];
            }

            addAndOrCreate(new VFile(index.getTitle(), index.getId()), Arrays.copyOf(split, length));
        }

        return vDirectory.convertToTree();
    }


    private VDirectory vDirectory = new VDirectory("snippets");
    private void addAndOrCreate(VFile vFile, String[] dirs) {
        VDirectory needToFind = null;

        for (String dir : dirs) {
            if (needToFind == null) {
                VDirectory preLastAdded = new VDirectory(dir);
                TypeFile vFile1 = vDirectory.addFile(preLastAdded);
                needToFind = (VDirectory) vFile1;
            } else {
                VDirectory preLastAdded = new VDirectory(dir);
                TypeFile vFile1 = needToFind.addFile(preLastAdded);
                needToFind = (VDirectory) vFile1;
            }
        }

        needToFind.addFile(vFile);
    }


    public static void searchTextChanged(String text) {
        int searchId = -1;
        if (text.startsWith("#") && NumberUtils.isNumber(text.substring(1))) {
            searchId = Integer.valueOf(text.substring(1));
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for (CreateIndex.IndexFile indexFile : HelpToolWindow.indexList) {
            if (searchId != -1) {
                if (indexFile.getId() == searchId) {
                    root.add(new DefaultMutableTreeNode(indexFile));
                }
            } else {
                boolean cont = true;
                for (String tag : indexFile.getTags()) {
                    if (tag.toLowerCase().contains(text.toLowerCase()) && cont) {
                        root.add(new DefaultMutableTreeNode(indexFile));
                        cont = false;
                    }
                }
            }
        }

        SwingUtilities.invokeLater(() -> {
            HelpToolWindow.jTree.clearSelection();
            HelpToolWindow.jTree.setModel(new DefaultTreeModel(root, false));
            HelpToolWindow.jTree.revalidate();
            HelpToolWindow.jTree.repaint();
        });
    }
}
