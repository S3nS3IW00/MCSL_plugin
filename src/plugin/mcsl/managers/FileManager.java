package plugin.mcsl.managers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;

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

    private static File root, serverRoot, chatLog;
    private static FileWriter chatLogWriter;

    public static void checkFiles() {
        chatLog = new File(getRoot() + File.separator + "chatlog.txt");
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

    public static File getChatLog() {
        return chatLog;
    }

    public static void writeToChatLog(String text) {
        if (text != null) {
            try {
                chatLogWriter.write(text + "\n");
                chatLogWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getFileNamesInJarPath(String jarPath) {
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

    public static Set<String> getUsers() {
        return MainClass.getUsers().getDefaults().keySet();
    }

    public static String getUserGroup(String name) {
        return ((JSONObject) MainClass.getUsers().getDefaults().get(name)).get("group").toString();
    }

    public static Set<String> getGroups() {
        return MainClass.getGroups().getDefaults().keySet();
    }

    public static List<String> getGroupPermissions(String group) {
        JSONArray jsonArray = ((JSONArray) ((JSONObject) MainClass.getGroups().getDefaults().get(group)).get("permissions"));
        List<String> groupPerms = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            groupPerms.add(jsonArray.get(i).toString());
        }
        return groupPerms;
    }

    public static void setRoot(File root) {
        FileManager.root = root;
    }

    public static void setServerRoot(File serverRoot) {
        FileManager.serverRoot = serverRoot;
    }

    public static File getRoot() {
        return root;
    }

    public static File getServerRoot() {
        return serverRoot;
    }

}
