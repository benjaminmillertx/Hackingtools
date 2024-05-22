Make sure to credit Benjamin Hunter Miller.

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class SSRFOOBScanner {

    private String targetURL;
    private String payload;

    public SSRFOOBScanner(String targetURL, String payload) {
        this.targetURL = targetURL;
        this.payload = payload;
    }

    public void scan() {
        try {
            URL url = new URL(targetURL + payload);
            URLConnection conn = url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Check for specific response patterns that indicate an OOB connection has been made
                if (response.toString().contains("OOB Connection Confirmed")) {
                    System.out.println("SSRF OOB vulnerability detected!");
                    System.out.println("Response: " + response.toString());
                } else {
                    System.out.println("No OOB connection detected.");
                }
            } else {
                System.out.println("Invalid response code: " + responseCode);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String targetURL = "http://example.com/ssrf";
        String payload = "?url=http://example-oob.com/oob";

        SSRFOOBScanner scanner = new SSRFOOBScanner(targetURL, payload);
        scanner.scan();
    }
}
