I am Benjamin Hunter Miller credit me if you use this.


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class SMSApp {

    private static final String ACCOUNT_SID = "your-account-sid";
    private static final String AUTH_TOKEN = "your-auth-token";
    private static final String FROM_PHONE_NUMBER = "your-phone-number";
    private static List<String> messages = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        Scanner scanner = new Scanner(System.in);
        String command = "";

        while (!command.equalsIgnoreCase("exit")) {
            System.out.print(">> ");
            command = scanner.nextLine();

            if (command.startsWith("send ")) {
                String[] messageArgs = command.split(" ");
                sendMessage(messageArgs[1], Arrays.stream(messageArgs).skip(2).collect(Collectors.joining(" ")));
            } else if (command.equalsIgnoreCase("list")) {
                listMessages();
            } else if (command.startsWith("load ")) {
                String fileName = command.split(" ")[1];
                loadMessages(fileName);
            } else if (command.startsWith("save ")) {
                String fileName = command.split(" ")[1];
                saveMessages(fileName);
            } else if (command.equalsIgnoreCase("clear")) {
                clearMessages();
            }
        }

        scanner.close();
    }

    public static void sendMessage(String phoneNumber, String message) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((ACCOUNT_SID + ":" + AUTH_TOKEN).getBytes()))
            .post(HttpRequest.BodyPublishers.ofString(getFormData(phoneNumber, message)))
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                messages.add("To: " + phoneNumber + " Message: " + message);
                System.out.println("Message sent to: " + phoneNumber);
            } else {
                System.err.println("Error sending message: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    private static String getFormData(String phoneNumber, String message) {
        Map<String, String> formData = new HashMap<>();
        formData.put("To", phoneNumber);
        formData.put("From", FROM_PHONE_NUMBER);
        formData.put("Body", message);
        return formData.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .reduce("", (keyValue1, keyValue2) -> keyValue1 + "&" + keyValue2);
    }

    public static void listMessages() {
        int index = 1;
        for (String message : messages) {
            System.out.printf("%d. %s%n", index++, message);
        }
    }

    public static void loadMessages(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        }
        System.out.println("Messages loaded from file: " + fileName);
    }

    public static void saveMessages(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String message : messages) {
                writer.println(message);
            }
        }
        System.out.println("Messages saved to file: " + fileName);
    }

    public static void clearMessages() {
        messages.clear();
        System.out.println("Messages cleared");
    }
}
This code defines a messages list that stores the messages to be sent. The main loop reads commands from the user input, and performs the corresponding action based on the command.
The send command takes a phone number and a message as arguments, sends the message to the phone number using the Twilio API, and adds the message to the messages list.
The list command lists the messages in the messages list.
The load command loads messages from a file and adds them to the messages list.
The save command saves the messages in the messages list to a file.
The clear command clears the messages in the messages list.
Note that you need to replace your-account-sid, your-auth-token, and your-phone-number with your own Twilio account SID, auth token, and phone number. You also need to add the Twilio Java library to your project.
To run the code, save it to a file named SMSApp.java, compile it using javac SMSApp.java, and run it using java SMSApp.
