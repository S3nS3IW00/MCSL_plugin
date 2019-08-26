package plugin.mcsl.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import plugin.mcsl.main.MainClass;
import plugin.mcsl.managers.User;
import plugin.mcsl.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventHandlers implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String prefix = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " INFO]: ";
        MainClass.getFileManager().writeToChatLog(prefix + player.getName() + ": " + message);

        for (User user : Utils.connectedUsers) {
            if (MainClass.getGroupManager().isChatModeEnabled(user.getGroup())) {
                user.getClient().sendData(prefix + player.getName() + ": " + message);
            }
        }
    }

}
