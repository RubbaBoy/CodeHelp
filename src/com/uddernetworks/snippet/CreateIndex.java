package com.uddernetworks.snippet;

import com.google.gson.Gson;
import com.intellij.util.text.MarkdownUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CreateIndex {

    private List<IndexFile> indexFileList = new ArrayList<>();
    private Gson gson;
    private File base;

    public CreateIndex(File base) {
        this.gson = new Gson();
        this.base = base;
    }

    public void createIndex(File path) throws IOException {
        File[] files = path.listFiles(); // (dir1, name) -> name.toLowerCase().endsWith(".ch")

        for (File file : files) {
            if (file.getName().endsWith(".chindex") || file.getName().endsWith(".chbookmarks")) continue;
            if (file.isDirectory()) {
                createIndex(file);
            } else {
                JSONSnippet snippet = gson.fromJson(new String(Files.readAllBytes(Paths.get(file.getPath()))), JSONSnippet.class);
                indexFileList.add(new IndexFile(snippet.getId(), snippet.getTitle(), file, snippet.getTags()));
            }
        }
    }

    public List<IndexFile> getFileIndexes() {
        return indexFileList;
    }

    public class IndexFile {
        private int id;
        private String title;
        private File path;
        private String[] tags;

        public IndexFile(int id, String title, File path, String[] tags) {
            this.id = id;
            this.title = title;
            this.path = path;
            this.tags = tags;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public File getPath() {
            return path;
        }

        public String[] getTags() {
            return tags;
        }

        public JSONSnippet getSnippet() {
            Gson gson = new Gson();
            try {
                return gson.fromJson(new String(Files.readAllBytes(Paths.get(path.getPath()))), JSONSnippet.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public String toString() {
            return title;
        }
    }

}