Make sure to credit Benjamin Hunter Miller. Here's the complete code for creating a Windows firewall rule in Java using the JNA library and the Windows Firewall API:
Download and install the JNA library from the official website: <https://github.com/java-native-access/jna>
Create a new Java project and add the JNA library to your project's classpath.
Add the following code to your project:
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;

public class WindowsFirewall {

    public static void main(String[] args) {
        // Set the firewall rule properties
        String name = "My Firewall Rule";
        String description = "Allows incoming traffic on port 80";
        String program = "C:\\Program Files\\My Program\\myprogram.exe";
        int port = 80;
        String protocol = "TCP";

        // Create the INetFwRule object
        INetFwRule firewallRule = new INetFwRule();
        firewallRule.setName(name);
        firewallRule.setDescription(description);
        firewallRule.setApplicationName(new WString(program));
        firewallRule.setLocalPort(port);
        firewallRule.setProtocol(protocol);
        firewallRule.setDirection(INetFwRule.DIR_INBOUND);
        firewallRule.setAction(INetFwRule.ACTION_ALLOW);

        // Add the firewall rule
        INetFwPolicy2 firewallPolicy = INetFwPolicy2.getLocalPolicy();
        firewallPolicy.getRules().add(firewallRule);
    }

    // Define the INetFwRule and INetFwPolicy2 interfaces
    public interface INetFwRule extends WinNT.ComObject {

        final int DIR_INBOUND = 0;
        final int DIR_OUTBOUND = 1;
        final int ACTION_ALLOW = 1;
        final int ACTION_BLOCK = 2;
        final int ACTION_CUSTOM = 3;

        void setName(String name);

        void setDescription(String description);

        void setApplicationName(WString applicationName);

        void setLocalPort(int port);

        void setRemotePort(int port);

        void setRemoteAddress(String address);

        void setProtocol(String protocol);

        void setDirection(int direction);

        void setAction(int action);

        void setEnabled(boolean enabled);

    }

    public interface INetFwPolicy2 extends WinNT.ComObject {

        INetFwRules getRules();

        static INetFwPolicy2 getLocalPolicy() {
            INetFwPolicy2 firewallPolicy = null;
            Pointer hResult = Native.invokeLong(
                "HNetcfg.FwPolicy2",
                "GetInstance",
                new Object[]{},
                (Class[]) null,
                Native.POINTER_SIZE);
            if (hResult.getPointer().getPointer() != null) {
                firewallPolicy = (INetFwPolicy2) Native.loadLibrary("HNetcfg", INetFwPolicy2.class);
                firewallPolicy.setHandle(hResult);
            }
            return firewallPolicy;
        }

    }

    public interface INetFwRules extends WinNT.ComObject {

        void add(INetFwRule rule);

    }

}
This code creates a simple firewall rule that allows incoming traffic on port 80 for a specific program. You can modify the code to suit your needs by changing the firewall rule properties, such as the name, description, program, port, and protocol.
Note that creating and modifying firewall rules requires administrative privileges, so make sure to run the Java code with sufficient permissions. Additionally, note that using Java for system-level programming can introduce security risks and instability, so make sure to follow best practices for securing your Java code and preventing unauthorized access.
