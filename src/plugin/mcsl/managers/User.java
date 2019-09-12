package plugin.mcsl.managers;

import plugin.mcsl.network.Client;

public class User {

    private String username, address, group;
    private Client client;

    public User(String username, String address, Client client) {
        this.username = username;
        this.address = address;
        this.client = client;
        this.group = UserManager.getGroup(username);
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getGroup() {
        return group;
    }

    public boolean hasPermission(String permission) {
        return GroupManager.hasPermission(getGroup(), permission);
    }

    public Client getClient() {
        return client;
    }
}
