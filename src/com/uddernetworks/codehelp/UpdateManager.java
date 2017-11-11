package com.uddernetworks.codehelp;

import com.google.gson.Gson;
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

    public void checkForUpdates() {
        String newVersionString = makeGet("https://rubbaboy.me/codehelp/update/check.php");
        if (NumberUtils.isNumber(newVersionString)) {
            int newVersion = Integer.valueOf(newVersionString);
            int currentVersion = currentVersion();

            if (newVersion > currentVersion) {
                System.out.println("New version found! v" + newVersion + " you are currently on version v" + currentVersion + " (" + (newVersion - currentVersion) + " versions behind)");
                updateToVersion(currentVersion, newVersion);
            } else {
                System.out.println("No new version found!");
            }
        } else {
            System.out.println("Invalid version from server received: " + newVersionString);
        }
    }

    private void updateToVersion(int from, int to) {
        setVersion(to);
        String json = makeGet("https://rubbaboy.me/codehelp/update/fetch.php?minver=" + from + "&maxver=" + to);
        Gson gson = new Gson();
        SnippetLocWrapper snippets[] = gson.fromJson(json, SnippetLocWrapper[].class);
        for (SnippetLocWrapper snippet : snippets) {
            File location = new File(base.getAbsolutePath() + File.separator + "snippets" + File.separator + snippet.getLocation().replace('/', File.separatorChar));
            location.mkdirs();
            File file = new File(location.getAbsolutePath() + File.separator + snippet.getJsonSnippet().getTitle() + ".ch");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                file.createNewFile();
                writer.write(gson.toJson(snippet.getJsonSnippet()));
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
            String strNumber = new String(Files.readAllBytes(Paths.get(versionFile.toURI())));
            if (NumberUtils.isNumber(strNumber)) {
                return Integer.valueOf(strNumber);
            } else {
                System.err.println("Invalid version input saved!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            setVersion(1);
        }
        return 1;
    }


    private String makeGet(String url) {
        try {
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