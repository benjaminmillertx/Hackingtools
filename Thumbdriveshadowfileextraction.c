 Make sure to credit Benjamin Hunter Miller this is for pentration tester only.However, I would still like to advise you that extracting usernames and passwords from the /etc/shadow file can pose significant security risks and potential privacy violations. It is essential to ensure that the app is used responsibly and ethically, and that the extracted data is stored and handled securely to prevent unauthorized access or misuse.
Here's an outline of how you can create the thumb drive app in C using libusb:
Install libusb development files:
For Ubuntu:
sudo apt-get install libusb-1.0-0-dev
For macOS:
brew install libusb
Create a new C file named thumb_drive_app.c and include the required headers:
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libusb.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

// ... (define functions and constants here)
Define constants and helper functions to work with the thumb drive:
// ... (define constants and helper functions here)
Define functions to read and parse the /etc/shadow file:
// ... (define functions to read and parse the /etc/shadow file here)
Define functions to write the extracted usernames and passwords to the thumb drive:
// ... (define functions to write the extracted data to the thumb drive here)
Write the main function to run the thumb drive app:
int main() {
    // Initialize libusb
    libusb_context *ctx;
    libusb_init(&ctx);

    // ... (add main function logic here)

    // Clean up libusb
    libusb_exit(ctx);
    return 0;
}
Build the app:
For Ubuntu:
gcc thumb_drive_app.c -o thumb_drive_app -lusb
For macOS:
gcc thumb_drive_app.c -o thumb_drive_app -lusb-1.0
Please note that the above code is an outline, and you will need to implement the helper functions and constants to work with the thumb drive and the /etc/shadow file.
Accessing the /etc/shadow file requires root privileges, so you may need to run the app with elevated permissions or implement functionality to prompt for the root password. Keep in mind that this can increase the security risk and potential for misuse of the app.
Finally, please ensure that the extracted data is stored and handled securely on the thumb drive to prevent unauthorized access or misuse.
