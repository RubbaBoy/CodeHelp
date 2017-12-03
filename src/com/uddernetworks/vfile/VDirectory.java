package com.uddernetworks.vfile;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class VDirectory implements TypeFile {
    private String name;
    private List<TypeFile> vFiles = new ArrayList<>();

    public VDirectory(String name) {
        this.name = name;
    }

    /***
     * adds a file to the current vDirectory
     * @param vFile What file to add
     * @return The file added
     */
    public TypeFile addFile(TypeFile vFile) {
        TypeFile add = null;
        for (TypeFile file : vFiles) {
            if (file.isDirectory() == vFile.isDirectory() && file.getName().equals(vFile.getName())) {
                add = file;
                break;
            }
        }
        if (add == null) {
            vFiles.add(vFile);
            return vFile;
        } else {
            return add;
        }
    }

    public DefaultMutableTreeNode convertToTree() {
        DefaultMutableTreeNode ret = new DefaultMutableTreeNode(name);
        for (TypeFile tFile : this.vFiles) {
            if (tFile.isDirectory()) {
                ret.add(((VDirectory) tFile).convertToTree());
            } else {
                ret.add(new DefaultMutableTreeNode(tFile));
            }
        }

        return ret;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
