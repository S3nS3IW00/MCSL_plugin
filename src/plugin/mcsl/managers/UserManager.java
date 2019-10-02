package plugin.mcsl.managers;

import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;
import plugin.mcsl.utils.Utils;

import java.util.Set;

public class UserManager {

    public static void checkJson() {
        for (String user : MainClass.getUsers().getDefaults().keySet()) {
            JSONObject jsonObject = MainClass.getUsers().getObject(user);
            if (jsonObject.get("password") == null) {
                jsonObject.put("password", "");
            }
            if (jsonObject.get("group") == null) {
                jsonObject.put("group", "");
            }
        }
        MainClass.getUsers().save();
    }

    public static void add(String username, String password, String group) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", password);
        jsonObject.put("group", group);

        MainClass.getUsers().addRawData(username, jsonObject);
        MainClass.getUsers().save();
    }

    public static void remove(String username) {
        MainClass.getUsers().getDefaults().remove(username);
        MainClass.getUsers().save();
    }

    public static Set<String> getUsers() {
        return MainClass.getUsers().getDefaults().keySet();
    }

    public static String getUserGroup(String name) {
        return ((JSONObject) MainClass.getUsers().getDefaults().get(name)).get("group").toString();
    }

    public static boolean isUserExists(String username) {
        return MainClass.getUsers().getDefaults().containsKey(username);
    }

    public static boolean isPasswordExists(String username, String passwordHash) {
        return MainClass.getUsers().getObject(username).get("password").toString().equalsIgnoreCase(passwordHash);
    }

    public static void setPassword(String username, String password) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        getUser.replace("password", HashManager.cuttedHash(password));
        MainClass.getUsers().save();
    }

    public static void setGroup(String username, String group) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        getUser.replace("group", group);
        MainClass.getUsers().save();
    }

    public static String getGroup(String username) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        return getUser.get("group").toString();
    }

    public static boolean isJoinedUser(String username) {
        for (User user : Utils.connectedUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static User getUserFromUsername(String username) {
        for (User user : Utils.connectedUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
}
