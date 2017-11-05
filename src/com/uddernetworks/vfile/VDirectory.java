package com.uddernetworks.vfile;

import org.apache.commons.lang.StringUtils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VDirectory implements TypeFile {
    private String name;
//    private String path;
    private List<TypeFile> vFiles = new ArrayList<>();

    public VDirectory(String name) {
        this.name = name;
//        this.path = path;
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
//                System.out.println("Found duplicate = " + file.getName());
                add = file;
                break;
            }
        }
        if (add == null) {
//            System.out.println("Added original = " + vFile.getName());
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

    public String printOutTree(int depth) {
        StringBuilder builder = new StringBuilder();
        for (TypeFile tFile : this.vFiles) {
            builder.append(getSpacing(depth)).append(tFile.getName()).append('\n');
            if (tFile.isDirectory()) {
                builder.append(((VDirectory) tFile).printOutTree(depth + 1));
            }
        }

        return builder.toString();
    }

    private String getSpacing(int amount) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            ret.append("   ");
        }
        return ret.toString();
    }

//    public VDirectory searchChildrenForDir(String name) {
//        if (this.name.equals(name)) return this;
//        List<VDirectory> search = getDirectoryChildren();
//        if (search.size() == 0) return null;
//
//        for (VDirectory vDir : search) {
//            VDirectory temp = vDir.searchChildrenForDir(name);
//            if (temp != null) return temp;
//        }
//
//        return null;
//    }
//
//    public List<VDirectory> getDirectoryChildren() {
//        List<VDirectory> ret = new ArrayList<>();
//        vFiles.stream().filter(typeFile -> typeFile instanceof VDirectory).forEach(vDir -> ret.add((VDirectory) vDir));
//        return ret;
//    }

    public List<TypeFile> getChildren() {
        return new ArrayList<>(vFiles);
    }

    @Override
    public String getName() {
        return name;
    }

//    @Override
//    public String getPath() {
////        return path;
//        return null;
//    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
