To create a Java tool to aid in discovering HTTP and HTTPS connectivity issues, you can use the `java.net` package, which includes classes for working with URLs, sockets, and streams. Here is an example of how you can use the `HttpURLConnection` class to test HTTP and HTTPS connectivity in Java:

```
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectivityChecker {

  // Test an HTTP or HTTPS URL
  public static void main(String[] args) throws Exception {
    // The URL to test
    URL url = new URL("https://www.example.com");

    // Open a connection to the URL
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

    // Set the request method
    connection.setRequestMethod("GET");

    // Send the request and get the response
    connection.connect();
    int responseCode = connection.getResponseCode();

    // Display the response code
    System.out.println("Response code: " + responseCode);

    // Check if the response code indicates success
    if (responseCode >= 200 && responseCode < 300) {
      // Read the response body
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
      reader.close();
    } else {
      // Display the response message
      System.out.println("Response message: " + connection.getResponseMessage());
    }
  }
}
```

This Java tool uses the `HttpURLConnection` class to open a connection to a specified URL, send a GET request, and read the response. The response code and message are then displayed, which can help you determine if there are any connectivity issues.

Keep in mind that this is just a simple example, and there are many other ways to test HTTP and HTTPS connectivity in Java. Depending on your specific needs, you may want to consider using other classes and libraries, such as the `java.net.Socket` class or the `org.apache.httpcomponents` library.

It is also important to note that testing HTTP and HTTPS connectivity can be a complex and time-consuming task, and it may require the use of multiple tools and techniques. It is generally recommended to start with the simplest tools and techniques, and then move on to more advanced tools as needed.
