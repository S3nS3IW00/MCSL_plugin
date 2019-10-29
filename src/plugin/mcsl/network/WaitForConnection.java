package plugin.mcsl.network;

import plugin.mcsl.managers.Language;

import java.io.IOException;
import java.net.Socket;

public class WaitForConnection extends Thread {

    @Override
    public void run() {
        while (!Server.getServer().isClosed()) {
            try {
                Socket client = Server.getServer().accept();
                System.out.println("[" + client.getInetAddress().getHostAddress() + "] " + Language.getText("clienttryingconnectserver"));
                new Client(client);
            } catch (IOException e) {
                //empty catch block
            }
        }
    }
}
