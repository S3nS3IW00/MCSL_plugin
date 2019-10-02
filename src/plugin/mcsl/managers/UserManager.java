package plugin.mcsl.managers;

import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;
import plugin.mcsl.utils.Utils;

import java.io.File;
import java.util.Set;

public class UserManager {

    private static JsonManager users;

    public static void init() {
        users = new JsonManager(new File(MainClass.getPluginFolder() + File.separator + "users.json"));
        checkJson();
    }

    private static void checkJson() {
        for (String user : users.getDefaults().keySet()) {
            JSONObject jsonObject = users.getObject(user);
            if (jsonObject.get("password") == null) {
                jsonObject.put("password", "");
            }
            if (jsonObject.get("group") == null) {
                jsonObject.put("group", "");
            }
        }
        users.save();
    }

    public static void add(String username, String password, String group) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", password);
        jsonObject.put("group", group);

        users.addRawData(username, jsonObject);
        users.save();
    }

    public static void remove(String username) {
        users.getDefaults().remove(username);
        users.save();
    }

    public static Set<String> getUsers() {
        return users.getDefaults().keySet();
    }

    public static String getUserGroup(String name) {
        return ((JSONObject) users.getDefaults().get(name)).get("group").toString();
    }

    public static boolean isUserExists(String username) {
        return users.getDefaults().containsKey(username);
    }

    public static boolean isPasswordExists(String username, String passwordHash) {
        return users.getObject(username).get("password").toString().equalsIgnoreCase(passwordHash);
    }

    public static void setPassword(String username, String password) {
        JSONObject getUser = users.getObject(username);
        getUser.replace("password", HashManager.cuttedHash(password));
        users.save();
    }

    public static void setGroup(String username, String group) {
        JSONObject getUser = users.getObject(username);
        getUser.replace("group", group);
        users.save();
    }

    public static String getGroup(String username) {
        JSONObject getUser = users.getObject(username);
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
