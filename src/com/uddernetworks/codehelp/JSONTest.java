package com.uddernetworks.codehelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uddernetworks.snippet.CreateIndex;
import com.uddernetworks.snippet.JSONSnippet;
import com.uddernetworks.snippet.Snippet;
import com.uddernetworks.snippet.SnippetContainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONTest {

    private static int id = 0;

    public static void main(String[] args) {
        try {

//            Snippet[] snippets = new Snippet[] {
//                    new Snippet("Description1", "// CODE111 HERE")
//            };
//
//            String[] tags = new String[] {
//                    "Spigot",
//                    "Test",
//                    "Example"
//            };
//
//            JSONSnippet snippet = new JSONSnippet(id++, System.currentTimeMillis(), "Section One", "RubbaBoy", "An overall description of the snippet", tags, snippets);
////            JSONSnippet snippet2 = new JSONSnippet(id++, System.currentTimeMillis(), "Section One Also", "RubbaBoy", snippets);
////
////
////            JSONSnippet snippet32 = new JSONSnippet(id++, System.currentTimeMillis(), "Section Two", "RubbaBoy", snippets);
////            JSONSnippet snippet34 = new JSONSnippet(id++, System.currentTimeMillis(), "Section Two As Well", "RubbaBoy", snippets);
////
////            JSONSnippet snippet432 = new JSONSnippet(id++, System.currentTimeMillis(), "In First Root", "RubbaBoy", snippets);
////            JSONSnippet snippet434 = new JSONSnippet(id++, System.currentTimeMillis(), "In Second Root", "RubbaBoy", snippets);
//
//
//
////            SnippetContainer[] roots = new SnippetContainer[] {
////                    new SnippetContainer("Root 111", snippet, snippet2),
////                    new SnippetContainer("Root 222", snippet32, snippet34),
////                    new SnippetContainer("Outer Root", snippet432, new SnippetContainer("Inner Root", snippet434))
////            };
//
//
//            BufferedWriter out = new BufferedWriter(new FileWriter("E:\\RubbaBoysCodeHelp\\JSON.json"));
//
//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
////            out.write(gson.toJson(snippet));
//            System.out.println(gson.toJson(snippet));


            CreateIndex createIndex = new CreateIndex(new File("E:\\RubbaBoysCodeHelp\\snippets"));
            createIndex.createIndex(new File("E:\\RubbaBoysCodeHelp\\snippets"));
            List<CreateIndex.IndexFile> list = createIndex.getFileIndexes();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(list));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
