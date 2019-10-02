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
    private static File pluginFolder;

    @Override
    public void onEnable() {
        pluginFolder = getDataFolder();
        saveDefaultConfig();
        this.getCommand("mcserverlauncher").setExecutor(new Command());
        this.getServer().getPluginManager().registerEvents(new EventHandlers(), this);
        FileManager.setRoot(getDataFolder());
        FileManager.setServerRoot(getServer().getWorldContainer());
        FileManager.checkFiles();
        GroupManager.init();
        UserManager.init();

        String language = (Locale.getDefault().getLanguage().equalsIgnoreCase("hu") || Locale.getDefault().getLanguage().equalsIgnoreCase("en") ? Locale.getDefault().getLanguage() : "en");
        if (getConfig().contains("language") && FileManager.getFileNamesInJarPath("plugin/mcsl/resources/languages").contains(getConfig().getString("language") + ".properties")) {
            language = getConfig().getString("language");
        }
        Language.loadLanguage(language);

        Server.startServer(getConfig().getInt("port"));
        if (GroupManager.getGroups().size() == 0) {
            System.out.println(getPrefix() + Language.getText("nogroupfoundalert"));
        }
        if (UserManager.getUsers().size() == 0) {
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

    public static File getPluginFolder() {
        return pluginFolder;
    }

    public static String getPrefix() {
        return prefix;
    }
}
