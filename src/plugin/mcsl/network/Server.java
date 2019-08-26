package plugin.mcsl.network;

import plugin.mcsl.managers.Language;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private static ServerSocket server = null;
    private static int serverPort = 9999;

    public static void startServer(int port) {
        serverPort = port;
        try {
            server = new ServerSocket(port);
            System.out.println("[MinecraftServerLauncher] " + Language.getText("serverisrunningonport", port));
            Thread waitForConnection = new Thread(new WaitForConnection());
            waitForConnection.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        try {
            if (getServer() != null) {
                getServer().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerSocket getServer() {
        return server;
    }

    public static int getPort() {
        return serverPort;
    }
}
