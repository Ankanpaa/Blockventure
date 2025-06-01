package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class UpdateChecker {

    public static boolean isLatestVersion;
    public static String latestVersion;
    public static int responseCode;
    
    public static void checkForUpdates() {
        try {
            String apiUrl = "https://api.github.com/repos/Ankanpaa/Blockventure/releases/latest";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("Authorization", null);

            responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            if (responseCode != 200) {
                System.out.println("Response Message: " + connection.getResponseMessage());
                return;
            }
            if(responseCode == 403 || responseCode == 429) {
                System.out.println("Rate limit exceeded. Please try again later.");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            latestVersion = jsonResponse.getString("tag_name");

            if (Main.gameVersion.equals(latestVersion)) {
                System.out.println("A new update is available: " + latestVersion);
                System.out.println("Download it here: " + jsonResponse.getString("html_url"));
                isLatestVersion = false;

            } else {
                System.out.println("You are using the latest version.");
                isLatestVersion = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to check for updates.");
            
        }
    }
}
