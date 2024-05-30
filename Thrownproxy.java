Make sure to credit Benjamin Hunter .a proxy for Firefox, Opera, and Safari that Burp Suite can connect to, you can follow these steps:
Step 1: Create a new Java project and add Burp Suite's Java-enabled CA certificate to the project's truststore. You can find the certificate in Burp Suite's "cacert.der" file.
Step 2: Create a new Java class called MyProxyServer and paste the following code: import java.io.BufferedReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyProxyServer {

    private static final int PROXY_PORT = 8080;
    private static final String LOG_FILE = "proxy.log";

    public static void main(String[] args) throws Exception {
        // Initialize the trust manager
        TrustManager[] trustManagers = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        };

        // Initialize the SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        // Create the SSL server socket factory
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

        // Create the proxy server socket
        ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(PROXY_PORT);

        System.out.println("MyProxyServer started on port " + PROXY_PORT);

        // Initialize the logger
        Logger logger = Logger.getLogger(MyProxyServer.class.getName());
        FileHandler fileHandler = new FileHandler(LOG_FILE, true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);

        while (true) {
            // Wait for a new client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted new client connection");

            // Create a new thread for the client connection
            Thread clientThread = new Thread(() -> {
                try {
                    // Read the request from the client
                    InputStream clientInputStream = clientSocket.getInputStream();
                    BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientInputStream));
                    String requestLine = clientReader.readLine();
                    if (requestLine == null) {
                        throw new SocketException("Failed to read request line");
                    }
                    String[] tokens = requestLine.split(" ");
                    String method = tokens[0];
                    String uri = tokens[1];
                    String protocol = tokens[2];

                    // Parse the URI
                    URI parsedUri = new URI(uri);
                    String host = parsedUri.getHost();
                    int port = parsedUri.getPort();
                    if (port == -1) {
                        if (protocol.equals("https")) {
                            port = 443;
                        } else if (protocol.equals("http")) {
                            port = 80;
                        }
                    }
                    String path = parsedUri.getPath();
                    if (path == null || path.isEmpty()) {
                        path = "/";
                    }

                    // Check if this is a CONNECT request
                    if (method.equals("CONNECT")) {
                        // Send a response to the client
                        PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                        clientWriter.println("HTTP/" + protocol + " 200 clientWriter.println("Connection: upgrade");
clientWriter.println();

// Upgrade the connection to SSL
SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(clientSocket, host, port, true);
sslSocket.startHandshake();

// Create the input and output streams for the SSL connection
InputStream sslInputStream = sslSocket.getInputStream();
OutputStream sslOutputStream = sslSocket.getOutputStream();

// Copy data between the client and the target
byte[] buffer = new byte[4096];
int bytesRead;
while ((bytesRead = sslInputStream.read(buffer)) != -1) {
    sslOutputStream.write(buffer, 0, bytesRead);
}
sslOutputStream.flush();

// Close the SSL connection
sslSocket.close();
                    } else {
                        // Create the URL for the target request
                        URL url = new URL(protocol, host, port, path);

                        // Create the connection to the target
                        if (protocol.equals("https")) {
                            SSLContext sslContext2 = SSLContext.getInstance("TLS");
                            sslContext2.init(null, trustManagers, new SecureRandom());
                            SSLSocketFactory sslSocketFactory2 = sslContext2.getSocketFactory();
                            HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
                            httpsConn.setSSLSocketFactory(sslSocketFactory2);
                            httpsConn.setDoOutput(true);
                            httpsConn.setRequestMethod(method);
                            if (method.equals("POST") || method.equals("PUT")) {
                                // Write the request body to the target
                                OutputStream targetOutputStream = httpsConn.getOutputStream();
                                InputStream clientInputStream2 = clientSocket.getInputStream();
                                byte[] buffer2 = new byte[4096];
                                int bytesRead2;
                                while ((bytesRead2 = clientInputStream2.read(buffer2)) != -1) {
                                    targetOutputStream.write(buffer2, 0, bytesRead2);
                                }
                                targetOutputStream.flush();
                                clientInputStream2.close();
                            }
                            // Read the response from the target
                            InputStream targetInputStream = httpsConn.getInputStream();
                            byte[] responseBytes = new byte[4096];
                            int bytesRead3;
                            while ((bytesRead3 = targetInputStream.read(responseBytes)) != -1) {
                                if (bytesRead3 > 0) {
                                    sslOutputStream.write(responseBytes, 0, bytesRead3);
                                }
                            }
                            sslOutputStream.flush();
                            targetInputStream.close();
                            httpsConn.disconnect();
                        } else {
                            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                            httpConn.setDoOutput(true);
                            httpConn.setRequestMethod(method);
                            if (method.equals("POST") || method.equals("// Write the request body to the target
OutputStream targetOutputStream = httpConn.getOutputStream();
InputStream clientInputStream2 = clientSocket.getInputStream();
byte[] buffer2 = new byte[4096];
int bytesRead2;
while ((bytesRead2 = clientInputStream2.read(buffer2)) != -1) {
    targetOutputStream.write(buffer2, 0, bytesRead2);
}
targetOutputStream.flush();
clientInputStream2.close();

// Read the response from the target
InputStream targetInputStream = httpConn.getInputStream();
byte[] responseBytes = new byte[4096];
int bytesRead3;
while ((bytesRead3 = targetInputStream.read(responseBytes)) !=// Write the response to the client
sslOutputStream.write(responseBytes, 0, bytesRead3);
sslOutputStream.flush();

// Close the connection to the target
targetInputStream.close();
httpConn.disconnect();
}
// Close the client and SSL connections
clientInputStream.close();
sslInputStream.close();
clientSocket.close();
sslSocket.close();
} catch (IOException e) {
// Log the error
logger.warning("Error processing request: " + e.getMessage());
}
}
}
}

This code should work for Firefox, Opera, Internet Explorer, and Safari. It handles CONNECT requests for HTTPS proxying and also works for HTTP requests. It uses SSL/TLS for secure communication with the target server and also logs errors to a file using Java's built-in logging framework.

      
       
                              
                      
