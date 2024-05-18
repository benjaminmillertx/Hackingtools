import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader {

    public static void main(String[] args) {
        String url = "https://example.com"; // URL of the webpage containing images
        String saveDir = "/path/to/save/directory"; // Directory to save images

        try {
            // Connect to the webpage and get the HTML document
            Document doc = Jsoup.connect(url).get();

            // Get all img elements
            Elements imgElements = doc.getElementsByTag("img");

            // Iterate through each img element
            for (Element img : imgElements) {
                // Get the source URL of the image
                String imgSrc = img.absUrl("src");

                // Download the image
                downloadImage(imgSrc, saveDir);
            }

            System.out.println("All images downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadImage(String imageUrl, String saveDir) throws IOException {
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();

        // Extract filename from URL
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // Save image to the specified directory
        OutputStream outputStream = new FileOutputStream(saveDir + File.separator + fileName);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        // Close streams
        outputStream.close();
        inputStream.close();
    }
}
