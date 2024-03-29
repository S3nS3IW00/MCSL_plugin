package plugin.mcsl.managers;

import plugin.mcsl.MainClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileManager {

    private static File root, serverRoot, chatLog;
    private static FileWriter chatLogWriter;

    public static void checkFiles() {
        chatLog = new File(getRoot() + File.separator + "chatlog.txt");
        if (!chatLog.exists()) {
            try {
                chatLog.getParentFile().mkdir();
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
