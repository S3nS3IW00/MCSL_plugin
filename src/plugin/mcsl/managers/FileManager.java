package plugin.mcsl.managers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import plugin.mcsl.main.MainClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {

    private File root, serverRoot, chatLog;
    private FileWriter chatLogWriter;

    public FileManager(File root, File serverRoot) {
        this.root = root;
        this.serverRoot = serverRoot;
    }

    public File getRoot() {
        return root;
    }

    public File getServerRoot() {
        return serverRoot;
    }

    public void checkFiles() {
        chatLog = new File(root + File.separator + "chatlog.txt");
        if (!chatLog.exists()) {
            try {
                chatLog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileWriter clearLog = new FileWriter(chatLog);
                clearLog.write("");
                clearLog.flush();
                clearLog.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            chatLogWriter = new FileWriter(chatLog, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getChatLog() {
        return chatLog;
    }

    public void writeToChatLog(String text) {
        if (text != null) {
            try {
                chatLogWriter.write(text + "\n");
                chatLogWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getFileNamesInJarPath(String jarPath) {
        List<String> fileNames = new ArrayList<>();
        CodeSource src = MainClass.class.getProtectionDomain().getCodeSource();
        try {
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith(jarPath)) {
                        String substringedName = name.substring(name.lastIndexOf("/") + 1);
                        if (!substringedName.equalsIgnoreCase("")) {
                            fileNames.add(substringedName);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }

    public Set<String> getUsers() {
        return MainClass.getUsers().getDefaults().keySet();
    }

    public String getUserGroup(String name) {
        return ((JSONObject) MainClass.getUsers().getDefaults().get(name)).get("group").toString();
    }

    public Set<String> getGroups() {
        return MainClass.getGroups().getDefaults().keySet();
    }

    public List<String> getGroupPermissions(String group) {
        JSONArray jsonArray = ((JSONArray) ((JSONObject) MainClass.getGroups().getDefaults().get(group)).get("permissions"));
        List<String> groupPerms = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            groupPerms.add(jsonArray.get(i).toString());
        }
        return groupPerms;
    }

}
