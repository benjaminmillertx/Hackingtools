import java.net.Socket;

public class PortScanner{
public static void
main(String[] args) {
String host="localhost"; //change this to the target host

int startPort= 1;
int endPort= 65535;

System.out.println("Scanning ports on" + host +"...");

for(int port = startPort; port <= endPort;
port++) {

            try{
              Socket socket = new Socket(host,port);
System.out.println("Port" + port + "is open");

socket.close();
            } catch (Exceptione) {
            // Port is closed or reachable
            }
      }  
System.out.println("Scan complete");
 }
}
