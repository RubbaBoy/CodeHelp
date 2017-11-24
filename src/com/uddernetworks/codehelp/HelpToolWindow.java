package com.uddernetworks.codehelp;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.codeEditor.printing.PrintOption;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.JBColor;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import com.uddernetworks.search.SearchThread;
import com.uddernetworks.snippet.CreateIndex;
import com.uddernetworks.snippet.JSONSnippet;
import com.uddernetworks.snippet.Snippet;
import com.uddernetworks.snippet.SnippetContainer;
import com.uddernetworks.vfile.VDirectory;
import com.uddernetworks.vfile.VFile;
import javafx.scene.text.TextFlow;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class HelpToolWindow implements ToolWindowFactory {

    private JPanel jpanel;
    private JPanel leftContainer;
    public static Tree jTree;
    private JScrollPane jScroll;
    private SearchTextField searchBar;
    private JPanel snippetContent;
    private JLayeredPane titleBarFrame;
    private JLabel snippetTitle;
    private JLabel snippetAuthor;
    private JLayeredPane centerContent;
    private JEditorPane snippetDescription;
    private JLabel bookmarkButton;
    private JLabel infoButton;
    private JEditorPane welcomeScreen;
    private Parser parser;

    public static DefaultMutableTreeNode root;
    public static DefaultMutableTreeNode bookmarkNode;

    private ImageIcon unbookmarkedIcon = new ImageIcon(getClass().getResource("/unbookmarked.png"));
    private ImageIcon bookmarkedIcon = new ImageIcon(getClass().getResource("/bookmarked.png"));
    private ImageIcon moreInfoIcon;

    public static List<CreateIndex.IndexFile> indexList;

    private SearchThread searchThread;
    private File snippets;
    private BookmarkManager bookmarkManager;
    private int currentId = 0;
    private JSONSnippet jsonSnippet;

    private boolean bookmarked = false;

    @Override
    public void init(ToolWindow toolWindow) {

    }

    private void OLDinit(Project project) {
        parser = Parser.builder().build();

        snippets = new File(PluginManager.getPlugin(PluginId.getId("com.uddernetworks.codehelp")).getPath().getAbsolutePath() + File.separator + "snippets");

        snippets.mkdirs();

        System.out.println("Checking for updates....");
        UpdateManager updateManager = new UpdateManager(snippets.getParentFile());
        updateManager.checkForUpdates(project);
        System.out.println("Done checking for updates!");

        Icon tempIcon = IconLoader.getIcon("/general/contextHelp.png");

        try {
            Method getRealIconField = tempIcon.getClass().getDeclaredMethod("getRealIcon");
            getRealIconField.setAccessible(true);
            moreInfoIcon = (ImageIcon) getRealIconField.invoke(tempIcon);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        createIndex(project, snippets);

        try {
            bookmarkManager = new BookmarkManager(snippets);
            welcomeScreen = new JEditorPane();

            welcomeScreen.setContentType("text/html");

            File welcomeFile = new File(snippets.getParentFile().getAbsolutePath() + File.separator + "Welcome.html");

            if (!welcomeFile.exists()) {
                welcomeFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(welcomeFile));
                String welcomeText = updateManager.makeGet("https://rubbaboy.me/codehelp/Welcome.html");
                writer.write(welcomeText);
                writer.close();

                welcomeScreen.setText(welcomeText);
            } else {
                String html = new String(Files.readAllBytes(Paths.get(welcomeFile.toURI())));
                welcomeScreen.setText(html);
            }


            welcomeScreen.setEditable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDoNotActivateOnStart() {
        return true;
    }

    private void resizeShit() {
        int rightWidth = jpanel.getWidth() - (200) - 30;

        snippetContent.setPreferredSize(new Dimension(rightWidth, jpanel.getHeight()));
        snippetContent.setBounds(200, 0, rightWidth, jpanel.getHeight());
        snippetContent.repaint();

        leftContainer.setBounds(0, 0, 200, jpanel.getHeight());
        leftContainer.setPreferredSize(new Dimension(200, jpanel.getHeight()));
        leftContainer.repaint();

        titleBarFrame.setOpaque(true);

        fitVertToContent(titleBarFrame);

        snippetTitle.setOpaque(true);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        OLDinit(project);

        unbookmarkedIcon = new ImageIcon(unbookmarkedIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        unbookmarkedIcon.getImage().flush();

        bookmarkedIcon = new ImageIcon(bookmarkedIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        bookmarkedIcon.getImage().flush();

        moreInfoIcon = new ImageIcon(moreInfoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        moreInfoIcon.getImage().flush();

        jpanel = new JPanel(null);

        leftContainer = new JPanel(null);

        jpanel.add(leftContainer);

        searchBar = new SearchTextField();

        leftContainer.add(searchBar);

        leftContainer.setBorder(new MatteBorder(0, 0, 0, 1, JBColor.GRAY));

        SnippetUtil util = new SnippetUtil();

        root = util.getTreeFromIndex(indexList, snippets);

        jTree = new Tree(root);

        bookmarkNode = bookmarkManager.addToRoot(root, jTree, indexList);

        bookmarkManager.setOnChange(bookmarks -> bookmarkManager.addToRoot(root, jTree, indexList));

        jTree.setRootVisible(false);

        jTree.addTreeSelectionListener(event -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();

            if (node == null) return;

            jsonSnippet = null;
            if (!(node.getUserObject() instanceof VFile)) {
                if (node.getUserObject() instanceof CreateIndex.IndexFile) {
                    jsonSnippet = ((CreateIndex.IndexFile) node.getUserObject()).getSnippet();
                } else if (node.getUserObject() instanceof JSONSnippet) {
                    jsonSnippet = (JSONSnippet) node.getUserObject();
                }
            } else {
                VFile indexFile = (VFile) node.getUserObject();
                jsonSnippet = getFileById(indexFile.getId()).getSnippet();
            }

            if (jsonSnippet == null) return;

            if (welcomeScreen != null) {
                welcomeScreen.setPreferredSize(new Dimension(0, 0));
                welcomeScreen.setBounds(0, 0, 0, 0);
                welcomeScreen.repaint();
                snippetContent.remove(welcomeScreen);
                welcomeScreen = null;

                bookmarkButton.show();
                infoButton.show();
            }

            snippetContent.show(true);

            updateBookmark(bookmarkManager.isBookmarked(jsonSnippet.getId()));

            snippetTitle.setText("<html>" + jsonSnippet.getTitle() + "</html>");
            snippetAuthor.setText("<html>" + jsonSnippet.getAuthor() + "</html>");
            snippetDescription.setText("<html>" + htmlFormat(jsonSnippet.getDescription()) + "</html>");
            currentId = jsonSnippet.getId();

            removeAllSpecific();

            LinkedList<Snippet> temp = new LinkedList<>(Arrays.asList(jsonSnippet.getSnippets()));

            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            LinkedHashMap<String, String> langs = new LinkedHashMap<>();
            for (Snippet snippet : temp) {
                map.put(snippet.getDescription(), snippet.getCode());
                langs.put(snippet.getDescription(), snippet.getLanguage());
            }

            centerContent.removeAll();
            createCodeBlock(centerContent, project, map, langs);
        });

        searchBar.addDocumentListener(new DocumentAdapter() {

            @Override
            protected void textChanged(DocumentEvent e) {
                searchThread.search(getText(e));
            }

            private String getText(DocumentEvent e) {
                try {
                    return e.getDocument().getText(0, e.getDocument().getLength());
                } catch (BadLocationException e1) {
                    return "";
                }
            }
        });


        jScroll = new JBScrollPane(jTree);
        jScroll.setBorder(null);

        leftContainer.add(jScroll);


        snippetContent = new JPanel();
        snippetContent.setLayout(new BoxLayout(snippetContent, BoxLayout.PAGE_AXIS));
        snippetContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        snippetContent.setAlignmentY(Component.TOP_ALIGNMENT);
        snippetContent.setBorder(new EmptyBorder(0, 15, 0, 0));

        /*
         * Start of top section
         */

        GridBag gb = new GridBag()
                .setDefaultAnchor(GridBagConstraints.NORTHWEST)
                .setDefaultPaddingY(0)
                .pady(0);
        titleBarFrame = new JLayeredPane();
        titleBarFrame.setLayout(new GridBagLayout());

        snippetContent.add(titleBarFrame);


        snippetTitle = new JLabel();
        snippetTitle.setVerticalTextPosition(JLabel.TOP);
        try {
            snippetTitle.setFont(getRoboto(36F, false));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        titleBarFrame.add(snippetTitle, gb.nextLine().anchor(GridBagConstraints.NORTHWEST).padx(70).fillCellHorizontally().weightx(1));

        infoButton = new JLabel(moreInfoIcon);
        infoButton.setMinimumSize(new Dimension(20, 20));
        infoButton.setMaximumSize(new Dimension(20, 20));
        infoButton.setSize(new Dimension(20, 20));
        infoButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        infoButton.setToolTipText("More Info");

        infoButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                InfoDialog dialog = new InfoDialog(true);

                dialog.addTitle(jsonSnippet.getTitle());
                dialog.addAuthor(jsonSnippet.getAuthor());
                dialog.addId("#" + jsonSnippet.getId());
                dialog.addDate(String.valueOf(jsonSnippet.getDate()));
                dialog.addTags(String.join(", ", jsonSnippet.getTags()));
                dialog.addReferences(jsonSnippet.getReferences());

                dialog.getPeer().setTitle("Snippet Extra Info");
                dialog.createCenterPanel();
                dialog.show();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        titleBarFrame.add(infoButton, gb.next().next().anchor(GridBagConstraints.EAST).padx(10));


        bookmarkButton = new JLabel(unbookmarkedIcon);
        bookmarkButton.setMinimumSize(new Dimension(25, 25));
        bookmarkButton.setMaximumSize(new Dimension(25, 25));
        bookmarkButton.setSize(new Dimension(25, 25));
        bookmarkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookmarkButton.setToolTipText("Bookmark");

        bookmarkButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                updateBookmark(!bookmarked);
                if (bookmarked) {
                    bookmarkManager.addBookmark(currentId);
                } else {
                    bookmarkManager.removeBookmark(currentId);
                }
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });

        bookmarkButton.setOpaque(true);
//        bookmarkButton.setBackground(JBColor.RED);
        titleBarFrame.add(bookmarkButton, gb.next().next().anchor(GridBagConstraints.EAST).padx(10));


//        snippetAuthor = new JLabel("Snippet Author");
        snippetAuthor = new JLabel();
        snippetAuthor.setVerticalTextPosition(JLabel.TOP);
        try {
            snippetAuthor.setFont(getRoboto(20F, true));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        titleBarFrame.add(snippetAuthor, gb.nextLine().fillCellHorizontally().weightx(1));


        snippetDescription = new JEditorPane("text/html", "<html></html>");
        snippetDescription.setOpaque(false);
        snippetDescription.setEditable(false);
        System.out.println("SIZE = " + snippetDescription.getFont().getSize());
        try {
            snippetDescription.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
            snippetDescription.setFont(getRoboto(snippetDescription.getFont().getSize(), false));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        snippetDescription.addHyperlinkListener(hle -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                System.out.println(hle.getURL());
                BrowserUtil.browse(hle.getURL());
            }
        });

        snippetDescription.setBorder(new JBEmptyBorder(10));
        titleBarFrame.add(snippetDescription, gb.nextLine().fillCellHorizontally().weightx(1));

        /*
         * End of top section
         */


        /*
         * Beginning of main content section
         */

        centerContent = new JLayeredPane();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.PAGE_AXIS));


//        JLabel spacerr = new JLabel("SPACER");
//        spacerr.setPreferredSize(new Dimension(100000, 500));
//        snippetContent.add(spacerr);

//        initializeCodeSnippets();
//        createCodeBlock(centerContent, project);


        snippetContent.add(centerContent);


        /*
         * End of main content section
         */

        jpanel.add(snippetContent);

//        snippetContent.show(false);

        bookmarkButton.hide();
        infoButton.hide();



        /*
         *  IMPORTANT
         */
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(jpanel, "", false);
        toolWindow.getContentManager().addContent(content);
        /*
         *  IMPORTANT
         */


        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }

            searchThread = new SearchThread();

            leftContainer.setBounds(0, 0, 200, jpanel.getHeight());
            leftContainer.setPreferredSize(new Dimension(200, jpanel.getHeight()));
            leftContainer.repaint();

            snippetContent.setPreferredSize(new Dimension(jpanel.getWidth() - (200) - 30, jpanel.getHeight()));
            snippetContent.setBounds(200, 0, jpanel.getWidth() - (200) - 30, jpanel.getHeight());
            snippetContent.repaint();

            searchBar.setPreferredSize(new Dimension(214, 28));
            searchBar.setBounds(5, 0, 190, 28);
            searchBar.repaint();


            jScroll.setPreferredSize(new Dimension(199, jpanel.getHeight() - 30));
            jScroll.setBounds(0, 30, 199, jpanel.getHeight() - 30);
            jScroll.repaint();

            snippetContent.add(welcomeScreen);
            welcomeScreen.setPreferredSize(new Dimension(snippetContent.getWidth(), snippetContent.getHeight()));
            welcomeScreen.setBounds(0, 0, snippetContent.getWidth(), snippetContent.getHeight());
            welcomeScreen.repaint();

            jpanel.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent evt) {
                    resizeShit();
                }
            });

            jpanel.repaint();
            resizeShit();
        }).start();

    }

    private String htmlFormat(String input) {
        Node document = parser.parse(input);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private void updateBookmark() {
        if (bookmarked) {
            bookmarkButton.setIcon(bookmarkedIcon);
        } else {
            bookmarkButton.setIcon(unbookmarkedIcon);
        }
    }

    private void updateBookmark(boolean newValue) {
        bookmarked = newValue;
        updateBookmark();
    }

    private void fitVertToContent(JComponent jComponent) {
        int totalHeight = 0;
        for (Component component : jComponent.getComponents()) {
            totalHeight += component.getBounds().height;
        }

        jComponent.setMaximumSize(new Dimension(Double.valueOf(jpanel.getWidth()).intValue() + 100, 200));
    }

    private void createIndex(Project project, File snippetsPath) {

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();

            progressIndicator.setIndeterminate(true);
            progressIndicator.setText("Indexing CodeHelp snippets...");

            try {
                CreateIndex createIndex = new CreateIndex(snippetsPath);
                File cbindex = new File(snippetsPath.getAbsolutePath() + File.separator + "index.chindex");
                if (!cbindex.exists()) cbindex.createNewFile();
                BufferedWriter out = new BufferedWriter(new FileWriter(cbindex));

                createIndex.createIndex(snippetsPath);
                indexList = createIndex.getFileIndexes();

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                out.write(gson.toJson(indexList));

                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            progressIndicator.setText("Finished");

        }, "Indexing Snippets", true, project);
    }

    public CreateIndex.IndexFile getFileById(int id) {
        CreateIndex.IndexFile ret = null;
        for (CreateIndex.IndexFile indexFile : indexList) {
            if (id == indexFile.getId()) {
                ret = indexFile;
                break;
            }
        }
        return ret;
    }


    private List<JPanel> myPanelsList = new ArrayList<>();

    private void removeAllSpecific() {

//        SwingUtilities.invokeLater(() -> {
//            spacers.forEach(spacer -> {
//                panell.remove(spacer);
//
//                spacer.revalidate();
//                spacer.repaint();
//            });
//
//            myPanelsList.forEach(jPanel -> {
//                panell.remove(jPanel);
//
//                jPanel.removeAll();
//                jPanel.revalidate();
//                jPanel.repaint();
//            });
//
//
//
//
//            spacers.clear();
//            myPanelsList.clear();
//            rawCodeSnippets.clear();
//        });
    }

    private void createCodeBlock(JLayeredPane panel, Project project, LinkedHashMap<String, String> map, LinkedHashMap<String, String> langs) {
        GridBag gb = new GridBag()
                .setDefaultAnchor(GridBagConstraints.NORTHWEST)
                .setDefaultInsets(JBUI.insets(2))
                .setDefaultPaddingY(0);
        for (String desc : map.keySet()) {
            String classString = map.get(desc);

            JPanel myPanel = new JPanel(new GridBagLayout());

            JEditorPane snippetDescriptionSpecific = new JEditorPane("text/html", "<html>" + htmlFormat(desc) + "</html>");
            snippetDescriptionSpecific.setOpaque(false);
            snippetDescriptionSpecific.setEditable(false);
            try {
                snippetDescriptionSpecific.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                snippetDescriptionSpecific.setFont(getRoboto(snippetDescriptionSpecific.getFont().getSize(), false));
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            }
            snippetDescriptionSpecific.addHyperlinkListener(hle -> {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    System.out.println(hle.getURL());
                    BrowserUtil.browse(hle.getURL());
                }
            });

            snippetDescriptionSpecific.setBorder(new EmptyBorder(20, 0, 0, 0));
            myPanel.add(snippetDescriptionSpecific, gb.nextLine().fillCellHorizontally().weightx(1));

            CodeDisplayTextField edtf = new CodeDisplayTextField(classString, project, Language.findLanguageByID(langs.get(desc)).getAssociatedFileType(), false);
            edtf.setOneLineMode(false);

            int linesInCode = countLines(classString);
            int lineHeight = edtf.getFontMetrics(edtf.getFont()).getHeight();

            edtf.setSize(10000, lineHeight * linesInCode);
            edtf.setPreferredSize(new Dimension(10000, lineHeight * linesInCode));
            edtf.setMaximumSize(new Dimension(10000, lineHeight * linesInCode));

            EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
            Color backgroundColor = scheme.getDefaultBackground();

            edtf.setBackground(backgroundColor);
            edtf.setOpaque(true);
            edtf.setEnabled(true);

            myPanel.add(edtf, gb.nextLine().next().pady(linesInCode * lineHeight).fillCellHorizontally().weightx(1));

            centerContent.add(myPanel);
        }


        addSpacer(centerContent);
    }

    private void addSpacer(JComponent panel) {
        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(10000, 10000));
        panel.add(spacer);
    }

    private int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private Font roboto;
    private Font robotoItalics;

    private Font getRoboto(float size, boolean italics) throws IOException, FontFormatException {
        if (italics) {
            robotoItalics = Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/RobotoItalics.ttf").getFile())).deriveFont(size);
        } else {
            roboto = Font.createFont(Font.TRUETYPE_FONT, new File(getClass().getResource("/Roboto.ttf").getFile())).deriveFont(size);
        }

        return (italics) ? robotoItalics : roboto;
    }
}
