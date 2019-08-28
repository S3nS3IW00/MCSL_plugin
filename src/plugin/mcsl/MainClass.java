package plugin.mcsl;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.mcsl.commands.Command;
import plugin.mcsl.event.EventHandlers;
import plugin.mcsl.managers.*;
import plugin.mcsl.network.Server;

import java.io.File;
import java.util.Locale;

public class MainClass extends JavaPlugin {

    public static final String VERSION = "2.0";

    private static String prefix = "[MinecraftServerLauncher] ";
    private static JsonManager users;
    private static JsonManager groups;
    private static UserManager userManager;
    private static GroupManager groupManager;
    private static FileManager fileManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.getCommand("mcserverlauncher").setExecutor(new Command());
        this.getServer().getPluginManager().registerEvents(new EventHandlers(), this);
        users = new JsonManager(new File(getDataFolder() + File.separator + "users.json"));
        groups = new JsonManager(new File(getDataFolder() + File.separator + "groups.json"));
        fileManager = new FileManager(getDataFolder(), getServer().getWorldContainer());
        userManager = new UserManager();
        groupManager = new GroupManager();
        checkFiles();
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

    public static UserManager getUserManager() {
        return userManager;
    }

    public static GroupManager getGroupManager() {
        return groupManager;
    }

    public static JsonManager getUsers() {
        return users;
    }

    public static JsonManager getGroups() {
        return groups;
    }

    public static FileManager getFileManager() {
        return fileManager;
    }

    private void checkFiles() {
        this.saveDefaultConfig();
        fileManager.checkFiles();
        String language = (Locale.getDefault().getLanguage().equalsIgnoreCase("hu") || Locale.getDefault().getLanguage().equalsIgnoreCase("en") ? Locale.getDefault().getLanguage() : "en");
        if (getConfig().contains("language") && getFileManager().getFileNamesInJarPath("plugin/mcsl/resources/languages").contains(getConfig().getString("language") + ".properties")) {
            language = getConfig().getString("language");
        }
        Language.loadLanguage(language);
    }
}
