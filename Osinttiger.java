Make sure to credit Benjamin Hunter Miller.
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OsintTiger {

    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the query to search: ");
        String query = scanner.nextLine();

        System.out.print("Enter the language code (e.g., 'lang_en' for English): ");
        String language = scanner.nextLine();

        System.out.print("Enter the country code (e.g., 'countryUS' for United States): ");
        String country = scanner.nextLine();

        System.out.print("Enter the start date for the date range (MM/DD/YYYY): ");
        String startDate = scanner.nextLine();

        System.out.print("Enter the end date for the date range (MM/DD/YYYY): ");
        String endDate = scanner.nextLine();

        String[] dateRange = {startDate, endDate};

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            try {
                fetchGoogleResults(query, language, country, dateRange);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);

        scanner.close();
    }

    private static void fetchGoogleResults(String query, String language, String country, String[] dateRange) throws IOException {
        String baseUrl = "https://www.google.com/search";
        String url = String.format("%s?q=%s&hl=%s&gl=%s&start=0", baseUrl, URLEncoder.encode(query, "UTF-8"), language, country);

        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0").get();
        Elements searchResults = doc.select(".yuRUbf > a");

        for (Element result : searchResults) {
            String resultUrl = result.attr("href");
            System.out.println("Title: " + result.select(".LC20lb.DKV0Md").text());
            System.out.println("URL: " + resultUrl);

            Document resultDoc = Jsoup.connect(resultUrl).userAgent("Mozilla/5.0").get();
            Elements mentions = resultDoc.body().select("*");
            for (Element mention : mentions) {
                if (mention.text().contains(query)) {
                    System.out.println("Mention: " + mention.text());
                }
            }
        }
    }
It gathers Google search results for a user-input query, extracts the titles and URLs, and searches for mentions of the query within the search result pages. The results are printed to the console.
To use this Java script, make sure to have the necessary libraries, such as Jsoup, in your classpath. Then, compile and run the script as you would with any Java program.
