package plugin.mcsl.managers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import plugin.mcsl.MainClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.URL;
import java.net.URLConnection;

public class UpdateManager {

    private static String latestVersion, htmlUrl;

    static {
        StringBuilder jsonString = new StringBuilder();
        try {
            if (Inet4Address.getByName(new URL("https://api.github.com").getHost()).isReachable(3000)) {
                URL url = new URL("https://api.github.com/repos/S3nS3IW00/mcserverlauncher/releases/latest");
                URLConnection connection = url.openConnection();
                connection.connect();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    jsonString.append(inputLine);
                }
                in.close();
            }
        } catch (IOException e) {
            //empty catch block
        }
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString.toString());
            JSONArray assets = (JSONArray) jsonObject.get("assets");

            String asset = null;
            int i = 0;
            while (i < assets.size() && !(asset = (String) ((JSONObject) assets.get(i)).get("name")).startsWith("MCSL_plugin")) {
                i++;
            }
            if (i < assets.size()) {
                latestVersion = asset.substring(12, 22);
                htmlUrl = (String) ((JSONObject) assets.get(i)).get("browser_download_url");
            }
        } catch (ParseException e) {
            //empty catch block
        }
    }

    public static boolean needUpdate() {
        return latestVersion != null && !latestVersion.equalsIgnoreCase(MainClass.VERSION);
    }

    public static String getLatestVersion() {
        return latestVersion;
    }

    public static String getHtmlUrl() {
        return htmlUrl;
    }
}
