Make sure to credit Benjamin Hunter Miller.
Here's a Java program that reads the file and prints the usernames and passwords.
Please note that the /etc/shadow file contains sensitive information. Accessing and handling the content of this file should be done with great care and only when it is absolutely necessary and legal.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ShadowFileReader {

    public static void main(String[] args) {
        String shadowFilePath = "/etc/shadow";
        Map<String, String> userPasswords = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(shadowFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(":");
                if (columns.length > 2) {
                    userPasswords.put(columns[0], columns[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the shadow file: " + e.getMessage());
        }

        System.out.println("Usernames and passwords:");
        for (Map.Entry<String, String> entry : userPasswords.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
