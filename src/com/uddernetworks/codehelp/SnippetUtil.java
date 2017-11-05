package com.uddernetworks.codehelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.uddernetworks.snippet.*;
import com.uddernetworks.vfile.TypeFile;
import com.uddernetworks.vfile.VDirectory;
import com.uddernetworks.vfile.VFile;
import org.apache.commons.lang.math.NumberUtils;
import org.codehaus.jettison.json.JSONObject;

import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class SnippetUtil {

    public static HashMap<String, JSONSnippet> paths = new LinkedHashMap<>();

//    public SnippetContainer[] convertToObject(String json) {
//        System.out.println("Converting \"" + json + "\"");
//
//        Gson gson = new Gson();
//        return gson.fromJson(json, SnippetContainer[].class);
//    }

    private String getRelPath(File path, File base) {
        return base.toURI().relativize(path.toURI()).getPath();
    }


    public static void main(String[] args) {
        SnippetUtil util = new SnippetUtil();
        List<CreateIndex.IndexFile> files = new ArrayList<>();

        try {
            CreateIndex createIndex = new CreateIndex(new File("E:\\RubbaBoysCodeHelp\\snippets"));
            File cbindex = new File("E:\\RubbaBoysCodeHelp\\snippets\\index.chindex");
//            if (cbindex.exists() && !cbindex.isDirectory()) cbindex.delete();
            if (!cbindex.exists()) cbindex.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(cbindex));

            createIndex.createIndex(new File("E:\\RubbaBoysCodeHelp\\snippets"));
            files = createIndex.getFileIndexes();

//            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            out.write(gson.toJson(indexList));

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        util.getTreeFromIndex(files, new File("E:\\RubbaBoysCodeHelp\\snippets"));
    }

    public DefaultMutableTreeNode getTreeFromIndex(List<CreateIndex.IndexFile> indexList, File base) {



//        base.toURI().relativize(file.toURI()).getPath()

        //            data.add(base.toURI().relativize(indexFile.getPath().toURI()).getPath());
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
                for (int i = 0; i < length - 1; i++){
                    System.out.print("   ");
                }

                System.out.println("+ " + lastFolder);
            }

            for (int i = 0; i < length; i++) {
                System.out.print("   ");
            }

            addAndOrCreate(new VFile(index.getTitle(), index.getId()), Arrays.copyOf(split, length));
            System.out.println("- " + split[length]);
        }


        System.out.println("\n\n\n\n");
        System.out.println(vDirectory.printOutTree(0));

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




    public static class Node {
        private final Map<String, Node> children = new TreeMap<>();

        public Node getChild(String name) {
            if (children.containsKey(name))
                return children.get(name);
            Node result = new Node();
            children.put(name, result);
            return result;
        }

        public Map<String, Node> getChildren() {
            return Collections.unmodifiableMap(children);
        }
    }

    private final Node root = new Node();

    private static final Pattern PATH_SEPARATOR = Pattern.compile("\\\\");
    public void addPath(String path) {
        String[] names = PATH_SEPARATOR.split(path);
        Node node = root;
        for (String name : names)
            node = node.getChild(name);
    }

    private static void printHtml(Node node, PrintStream out) {
        Map<String, Node> children = node.getChildren();
        if (children.isEmpty())
            return;
        out.println("<ul>");
        for (Map.Entry<String, Node> child : children.entrySet()) {
            out.print("<li>");
            out.print(child.getKey());
            printHtml(child.getValue(), out);
            out.println("</li>");
        }
        out.println("</ul>");
    }

    public void printHtml(PrintStream out) {
        printHtml(root, out);
    }


















//    public DefaultMutableTreeNode convertFromContainer(SnippetContainer[] snippetContainers) {
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
//
//        for (SnippetContainer snippetContainer : snippetContainers) {
//            recursivelyLoop(snippetContainer, root, "root", true);
//        }
//
//        System.out.println("paths = " + String.join("   \n", paths.keySet()));
//
//        return root;
//    }
//
//    private void recursivelyLoop(SnippetContainer container, DefaultMutableTreeNode defaultMutableTreeNode, String path, boolean first) {
//        DefaultMutableTreeNode currentlyOn = new DefaultMutableTreeNode(container.getSectionName());
//        defaultMutableTreeNode.add(currentlyOn);
//        for (Object object1 : container.getSnippetObjects()) {
//            if (first) System.out.println(object1);
//            if (object1 instanceof ArrayList) {
//
//                for (Object otherObj : (ArrayList) object1) {
//
//                    LinkedTreeMap mapp = (LinkedTreeMap) otherObj;
//                    SnippetObject object = createFromMap(mapp);
//                    if (object.isContainer()) {
//                        SnippetContainer newContainer = (SnippetContainer) object;
//                        recursivelyLoop(newContainer, currentlyOn, path + ((first) ? "|" + container.getSectionName() : "") + "|" + newContainer.getSectionName(), false);
//                    } else {
//                        JSONSnippet jsonSnippet = (JSONSnippet) object;
//
//                        System.out.println("Adding to paths - \"" + path + ((first) ? "|" + container.getSectionName() : "") + "|" + jsonSnippet.getTitle() + "\"");
//                        paths.put(path + ((first) ? "|" + container.getSectionName() : "") + "|" + jsonSnippet.getTitle(), jsonSnippet);
//                        currentlyOn.add(new DefaultMutableTreeNode(jsonSnippet.getTitle()));
//                    }
//                }
//            } else {
//                LinkedTreeMap mapp = (LinkedTreeMap) object1;
//                SnippetObject object = createFromMap(mapp);
//                if (object.isContainer()) {
//                    SnippetContainer newContainer = (SnippetContainer) object;
//                    recursivelyLoop(newContainer, currentlyOn, path + ((first) ? "|" + container.getSectionName() : "") + "|" + newContainer.getSectionName(), false);
//                } else {
//                    JSONSnippet jsonSnippet = (JSONSnippet) object;
//
//                    System.out.println("Adding to paths - \"" + path + ((first) ? "|" + container.getSectionName() : "") + "|" + jsonSnippet.getTitle() + "\"");
//                    paths.put(path + ((first) ? "|" + container.getSectionName() : "") + "|" + jsonSnippet.getTitle(), jsonSnippet);
//                    currentlyOn.add(new DefaultMutableTreeNode(jsonSnippet.getTitle()));
//                }
//            }
//
//        }
//    }
//
//
//    private SnippetObject createFromMap(LinkedTreeMap map) {
////        if (containsKeys(map, "id", "date", "Title", "Author", "snippets")) {
//
//            ArrayList<LinkedTreeMap> mappList = (ArrayList<LinkedTreeMap>) map.get("snippets");
//
//            ArrayList<Snippet> snippetList = new ArrayList<>();
//            mappList.forEach(treeMap -> snippetList.add(new Snippet((String) treeMap.get("description"), (String) treeMap.get("code"))));
//
//            Snippet[] snippets = snippetList.toArray(new Snippet[snippetList.size()]);
//
////            return new JSONSnippet(Double.valueOf((double) map.get("id")).intValue(), Double.valueOf((double) map.get("id")).longValue(), (String) map.get("Title"), (String) map.get("Author"), (String) map.get("description"), snippets);
////        } else {
//            return new SnippetContainer((String) map.get("sectionName"), map.get("snippetObjects"));
////        }
//    }
//
//    public static JSONSnippet getSnippetFromPath(TreePath path) {
//        StringBuilder newPath = new StringBuilder();
//
//        int var2 = 0;
//
//        for(int var3 = path.getPathCount(); var2 < var3; ++var2) {
//            if (var2 > 0) {
//                newPath.append("|");
//            }
//
//            newPath.append(path.getPathComponent(var2));
//        }
//
//        for (Map.Entry<String, JSONSnippet> entry : SnippetUtil.paths.entrySet()) {
//            String pat = entry.getKey();
//            if (pat.equals(newPath.toString())) {
//                return entry.getValue();
//            }
//        }
//
//        return SnippetUtil.paths.get(newPath.toString());
//    }
//
//    private boolean containsKeys(Map map, String... keys) {
//        for (String key : keys) {
//            if (!map.containsKey(key)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static void searchTextChanged(String text) {
//        long startTime = System.currentTimeMillis();
//
//        HashMap<Integer, JSONSnippet> snippets = new HashMap<>();
//
//        for (Map.Entry<String, JSONSnippet> entry : SnippetUtil.paths.entrySet()) {
//            JSONSnippet jsonSnippet = entry.getValue();
//            StringBuilder searchString = new StringBuilder(jsonSnippet.getDescription());
//            searchString.append(" ");
//
//            for (Snippet snippet : jsonSnippet.getSnippets()) {
//                searchString.append(snippet.getDescription()).append(" ");
//            }
//
//            snippets.put(findAmount(searchString.toString(), text), entry.getValue());
//        }
//
//        Map<Integer, JSONSnippet> map = new TreeMap<>(snippets);
//        Set<Map.Entry<Integer, JSONSnippet>> entries = map.entrySet();
//
//        List<Map.Entry<Integer, JSONSnippet>> list = new ArrayList<>(entries);
//
//        list.sort(Comparator.comparing(o -> (o.getKey())));
//
//        Set<Map.Entry<Integer, JSONSnippet>> resultSet = new LinkedHashSet<>(list);
//
//        for (Map.Entry<Integer, JSONSnippet> entry : resultSet) {
//            System.out.println(entry.getKey() + " -     " + entry.getValue().getTitle());
//        }
//
//
//
//        SwingUtilities.invokeLater(() -> {
//            HelpToolWindow.jTree.clearSelection();
//            HelpToolWindow.jTree.setModel(new DefaultTreeModel(hideAllBut(resultSet), false));
//
////            DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
////            root.add(new DefaultMutableTreeNode("one"));
////            root.add(new DefaultMutableTreeNode("two"));
////            root.add(new DefaultMutableTreeNode("three"));
////            root.add(new DefaultMutableTreeNode("four"));
////            root.add(new DefaultMutableTreeNode("five"));
////
////            DefaultMutableTreeNode outer = new DefaultMutableTreeNode("outer");
////            outer.add(new DefaultMutableTreeNode("inner one"));
////            outer.add(new DefaultMutableTreeNode("inner two"));
////            outer.add(new DefaultMutableTreeNode("inner three"));
////            root.add(outer);
//
////            HelpToolWindow.jTree.setModel(new DefaultTreeModel(root, false));
//            HelpToolWindow.jTree.revalidate();
//            HelpToolWindow.jTree.repaint();
//        });
//
//        System.out.println("\n\n        Search completed in " + (System.currentTimeMillis() - startTime) + "ms");
//    }
//
//    private static DefaultMutableTreeNode hideAllBut(Set<Map.Entry<Integer, JSONSnippet>> resultSet) {
//        List<Integer> good = new ArrayList<>();
//
//        for (Map.Entry<Integer, JSONSnippet> entry : resultSet) {
//            good.add(entry.getValue().getId());
//        }
//
//        DefaultMutableTreeNode dontModiofy = (DefaultMutableTreeNode) HelpToolWindow.root.getRoot();
//
//
//        DefaultMutableTreeNode roott = new DefaultMutableTreeNode();
//        roott.insert(dontModiofy, 0);
//
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
//
//        print(roott, root, "", good);
//        return root;
//    }
//
//    private static void print(DefaultMutableTreeNode aNode, DefaultMutableTreeNode newRoot, String currentPath, List<Integer> good) {
//        String name = aNode.toString();
//        if (name.trim().equals("") || name.trim().equals("root")) {
//            if (!aNode.isLeaf()) print((DefaultMutableTreeNode) aNode.getChildAt(0), newRoot, currentPath, good);
//            return;
//        }
//
//        if(aNode.isLeaf()) {
//            String path = (currentPath + "|" + name).substring(2);
//            for (Map.Entry<String, JSONSnippet> entry : SnippetUtil.paths.entrySet()) {
//                String pat = entry.getKey();
//                if (pat.equals(path)) {
//                    if (good.contains(entry.getValue().getId())) {
//                        DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
//                        temp.insert(aNode, 0);
//
//                        newRoot.add(temp);
//                    }
//                }
//            }
//
//        } else {
//            List<DefaultMutableTreeNode> nodes = new ArrayList<>();
//
//            final int count = aNode.getChildCount();
//            for (int i = 0; i < count; i++) {
//                nodes.add((DefaultMutableTreeNode) aNode.getChildAt(i));
//            }
//
//            for (DefaultMutableTreeNode nodee : nodes) {
//                print(nodee, aNode, currentPath + "|" + name, good);
//            }
//        }
//
//        newRoot.add(aNode);
//    }
//
//    private static int findAmount(String input, String search) {
//        int ret = 0;
//        while (true) {
//            if (input.contains(search)) {
//                input = input.replaceFirst(search, "");
//                ret++;
//            } else {
//                break;
//            }
//        }
//        return ret;
//    }
}
