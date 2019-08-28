package plugin.mcsl.managers;

import org.json.simple.JSONObject;
import plugin.mcsl.MainClass;
import plugin.mcsl.utils.Utils;

public class UserManager {

    public UserManager() {
        checkJson();
    }

    public void checkJson() {
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

    public void add(String username, String password, String group) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", password);
        jsonObject.put("group", group);

        MainClass.getUsers().addRawData(username, jsonObject);
        MainClass.getUsers().save();
    }

    public void remove(String username) {
        MainClass.getUsers().getDefaults().remove(username);
        MainClass.getUsers().save();
    }

    public boolean isUserExists(String username) {
        return MainClass.getUsers().getDefaults().containsKey(username);
    }

    public boolean isPasswordExists(String username, String passwordHash) {
        return MainClass.getUsers().getObject(username).get("password").toString().equalsIgnoreCase(passwordHash);
    }

    public void setPassword(String username, String password) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        getUser.replace("password", HashManager.cuttedHash(password));
        MainClass.getUsers().save();
    }

    public void setGroup(String username, String group) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        getUser.replace("group", group);
        MainClass.getUsers().save();
    }

    public String getGroup(String username) {
        JSONObject getUser = MainClass.getUsers().getObject(username);
        return getUser.get("group").toString();
    }

    public boolean isJoinedUser(String username) {
        for (User user : Utils.connectedUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public User getUserFromUsername(String username) {
        for (User user : Utils.connectedUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }
}
