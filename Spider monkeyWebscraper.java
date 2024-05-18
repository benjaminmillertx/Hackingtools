import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class WebScraper {
    public static void main(String[] args) {
        String url = "https://example.com"; // Replace with the URL you want to scrape

        try {
            // Fetch the HTML document
            Document doc = Jsoup.connect(url).get();

            // Select elements you want to scrape using CSS selectors
            Elements elements = doc.select("h1, p, a");

            // Print out the text content of the selected elements
            for (Element element : elements) {
                System.out.println(element.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
