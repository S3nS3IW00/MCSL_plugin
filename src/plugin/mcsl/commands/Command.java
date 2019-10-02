package plugin.mcsl.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import plugin.mcsl.MainClass;
import plugin.mcsl.managers.*;
import plugin.mcsl.network.Server;
import plugin.mcsl.utils.Utils;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length > 0) {
            if (!(sender.hasPermission("mcserverlauncher.*") || sender.hasPermission("mcserverlauncher." + args[0]))) {
                sender.sendMessage(MainClass.getPrefix() + Language.getText("nopermission"));
                return true;
            }
            if (args[0].equalsIgnoreCase("adduser")) {
                if (args.length > 3) {
                    if (!UserManager.isUserExists(args[1])) {
                        UserManager.add(args[1], HashManager.cuttedHash(args[2]), args[3]);
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("useradded"));
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("useralreadyexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("adduserusage"));
                }
            } else if (args[0].equalsIgnoreCase("removeuser")) {
                if (args.length > 1) {
                    if (UserManager.isUserExists(args[1])) {
                        UserManager.remove(args[1]);
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("userremoved"));
                        if (UserManager.isJoinedUser(args[1]))
                            UserManager.getUserFromUsername(args[1]).getClient().kick();
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("usernotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("removeuserusage"));
                }
            } else if (args[0].equalsIgnoreCase("addpermission")) {
                if (args.length > 2) {
                    if (GroupManager.isGroupExists(args[1])) {
                        if (!GroupManager.hasPermission(args[1], args[2])) {
                            GroupManager.addPermission(args[1], args[2]);
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("permissionadded"));
                        } else {
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("permissionalreadyexists"));
                        }
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupnotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("addpermissionusage"));
                }
            } else if (args[0].equalsIgnoreCase("removepermission")) {
                if (args.length > 2) {
                    if (GroupManager.isGroupExists(args[1])) {
                        if (GroupManager.hasPermission(args[1], args[2])) {
                            GroupManager.removePermission(args[1], args[2]);
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("permissionremoved"));
                        } else {
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("permissionnotexists"));
                        }
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupnotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("removepermissionusage"));
                }
            } else if (args[0].equalsIgnoreCase("addgroup")) {
                if (args.length > 1) {
                    if (!GroupManager.isGroupExists(args[1])) {
                        GroupManager.add(args[1]);
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupadded"));
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupalreadyexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("addgroupusage"));
                }
            } else if (args[0].equalsIgnoreCase("removegroup")) {
                if (args.length > 1) {
                    if (GroupManager.isGroupExists(args[1])) {
                        if (!GroupManager.hasGroupUser(args[1])) {
                            GroupManager.remove(args[1]);
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("groupremoved"));
                            if (GroupManager.isJoinedGroup(args[1])) {
                                for (User user : Utils.connectedUsers) {
                                    if (user.getGroup().equalsIgnoreCase(args[1])) {
                                        user.getClient().kick();
                                    }
                                }
                            }
                        } else {
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("userassignedtogroup"));
                        }
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupnotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("removegroupusage"));
                }
            } else if (args[0].equalsIgnoreCase("changeuserpassword")) {
                if (args.length > 2) {
                    if (UserManager.isUserExists(args[1])) {
                        UserManager.setPassword(args[1], args[2]);
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("passwordchanged"));
                        if (UserManager.isJoinedUser(args[1]))
                            UserManager.getUserFromUsername(args[1]).getClient().kick();
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("usernotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("passwordchangedusage"));
                }
            } else if (args[0].equalsIgnoreCase("changeusergroup")) {
                if (args.length > 2) {
                    if (UserManager.isUserExists(args[1])) {
                        UserManager.setGroup(args[1], args[2]);
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupchanged"));
                        if (UserManager.isJoinedUser(args[1]))
                            UserManager.getUserFromUsername(args[1]).getClient().kick();
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("usernotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("groupchangedusage"));
                }
            } else if (args[0].equalsIgnoreCase("togglechatmode")) {
                if (args.length > 1) {
                    if (GroupManager.isGroupExists(args[1])) {
                        GroupManager.setChatMode(args[1], !GroupManager.isChatModeEnabled(args[1]));
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("chatmodechanged", Language.getText(GroupManager.isChatModeEnabled(args[1]) ? "on" : "off")));
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupnotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("togglechatmodeusage"));
                }
            } else if (args[0].equalsIgnoreCase("grouplist")) {
                if (GroupManager.getGroups().size() > 0) {
                    sender.sendMessage("-----[MinecraftServerLauncher]-----");
                    for (String group : GroupManager.getGroups()) {
                        sender.sendMessage("- " + group);
                    }
                    sender.sendMessage("-----[]-----");
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("nogroupfoundalert"));
                }
            } else if (args[0].equalsIgnoreCase("userlist")) {
                if (UserManager.getUsers().size() > 0) {
                    sender.sendMessage("-----[MinecraftServerLauncher]-----");
                    for (String user : UserManager.getUsers()) {
                        sender.sendMessage("- " + Language.getText("username") + ": " + user + " | " + Language.getText("group") + ": " + UserManager.getUserGroup(user));
                    }
                    sender.sendMessage("-----[]-----");
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("nouserfoundalert"));
                }
            } else if (args[0].equalsIgnoreCase("onlineuserlist")) {
                if (Utils.connectedUsers.size() > 0) {
                    sender.sendMessage("-----[MinecraftServerLauncher]-----");
                    for (User user : Utils.connectedUsers) {
                        sender.sendMessage("- " + Language.getText("username") + ": " + user.getUsername() + " | " + Language.getText("group") + ": " + user.getGroup());
                    }
                    sender.sendMessage("-----[]-----");
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("noonlineuserfound"));
                }
            } else if (args[0].equalsIgnoreCase("permissionlist")) {
                if (args.length > 1) {
                    if (GroupManager.isGroupExists(args[1])) {
                        if (GroupManager.getPermissions(args[1]).size() > 0) {
                            sender.sendMessage("-----[MinecraftServerLauncher]-----");
                            for (String permission : GroupManager.getPermissions(args[1])) {
                                sender.sendMessage("- " + permission);
                            }
                            sender.sendMessage("-----[]-----");
                        } else {
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("groupdonthavepermission"));
                        }
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("groupnotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("permissionlistusage"));
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length > 1) {
                    if (UserManager.isUserExists(args[1])) {
                        if (UserManager.isJoinedUser(args[1])) {
                            UserManager.getUserFromUsername(args[1]).getClient().kick();
                        } else {
                            sender.sendMessage(MainClass.getPrefix() + Language.getText("clientnotconnected"));
                        }
                    } else {
                        sender.sendMessage(MainClass.getPrefix() + Language.getText("usernotexists"));
                    }
                } else {
                    sender.sendMessage(MainClass.getPrefix() + Language.getText("kickusage"));
                }
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("-----[MinecraftServerLauncher]-----\n" +
                        "- " + Language.getText("serverisrunningonport") + " " + Server.getPort() + "\n" +
                        "- " + Utils.connectedUsers.size() + " " + Language.getText("clientsconnected") + "\n" +
                        "-----[]-----");
            } else {
                showHelp(sender);
            }
        } else {
            showHelp(sender);
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage("-----[MinecraftServerLauncher]-----\n" +
                "- /mcsl adduser <" + Language.getText("username") + "> <" + Language.getText("password") + ">\n" +
                "- /mcsl removeuser <" + Language.getText("username") + ">\n" +
                "- /mcsl addpermission <" + Language.getText("group") + "> <" + Language.getText("permission") + ">\n" +
                "- /mcsl removepermission <" + Language.getText("group") + "> <" + Language.getText("permission") + ">\n" +
                "- /mcsl addgroup <" + Language.getText("group") + ">\n" +
                "- /mcsl removegroup <" + Language.getText("group") + ">\n" +
                "- /mcsl changeuserpassword <" + Language.getText("username") + "> <" + Language.getText("password") + ">\n" +
                "- /mcsl changeusergroup <" + Language.getText("username") + "> <" + Language.getText("group") + ">\n" +
                "- /mcsl togglechatmode <" + Language.getText("group") + ">\n" +
                "- /mcsl grouplist\n" +
                "- /mcsl userlist\n" +
                "- /mcsl onlineuserlist\n" +
                "- /mcsl permissionlist <" + Language.getText("group") + ">\n" +
                "- /mcsl kick <" + Language.getText("username") + ">\n" +
                "- /mcsl info\n" +
                "-----[]-----");
    }
}
