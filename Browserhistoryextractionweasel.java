import sqllite3
import winreg
import os
import sys
import opera

def export_firefox_history():
    """
    Export Firefox history to a text file.
    """
    try:
        firefox_path = '~/.mozilla/firefox/'
        firefox_profiles = [d for d in os.listdir(firefox_path) if os.path.isdir(os.path.join(firefox_path, d))]

        for profile in firefox_profiles:
            conn = sqlite3.connect(os.path.join(firefox_path, profile, 'places.sqlite'))
            c = conn.cursor()
            c.execute("SELECT url FROM moz_places WHERE visit_count > 0")
            history = c.fetchall()

            with open("browser_history.txt", "a") as f:
                for url in history:
                    f.write(f"{url[0]}\n")

    except sqlite3.Error:
        print("Firefox not found.")

    finally:
        if conn:
            conn.close()

def export_ie_history():
    """
    Export Internet Explorer history to a text file (Windows only).
    """
    try:
        if sys.platform == 'win32':
            key = winreg.OpenKey(winreg.HKEY_CURRENT_USER, r"Software\Microsoft\Internet Explorer\TypedURLs")
            i = 0

            while True:
                try:
                    url, _ = winreg.EnumValue(key, i)
                    with open("browser_history.txt", "a") as f:
                        f.write(f"{url}\n")
                    i += 1
                except WindowsError:
                    break
        else:
            print("Internet Explorer not found on this platform.")

    except WindowsError:
        print("Internet Explorer not found.")

def export_safari_history():
    """
    Export Safari history to a text file (macOS only).
    """
    try:
        if sys.platform == 'darwin':
            conn = sqlite3.connect('~/Library/Safari/history.db')
            c = conn.cursor()
            c.execute("SELECT url FROM urls WHERE visit_count > 0")
            history = c.fetchall()

            with open("browser_history.txt", "a") as f:
                for url in history:
                    f.write(f"{url[0]}\n")

        else:
            print("Safari not found on this platform.")

    except sqlite3.Error:
        print("Safari not found.")

    finally:
        if conn:
            conn.close()

def export_opera_history():
    """
    Export Opera history to a text file.
    """
    try:
        if sys.platform.startswith('linux'):
            opera_config_dir = os.path.expanduser('~/.config/opera')
        elif sys.platform == 'darwin':
            opera_config_dir = os.path.expanduser('~/Library/Application Support/com.operasoftware.Opera')
        elif sys.platform == 'win32':
            opera_config_dir = os.path.join(os.environ['APPDATA'], r'Opera Software\Opera Stable')
        else:
            print("Opera not found on this platform.")
            return

        profile = opera.find_operaprofile(opera_config_dir)
        history = opera.History(profile)
        urls = [item.url for item in history.get_history()]

        with open("browser_history.txt", "a") as f:
            for url in urls:
                f.write(f"{url}\n")

    except opera.OperaError:
        print("Opera not found.")

def main():
    """
    Export history data from all supported browsers to a text file.
    """
    print("Browser History Extractor\n")
    export_firefox_history()
    export_ie_history()
    export_safari_history()
    export_opera_history()

if __name__ == "__main__":
    main()
This script should work on Windows, Linux, and macOS. It checks the platform and modifies the paths accordingly. If a browser is not found on a specific platform, it will print "Browser not found" in the corresponding section.
Please make sure you have the necessary libraries installed, such as sqlite3, opera, and winreg (Windows only). For opera, you can install it using pip install opera. The winreg library is available in Windows systems by default.
