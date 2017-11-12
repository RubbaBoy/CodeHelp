package com.uddernetworks.codehelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uddernetworks.snippet.JSONSnippet;
import com.uddernetworks.snippet.Snippet;

import javax.swing.tree.DefaultMutableTreeNode;

public class Temp {

    public static void main(String[] args) {
//        char[] chars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        Random random = new Random();
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(chars[random.nextInt(15)]);
//        }

//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
//            root.add(new DefaultMutableTreeNode("one"));
//            root.add(new DefaultMutableTreeNode("two"));
//            root.add(new DefaultMutableTreeNode("three"));
//            root.add(new DefaultMutableTreeNode("four"));
//            root.add(new DefaultMutableTreeNode("five"));
//
//            DefaultMutableTreeNode outer = new DefaultMutableTreeNode("outer");
//                outer.add(new DefaultMutableTreeNode("inner one"));
//                outer.add(new DefaultMutableTreeNode("inner two"));
//                outer.add(new DefaultMutableTreeNode("inner three"));
//            root.add(outer);
//
//
//        print(root, "");

//        JSONSnippet snippet = new JSONSnippet(1, "Today", "Title", "RubbaBoy", "My snippet description!", new String[] {"Tag 1, Tag 2, Tag 3"}, new Snippet[] {new Snippet("Code description here", "// Code here")});
//
//        SnippetLocWrapper[] wrapper = new SnippetLocWrapper[] {
//                new SnippetLocWrapper("Root 111/Root 222/Root 333", snippet)
//        };

//        String input = "[{\"location\":\"Root 111\\/Root 222\\/Root 333\\/Root 444\\/Root 555\\/Root 666\",\"jsonSnippet\":{\"id\":1,\"date\":\"11-05-2017 18:59:23\",\"Title\":\"Title Of Snippet\",\"Author\":\"Your Name\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Root 111\\/Root 222\\/Root 333\\/Root 444\\/Root 555\",\"jsonSnippet\":{\"id\":2,\"date\":\"11-05-2017 18:59:31\",\"Title\":\"Odd one\",\"Author\":\"Your Name\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Root 111\\/Root 222\\/Root 333\",\"jsonSnippet\":{\"id\":3,\"date\":\"11-05-2017 18:59:58\",\"Title\":\"Second version!\",\"Author\":\"RubbaBoy\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\",\"Tag3\"]\",\"snippets\":[{\"code\":\"Description of code block111\",\"description\":\"\\/\\/ Code here111\"},{\"code\":\"Description of code block222\",\"description\":\"\\/\\/ Code here222\"}]}},{\"location\":\"Root 111\\/Root 222\",\"jsonSnippet\":{\"id\":4,\"date\":\"11-05-2017 19:00:37\",\"Title\":\"Another in 2\",\"Author\":\"Your Name\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Root 111\\/Root 222\\/Extra Root\",\"jsonSnippet\":{\"id\":5,\"date\":\"11-05-2017 19:01:01\",\"Title\":\"In three?!\",\"Author\":\"Your Name\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Other Base\\/Wow\",\"jsonSnippet\":{\"id\":6,\"date\":\"11-05-2017 19:01:29\",\"Title\":\"oof, 3 still\",\"Author\":\"Your Name\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Root 111\\/Root 222\",\"jsonSnippet\":{\"id\":7,\"date\":\"11-05-2017 19:02:04\",\"Title\":\"Now 4?!?!?!!!\",\"Author\":\"Your Name\",\"description\":\"LEss boring desc\",\"tags\":\"[\"Tag1\",\"Tag2\",\"Fourth gen\",\"4\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}},{\"location\":\"Other Base\\/Wow\",\"jsonSnippet\":{\"id\":8,\"date\":\"11-05-2017 19:03:00\",\"Title\":\"Last in 4\",\"Author\":\"RubbaBoy\",\"description\":\"Description of the snippet\",\"tags\":\"[\"Tag1\",\"Tag2\"]\",\"snippets\":[{\"code\":\"Description of code block\",\"description\":\"\\/\\/ Code here\"}]}}]";
//
//        Gson gson = new Gson();
//
//        SnippetLocWrapper[] wrappers = gson.fromJson(input,  SnippetLocWrapper[].class);
//        System.out.println("Wrappers:\n");
//        System.out.println(wrappers);

        System.out.println("Line one!%nNext line!%nLine three!%n:D".replaceAll("%n", System.getProperty("line.separator")).replaceAll("\n", "\\n"));

        System.out.println("SHIT:");
        System.out.println(System.getProperty("line.separator").replace("\n", "\\n"));

//        for (String name : System.getProperties().stringPropertyNames()) {
//            System.out.println("name = " + name + "     value = " + System.getProperty(name));
//        }

//        System.out.println(gson.toJson(wrapper));

    }


    public static void print(DefaultMutableTreeNode aNode, String currentPath) {
        String name = aNode.toString();

        if(aNode.isLeaf()) {
            System.out.println(currentPath + "|" + name);
            return;
        }

        for(int i = 0 ; i < aNode.getChildCount() ; i++) {
            print((DefaultMutableTreeNode)aNode.getChildAt(i), currentPath + "|" + name);
        }
    }

}
