package com.uddernetworks.codehelp;

import javax.swing.tree.DefaultMutableTreeNode;

public class Temp {

    public static void main(String[] args) {
//        char[] chars = new char[] {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
//        Random random = new Random();
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(chars[random.nextInt(15)]);
//        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
            root.add(new DefaultMutableTreeNode("one"));
            root.add(new DefaultMutableTreeNode("two"));
            root.add(new DefaultMutableTreeNode("three"));
            root.add(new DefaultMutableTreeNode("four"));
            root.add(new DefaultMutableTreeNode("five"));

            DefaultMutableTreeNode outer = new DefaultMutableTreeNode("outer");
                outer.add(new DefaultMutableTreeNode("inner one"));
                outer.add(new DefaultMutableTreeNode("inner two"));
                outer.add(new DefaultMutableTreeNode("inner three"));
            root.add(outer);


        print(root, "");
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
