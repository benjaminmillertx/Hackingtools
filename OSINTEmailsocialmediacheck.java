Make sure to credit Benjamin Hunter Miller made for OSINT professtional tool.In this program, I'll provide you with a Java program to check if an email address exists on MySpace, Twitter, and Facebook. The program will create a chart with the results, indicating if the account was found or not, and include the URL of the social media account if it exists.
To accomplish this task, you'll need to use a web scraping library like Jsoup.
First, add the Jsoup library to your Java project. You can add it as a dependency using Maven or Gradle. For Maven, add the following to your pom.xml:
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.14.3</version>
</dependency>
For Gradle, add the following to your build.gradle:
implementation 'org.jsoup:jsoup:1.14.3'
Next, create a Java class named In this example, I'll provide you with a Java program to check if an email address exists on MySpace, Twitter, and Facebook. The program will create a chart with the results, indicating if the account was found or not, and include the URL of the social media account if it exists.
To accomplish this task, you'll need to use a web scraping library like Jsoup.
First, add the Jsoup library to your Java project. You can add it as a dependency using Maven or Gradle. For Maven, add the following to your pom.xml:
<dependency>
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.14.3</version>
</dependency>
  import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SocialMediaEmailChecker {

    private static final String MYSPACE_URL = "https://myspace.com/findfriends/search/email/%s";
    private static final String TWITTER_URL = "https://twitter.com/search?q=%s&src=typd";
    private static final String FACEBOOK_URL = "https://www.facebook.com/public/%s";

    public static void main(String[] args) throws IOException {
        String email = "example@example.com";

        Map<String, Boolean> results = new HashMap<>();

        Boolean myspaceResult = checkMyspace(email);
        results.put("Myspace", myspaceResult);

        Boolean twitterResult = checkTwitter(email);
        results.put("Twitter", twitterResult);

        Boolean facebookResult = checkFacebook(email);
        results.put("Facebook", facebookResult);

        printResults(results);
    }

    private static Boolean checkMyspace(String email) throws IOException {
        String url = String.format(MYSPACE_URL, email);
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select(".friend_result");
        return !elements.isEmpty();
    }

    private static Boolean checkTwitter(String email) throws IOException {
        String url = String.format(TWITTER_URL, email);
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select(".ProfileCard");
        if (elements.isEmpty()) {
            elements = doc.select(".ProfileCard-container");
        }
        return !elements.isEmpty();
    }

    private static Boolean checkFacebook(String email) throws IOException {
        String url = String.format(FACEBOOK_URL, email);
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select(".profileLink");

        // Facebook returns a 200 OK status even if the email isn't found
        // Check if profileLink element exists to determine if the email is found
        return !elements.isEmpty();
    }

    private static void printResults(Map<String, Boolean> results) {
        System.out.println("Social Media Account Check");
        System.out.printf("%-15s | %-10s | %s%n", "Platform", "Found", "URL");
        System.out.println("--------------------------------------------------");

        for (Map.Entry<String, Boolean> entry : results.entrySet()) {
            Boolean found = entry.getValue();
            String platform = entry.getKey();
            String url = found ? getUrl(platform, email) : "";

            System.out.printf("%-15s | %-10s | %s%n", platform, found ? "Yes" : "No", url);
        }
    }

    private static String getUrl(String platform, String email) {
        switch (platform.toLowerCase()) {
            case "myspace":
                return String.format("https://myspace.com/findfriends/profile/%s", email);
            case "twitter":
                return String.format("https://twitter.com/intent/user?user=%s", email);
            case "facebook":
                return String.format("https://www.facebook.com/%s", email);
            default:
                return "";
        }
    }
}
This code includes the missing part of the getUrl method, which returns the URL for the found social media accounts.
To run the code, replace example@example.com in the main method with the email address you want to check.
The chart will display the results, indicating if the account was found or not, and include the URL of the social media account if it exists.
Make sure to test and adapt it to your specific use case. Additionally, be aware of the legal and ethical implications of scraping social media platforms; ensure you comply with their terms of service and have proper consent before using this tool
  

  
