Creating a tool on Kali Linux that captures a 20-second video from the webcam when an incorrect password is entered and then emails it to you involves multiple steps. This can be broken down into these main parts:

	1.	Capturing the Incorrect Password Attempt: This involves monitoring login attempts and detecting when an incorrect password is entered.
	2.	Recording a Video: Using the webcam to capture a 20-second video.
	3.	Emailing the Video: Sending the captured video to a specified email address.

For this example, we’ll use Python and a few libraries to accomplish these tasks.

Step 1: Capturing Incorrect Password Attempt

To capture incorrect password attempts, you can monitor the system authentication logs. On many Linux systems, login attempts are logged in /var/log/auth.log or /var/log/secure.

Step 2: Recording a Video

To capture video from the webcam, you can use the opencv-python library.

Step 3: Emailing the Video

To send emails, you can use the smtplib library in Python.

Here’s a step-by-step guide to create this tool:

Prerequisites

	1.	Install necessary libraries:

sudo apt update
sudo apt install python3 python3-pip
pip3 install opencv-python
pip3 install smtplib
pip3 install email



Script to Monitor Log, Capture Video, and Send Email

Create a Python script (e.g., security_monitor.py):

import cv2
import time
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email import encoders
import os
import subprocess
from datetime import datetime, timedelta

# Email details
EMAIL_ADDRESS = "your_email@example.com"
EMAIL_PASSWORD = "your_email_password"
RECIPIENT_EMAIL = "recipient_email@example.com"

def send_email(filename):
    msg = MIMEMultipart()
    msg['From'] = EMAIL_ADDRESS
    msg['To'] = RECIPIENT_EMAIL
    msg['Subject'] = "Security Alert: Incorrect Password Attempt"
    
    attachment = MIMEBase('application', 'octet-stream')
    attachment.set_payload(open(filename, "rb").read())
    encoders.encode_base64(attachment)
    attachment.add_header('Content-Disposition', 'attachment; filename="%s"' % os.path.basename(filename))
    msg.attach(attachment)
    
    server = smtplib.SMTP('smtp.gmail.com', 587)
    server.starttls()
    server.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
    server.sendmail(EMAIL_ADDRESS, RECIPIENT_EMAIL, msg.as_string())
    server.quit()

def record_video():
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("Cannot open camera")
        return
    
    fourcc = cv2.VideoWriter_fourcc(*'XVID')
    out = cv2.VideoWriter('intruder.avi', fourcc, 20.0, (640, 480))

    start_time = datetime.now()
    while (datetime.now() - start_time).seconds < 20:
        ret, frame = cap.read()
        if not ret:
            print("Can't receive frame (stream end?). Exiting ...")
            break
        out.write(frame)
    
    cap.release()
    out.release()
    cv2.destroyAllWindows()

def monitor_log():
    log_file = '/var/log/auth.log'
    p = subprocess.Popen(['tail', '-F', log_file], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    
    while True:
        line = p.stdout.readline().decode('utf-8')
        if "Failed password" in line:
            print("Incorrect password attempt detected")
            record_video()
            send_email('intruder.avi')

if __name__ == "__main__":
    monitor_log()

Important Considerations

	1.	Permissions: This script needs to read system log files which require root permissions. Run the script with sudo.
	2.	Security: Storing email credentials in the script is not secure. Consider using environment variables or a more secure method to handle credentials.
	3.	Dependencies: Ensure all required Python packages are installed.

Running the Script

Save the script as security_monitor.py and run it with:

sudo python3 security_monitor.py

This script will:

	•	Monitor the /var/log/auth.log for any “Failed password” entries.
	•	Capture a 20-second video from the webcam if such an entry is detected.
	•	Email the captured video to a specified email address.

This is a basic implementation and can be expanded with additional features and error handling as required.
