package com.uddernetworks.vfile;

public class VFile implements TypeFile {
    private String name;
    private int id;

    public VFile(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

}
