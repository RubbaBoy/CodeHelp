package com.uddernetworks.vfile;

public class VFile implements TypeFile {
    private String name;
    private int id;
//    private String path;

    public VFile(String name, int id) {
        this.name = name;
        this.id = id;
//        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

//    @Override
//    public String getPath() {
//        return path;
//    }

//    @Override
//    public String toString() {
//        return "VFile [name = \"" + name + "\" path = \"" + path + "\"]";
//    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }


}
