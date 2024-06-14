import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.sun.media.jai.codec.CaptureDeviceCodec;
import com.sun.media.jai.codec.Format;
import com.sun.media.jai.codec.FrameGrabber;
import com.sun.media.jai.codec.ImageFormatDescriptor;
import com.sun.media.jai.codec.ImageFormatException;
import com.sun.media.jai.codec.MediaLocator;

public class WebcamEmailer {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final int FRAME_COUNT = 20 * 25; // 20 seconds at 25 fps
    private static FrameGrabber grabber;
    private static BufferedImage image;
    private static ByteArrayOutputStream outputStream;
    private static int frameCounter = 0;

    public static void main(String[] args) throws Exception {
        // Initialize the webcam
        grabber = new FrameGrabber(new MediaLocator("vfw://0"));
        grabber.setFormat(new Format(ImageFormatDescriptor.getDescriptor(ImageFormatDescriptor.YUV420)));
        grabber.setDispose(FrameGrabber.RELEASE);
        grabber.setCompressed(true);

        // Listen for failed login attempts
        while (true) {
            System.out.print("Enter password: ");
            String password = System.console().readLine();

            if (password.equals("wrongpassword")) {
                // Start the webcam and capture a 20-second video
                outputStream = new ByteArrayOutputStream();
                while (frameCounter < FRAME_COUNT) {
                    grabber.grabFrame();
                    image = grabber.getImage();
                    ImageIO.write(image, "jpg", outputStream);
                    frameCounter++;
                }

                // Save the video to a file
                File videoFile = new File("webcam_video.avi");
                OutputStream fileOutputStream = new FileOutputStream(videoFile);
                fileOutputStream.write(outputStream.toByteArray());
                fileOutputStream.close();

                // Send the video as an email attachment
                sendEmail("sender@example.com", "recipient@example.com", "Failed Login Attempt", "Here is a video of the failed login attempt.", videoFile);

                break;
            }
        }

        // Release the webcam resources
        grabber.stop();
        grabber.dispose();
    }

    private static void sendEmail(String from, String to, String subject, String body, File... attachments) throws MessagingException {
        // Set up the email properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Set up the email session
        Session session = Session.getDefaultInstance(props, null);

        // Create the email message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);

        // Add the attachments to the message
        MimeMultipart multipart = new MimeMultipart();
        message.setContent(multipart);
        for (File attachment : attachments) {
            MimeBodyPart bodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            bodyPart.setDataHandler(new DataHandler(source));
            bodyPart.setFileName(attachment.getName());
            multipart.addBodyPart(bodyPart);
        }

        // Send the email
        Transport.send(message);
    }
}
Please note that this is just a basic example, and you should modify the code to fit your specific needs and use case. You should also consider the privacy implications of capturing and sending video footage, and ensure that you comply with all relevant laws and regulations.
Also, you will need to install the Java Media Framework (JMF) and JavaMail API.
For JMF, you can download it from the official Oracle website:
<https://www.oracle.com/java/technologies/javase-jmf-downloads.html>
For JavaMail, you can download it from the official Maven repository:
<https://search.maven.org/artifact/com.sun.mail/javax.mail>
You can add the JavaMail dependency to your project by adding the following to your pom.xml file:
<dependency>
  <groupId>com.sun.mail</groupId>
  <artifactId>javax.mail</artifactId>
  <version>1.6.2</version>
</dependency>
Finally, you will need to replace the vfw://0 media locator with the appropriate media locator for your webcam. You can find the appropriate media locator by running the following command in a Java program:
Vector<MediaLocator> webcams = CaptureDeviceManager.getDeviceList(Format.MJPEG);
for (MediaLocator webcam : webcams) {
    System.out.println(webcam);
}
This will print out a list of available webcams, and you can choose the appropriate one based on the name or index.
