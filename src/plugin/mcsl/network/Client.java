package plugin.mcsl.network;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import plugin.mcsl.main.MainClass;
import plugin.mcsl.managers.CommandManager;
import plugin.mcsl.managers.Language;
import plugin.mcsl.managers.User;
import plugin.mcsl.utils.Crypter;
import plugin.mcsl.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Client {

    private Socket client;
    private boolean isAuth = false;
    private User user;
    private CommandManager commandManager;

    public Client(Socket client) {
        this.client = client;
        commandManager = new CommandManager(this);

        Thread readData = new Thread(new ReadData(this));
        readData.start();
    }

    void readData(String data) {
        if (isAuth) {
            getCommandManager().runCommand(data);
        } else {
            auth(data);
        }

    }

    public void sendData(String data) {
        data = Crypter.encode(data);

        try {
            OutputStream out = client.getOutputStream();
            PrintWriter writer = new PrintWriter(out);

            writer.write(data + "\n");
            writer.flush();
        } catch (IOException e) {
            //empty catch block
        }
    }

    private void auth(String data) {
        String decodedKey = Crypter.decode(data);

        if (decodedKey.startsWith("#auth")) {
            String[] splitter = decodedKey.replace("#auth;", "").split(";");
            String username = splitter[0];
            String password = splitter[1];

            for (User user : Utils.connectedUsers) {
                if (username.equalsIgnoreCase(user.getUsername())) {
                    System.out.println("[" + client.getInetAddress().getHostAddress() + "] " + Language.getText("clientalreadyconnected"));
                    sendData("#showerror;alreadyconnectedtoserver");
                    disconnect();
                    return;
                }
            }

            if (MainClass.getUserManager().isUserExists(username) && MainClass.getUserManager().isPasswordExists(username, password)) {
                User user = new User(username, client.getInetAddress().getHostAddress(), this);
                Utils.connectedUsers.add(user);
                setUser(user);
                System.out.println("[" + client.getInetAddress().getHostAddress() + "-" + user.getUsername() + "] " + Language.getText("clientconnected"));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        player.sendMessage("[" + client.getInetAddress().getHostAddress() + "-" + user.getUsername() + "] " + Language.getText("clientconnected"));
                    }
                }
                setAuth(true);
                sendData("#showinfo;connectedtoserver");
                if (!MainClass.getGroupManager().isChatModeEnabled(getUser().getGroup())) {
                    try {
                        for (String line : Files.readAllLines(new File(MainClass.getFileManager().getServerRoot().getAbsolutePath() + "/logs/latest.log").toPath(), Charset.forName("UTF-8"))) {
                            sendData(line.replaceAll("] \\[.+/", " "));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    org.apache.logging.log4j.core.Logger consoleLogger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
                    consoleLogger.addFilter(new ReadOutput(this));
                } else {
                    sendData("#showwarndialog;onlychat");
                    try {
                        for (String line : Files.readAllLines(MainClass.getFileManager().getChatLog().toPath(), Charset.forName("UTF-8"))) {
                            sendData(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }

            setAuth(false);
            sendData("#showerror;invalidusernameorpassword");
            System.out.println("[" + client.getInetAddress().getHostAddress() + "] " + Language.getText("clientfailedconnect"));
            disconnect();
        }

    }

    public Socket getClient() {
        return client;
    }

    public boolean isAuth() {
        return isAuth;
    }

    private void setAuth(boolean auth) {
        isAuth = auth;
    }

    public User getUser() {
        return user;
    }

    private void setUser(User user) {
        this.user = user;
    }

    public void disconnect() {
        try {
            sendData("#disconnect");
            System.out.println("[" + client.getInetAddress().getHostAddress() + (getUser() != null ? "-" + getUser().getUsername() : "") + "] " + Language.getText("clientdisconnected"));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    player.sendMessage("[" + client.getInetAddress().getHostAddress() + (getUser() != null ? "-" + getUser().getUsername() : "") + "] " + Language.getText("clientdisconnected"));
                }
            }
            client.close();
            Utils.connectedUsers.remove(getUser());
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void kick() {
        sendData("#showerror;externalserverforcedcloseconnection");
        System.out.println("[" + client.getInetAddress().getHostAddress() + "-" + getUser().getUsername() + "] " + Language.getText("clientkicked"));
        disconnect();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}
