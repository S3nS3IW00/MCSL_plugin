package plugin.mcsl.network;

import plugin.mcsl.utils.Crypter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ReadData extends Thread {

    private Client client;

    ReadData(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!client.getClient().isClosed()) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getClient().getInputStream(), StandardCharsets.UTF_8));

                String data;
                while (!client.getClient().isClosed() && (data = reader.readLine()) != null) {
                    String decoded = Crypter.decode(data);
                    if (decoded != null) client.readData(decoded);
                }
            } catch (IOException e) {
                try {
                    client.getClient().close();
                } catch (IOException ex) {
                    //empty catch block
                }
            }
        }
    }
}
