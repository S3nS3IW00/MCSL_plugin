package plugin.mcsl.managers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;
import plugin.mcsl.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GroupManager {

    public static void checkJson() {
        for (String group : MainClass.getGroups().getDefaults().keySet()) {
            JSONObject jsonObject = MainClass.getGroups().getObject(group);
            if (jsonObject.get("chatMode") == null) {
                jsonObject.put("chatMode", false);
            }
            if (jsonObject.get("permissions") == null) {
                jsonObject.put("permissions", new JSONArray());
            }
        }
        MainClass.getGroups().save();
    }

    public static void add(String group) {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("chatMode", false);
        jsonObject.put("permissions", jsonArray);

        MainClass.getGroups().addRawData(group, jsonObject);
        MainClass.getGroups().save();
    }

    public static void remove(String group) {
        MainClass.getGroups().getDefaults().remove(group);
        MainClass.getGroups().save();
        MainClass.getGroups().reload();
    }

    public static boolean isGroupExists(String group) {
        return MainClass.getGroups().getDefaults().containsKey(group);
    }

    public static List<String> getPermissions(String group) {
        List<String> permissions = new ArrayList<>();
        JSONArray permissionsArray = (JSONArray) MainClass.getGroups().getObject(group).get("permissions");
        for (int i = 0; i < permissionsArray.size(); i++) {
            permissions.add(permissionsArray.get(i).toString());
        }
        return permissions;
    }

    public static boolean hasPermission(String group, String permission) {
        return getPermissions(group).contains(permission);
    }

    public static void addPermission(String group, String permission) {
        JSONObject getUser = MainClass.getGroups().getObject(group);
        JSONArray getArray = (JSONArray) getUser.get("permissions");
        getArray.add(permission);
        MainClass.getGroups().save();
    }

    public static void removePermission(String group, String permission) {
        JSONObject getUser = MainClass.getGroups().getObject(group);
        JSONArray getArray = (JSONArray) getUser.get("permissions");
        if (hasPermission(group, permission)) {
            getArray.remove(permission);
            MainClass.getGroups().save();
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
        for (String username : MainClass.getUsers().getDefaults().keySet()) {
            if (UserManager.getGroup(username).equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChatModeEnabled(String group) {
        return (boolean) ((JSONObject) MainClass.getGroups().getDefaults().get(group)).get("chatMode");
    }

    public static void setChatMode(String group, boolean enabled) {
        JSONObject getGroup = MainClass.getGroups().getObject(group);
        getGroup.replace("chatMode", enabled);
        MainClass.getGroups().save();
    }

}
