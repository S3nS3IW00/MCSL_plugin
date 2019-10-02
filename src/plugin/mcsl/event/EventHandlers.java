package plugin.mcsl.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import plugin.mcsl.managers.FileManager;
import plugin.mcsl.managers.GroupManager;
import plugin.mcsl.managers.User;
import plugin.mcsl.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventHandlers implements Listener {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        String prefix = "[" + sdf.format(new Date()) + " INFO]: ";
        FileManager.writeToChatLog(prefix + player.getName() + ": " + message);

        for (User user : Utils.connectedUsers) {
            if (GroupManager.isChatModeEnabled(user.getGroup())) {
                user.getClient().sendData(prefix + player.getName() + ": " + message);
            }
        }
    }

}
