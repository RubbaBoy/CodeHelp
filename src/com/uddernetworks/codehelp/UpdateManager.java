package com.uddernetworks.codehelp;

import com.google.gson.Gson;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.math.NumberUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class UpdateManager {

    private File base;
    private File versionFile;

    public UpdateManager(File base) {
        this.base = base;
        versionFile = new File(base.getAbsolutePath() + File.separator + "version.chv");
    }

    public void checkForUpdates(Project project) {

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
            ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();

            progressIndicator.setIndeterminate(true);
            progressIndicator.setText("Checking for CodeHelp updates...");

            String newVersionString = makeGet("https://rubbaboy.me/codehelp/update/check.php");
            if (NumberUtils.isNumber(newVersionString)) {
                int newVersion = Integer.valueOf(newVersionString);
                int currentVersion = currentVersion();

                if (newVersion > currentVersion) {
                    System.out.println("New version found! v" + newVersion + " you are currently on version v" + currentVersion + " (" + (newVersion - currentVersion) + " versions behind)");
                    updateToVersion(progressIndicator, currentVersion, newVersion);
                } else {
                    System.out.println("No new version found!");
                }
            } else {
                System.out.println("Invalid version from server received: " + newVersionString);
            }

            progressIndicator.setText("Finished");
        }, "Updating", true, project);
    }

    private void updateToVersion(ProgressIndicator progressIndicator, int from, int to) {
//        for (String name : System.getProperties().stringPropertyNames()) {
//            System.out.println("name = " + name + "     value = " + System.getProperty(name));
//        }

        setVersion(to);
        progressIndicator.setText("Fetching update...");
        String json = makeGet("https://rubbaboy.me/codehelp/update/fetch.php?minver=" + from + "&maxver=" + to);
        Gson gson = new Gson();

        System.out.println("json = " + json);

        SnippetLocWrapper snippets[] = gson.fromJson(json, SnippetLocWrapper[].class);
        progressIndicator.setIndeterminate(false);
        final int total = snippets.length;
        progressIndicator.setText("Writing update 0/" + total);
        progressIndicator.setFraction(0.0F);

        int i = 0;
        for (SnippetLocWrapper snippet : snippets) {
            i++;
            progressIndicator.setText("Writing update " + i + "/" + total);
            progressIndicator.setFraction(i / total);

            File location = new File(base.getAbsolutePath() + File.separator + "snippets" + File.separator + snippet.getLocation().replace('/', File.separatorChar));
            location.mkdirs();
            File file = new File(location.getAbsolutePath() + File.separator + snippet.getJsonSnippet().getTitle() + ".ch");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                file.createNewFile();

                writer.write(gson.toJson(snippet.getJsonSnippet()).replace("%n", "\n"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVersion(int version) {
        try {
            if (!versionFile.exists()) {
                versionFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(versionFile));
            writer.write(String.valueOf(version));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int currentVersion() {
        try {
            if (!versionFile.exists()) {
                setVersion(0);
                return 0;
            }
            String strNumber = new String(Files.readAllBytes(Paths.get(versionFile.toURI())));
            if (NumberUtils.isNumber(strNumber)) {
                return Integer.valueOf(strNumber);
            } else {
                System.err.println("Invalid version input saved!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public String makeGet(String url) {
        try {
            System.out.println("Making Get to url \"" + url + "\"");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}