package plugin.mcsl;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.mcsl.commands.Command;
import plugin.mcsl.event.EventHandlers;
import plugin.mcsl.managers.*;
import plugin.mcsl.network.Server;

import java.io.File;
import java.util.Locale;

public class MainClass extends JavaPlugin {

    public static final String VERSION = "1.2";

    private static String prefix = "[MinecraftServerLauncher] ";
    private static JsonManager users;
    private static JsonManager groups;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("mcserverlauncher").setExecutor(new Command());
        this.getServer().getPluginManager().registerEvents(new EventHandlers(), this);
        users = new JsonManager(new File(getDataFolder() + File.separator + "users.json"));
        groups = new JsonManager(new File(getDataFolder() + File.separator + "groups.json"));
        FileManager.setRoot(getDataFolder());
        FileManager.setServerRoot(getServer().getWorldContainer());
        FileManager.checkFiles();
        UserManager.checkJson();
        GroupManager.checkJson();

        String language = (Locale.getDefault().getLanguage().equalsIgnoreCase("hu") || Locale.getDefault().getLanguage().equalsIgnoreCase("en") ? Locale.getDefault().getLanguage() : "en");
        if (getConfig().contains("language") && FileManager.getFileNamesInJarPath("plugin/mcsl/resources/languages").contains(getConfig().getString("language") + ".properties")) {
            language = getConfig().getString("language");
        }
        Language.loadLanguage(language);

        Server.startServer(getConfig().getInt("port"));
        if (getGroups().getDefaults().size() == 0) {
            System.out.println(getPrefix() + Language.getText("nogroupfoundalert"));
        }
        if (getUsers().getDefaults().size() == 0) {
            System.out.println(getPrefix() + Language.getText("nouserfoundalert"));
        }
        if (UpdateManager.needUpdate()) {
            System.out.println(getPrefix() + Language.getText("updateavailable"));
        }
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        Server.stopServer();
    }

    public static String getPrefix() {
        return prefix;
    }

    public static JsonManager getUsers() {
        return users;
    }

    public static JsonManager getGroups() {
        return groups;
    }
}
