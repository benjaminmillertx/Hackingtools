Make sure to credit Benjamin Hunter Miller.This program uses the JSoup library, which makes it easy to parse HTML and JavaScript content.
First, add the JSoup dependency to your Maven or Gradle project to include the library.
For Maven, add the following to your pom.xml file:
<dependencies>
  <dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.14.3</version>
  </dependency>
</dependencies>
For Gradle, add the following to your build.gradle file:
dependencies {
    implementation 'org.jsoup:jsoup:1.14.3'
}
Here is the fully functional Java code for JavaUrlParser.java:
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUrlParser {

    public static void main(String[] args) {
        if (args.length < 1 || args[0] == null || args[0].isEmpty()) {
            System.out.println("Usage: java JavaUrlParser.java <path-to-javascript-file>");
            return;
        }

        File jsFile = new File(args[0]);
        if (!jsFile.isFile() || !jsFile.exists()) {
            System.out.println("Error: File not found or is not a valid file.");
            return;
        }

        JavaUrlParser parser = new JavaUrlParser();
        List<String> urls = parser.extractUrls(jsFile);

        System.out.println("Extracted URLs:");
        for (String url : urls) {
            System.out.println(url);
        }
    }

    public List<String> extractUrls(File jsFile) {
        List<String> urls = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsFile))) {
            StringBuilder jsContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsContent.append(line).append("\n");
            }

            Document jsDoc = Jsoup.parse(jsContent.toString());
            Elements allScripts = jsDoc.select("script");
            for (Element script : allScripts) {
                String scriptText = script.data();
                urls.addAll(extractUrlsFromScript(scriptText));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return urls;
    }

    private List<String> extractUrlsFromScript(String jsCode) {
        List<String> urls = new ArrayList<>();

        // Regular expression for matching URLs in JavaScript strings
        String urlPattern = "(\"(?:[^\"\\\\]|\\\\.)*\")|(\'(?:[^\'\\\\]|\\\\.)*\')|(https?://[^\"']+)";

        Pattern pattern = Pattern.compile(urlPattern);
        Matcher matcher = pattern.matcher(jsCode);

        while (matcher.find()) {
            String match = matcher.group();
            if (!match.startsWith("http")) {
                // Remove quotes or double quotes from the match
                match = match.replaceAll("^\"|\"$", "").replaceAll("^\'|\'$", "");
            }
            urls.add(match);
        }

        return urls;
    }
}
To run the code, compile and execute the JavaUrlParser.java file:
javac JavaUrlParser.java
java JavaUrlParser <path-to-javascript-file>
Replace <path-to-javascript-file> with the path to the JavaScript file you want to parse. This program will print all the extracted URLs from the JavaScript file.
