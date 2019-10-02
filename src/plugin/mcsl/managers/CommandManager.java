package plugin.mcsl.managers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.mcsl.MainClass;
import plugin.mcsl.network.Client;
import plugin.mcsl.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommandManager {

    private Client client;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public CommandManager(Client client) {
        this.client = client;
    }

    public boolean isCommand(String data) {
        return data.startsWith("#");
    }

    public String getCommand(String data) {
        if (isCommand(data)) {
            data = data.replace("#", "");
            return data.split(";")[0];
        }
        return null;
    }

    public String[] getArgs(String data) {
        if (isCommand(data)) {
            data = data.replace("#" + getCommand(data) + ";", "");
            return data.split(";");
        }
        return null;
    }

    public String getServerCommand(String data) {
        return data.split(" ")[0];
    }

    public String[] getServerCommandArgs(String data) {
        data = data.replace(getServerCommand(data), "");
        return data.split(" ");
    }

    public void runCommand(String data) {
        String prefix = "[" + sdf.format(new Date()) + " INFO]: ";

        if (GroupManager.isChatModeEnabled(client.getUser().getGroup()) && !data.startsWith("#disconnect")) {
            Bukkit.broadcastMessage("[" + client.getUser().getUsername() + "] " + data);
            client.sendData(prefix + "[" + client.getUser().getUsername() + "] " + data);
            FileManager.writeToChatLog(prefix + "[" + client.getUser().getUsername() + "] " + data);
        } else {
            if (isCommand(data)) {
                switch (getCommand(data)) {
                    case "disconnect":
                        client.disconnect();
                        break;
                }
            } else {
                switch (getServerCommand(data)) {
                    case "say":
                        if (getServerCommandArgs(data).length > 0) {
                            StringBuilder msg = new StringBuilder();
                            for (String arg : getServerCommandArgs(data)) {
                                msg.append(arg).append(" ");
                            }
                            Bukkit.broadcastMessage("[" + client.getUser().getUsername() + "] " + msg);
                            FileManager.writeToChatLog(prefix + "[" + client.getUser().getUsername() + "] " + msg);
                            for (User user : Utils.connectedUsers) {
                                if (GroupManager.isChatModeEnabled(user.getGroup())) {
                                    user.getClient().sendData(prefix + "[" + client.getUser().getUsername() + "] " + msg);
                                }
                            }
                        }
                        break;
                    default:
                        if (client.getUser().hasPermission("server.*") || (client.getUser().hasPermission("server.sendcommand") && (client.getUser().hasPermission("server.command.*") || client.getUser().hasPermission("server.command." + data.split(" ")[0])))) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    System.out.println("[" + client.getClient().getInetAddress().getHostAddress() + "-" + client.getUser().getUsername() + "] " + Language.getText("command") + ": /" + data);
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
                                }
                            }.runTask(MainClass.getPlugin(MainClass.class));
                        } else {
                            client.sendData("#showerror;donthavepermission");
                        }
                }
            }
        }
    }

}
