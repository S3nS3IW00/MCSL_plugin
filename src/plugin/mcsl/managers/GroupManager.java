package plugin.mcsl.managers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;
import plugin.mcsl.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupManager {

    private static JsonManager groups;

    public static void init() {
        groups = new JsonManager(new File(MainClass.getPluginFolder() + File.separator + "groups.json"));
        checkJson();
    }

    private static void checkJson() {
        for (String group : groups.getDefaults().keySet()) {
            JSONObject jsonObject = groups.getObject(group);
            if (jsonObject.get("chatMode") == null) {
                jsonObject.put("chatMode", false);
            }
            if (jsonObject.get("permissions") == null) {
                jsonObject.put("permissions", new JSONArray());
            }
        }
        groups.save();
    }

    public static void add(String group) {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chatMode", false);
        jsonObject.put("permissions", jsonArray);

        groups.addRawData(group, jsonObject);
        groups.save();
    }

    public static void remove(String group) {
        groups.getDefaults().remove(group);
        groups.save();
        groups.reload();
    }

    public static Set<String> getGroups() {
        return groups.getDefaults().keySet();
    }

    public static boolean isGroupExists(String group) {
        return groups.getDefaults().containsKey(group);
    }

    public static List<String> getPermissions(String group) {
        List<String> permissions = new ArrayList<>();
        JSONArray permissionsArray = (JSONArray) groups.getObject(group).get("permissions");
        for (int i = 0; i < permissionsArray.size(); i++) {
            permissions.add(permissionsArray.get(i).toString());
        }
        return permissions;
    }

    public static boolean hasPermission(String group, String permission) {
        return getPermissions(group).contains(permission);
    }

    public static void addPermission(String group, String permission) {
        JSONObject getUser = groups.getObject(group);
        JSONArray getArray = (JSONArray) getUser.get("permissions");
        getArray.add(permission);
        groups.save();
    }

    public static void removePermission(String group, String permission) {
        JSONObject getUser = groups.getObject(group);
        JSONArray getArray = (JSONArray) getUser.get("permissions");
        if (hasPermission(group, permission)) {
            getArray.remove(permission);
            groups.save();
        }
    }

    public static boolean isJoinedGroup(String group) {
        for (User user : Utils.connectedUsers) {
            if (user.getGroup().equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasGroupUser(String group) {
        for (String username : UserManager.getUsers()) {
            if (UserManager.getGroup(username).equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChatModeEnabled(String group) {
        return (boolean) ((JSONObject) groups.getDefaults().get(group)).get("chatMode");
    }

    public static void setChatMode(String group, boolean enabled) {
        JSONObject getGroup = groups.getObject(group);
        getGroup.replace("chatMode", enabled);
        groups.save();
    }

}
