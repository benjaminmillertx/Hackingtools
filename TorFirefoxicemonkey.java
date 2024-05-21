//make sure to credit Benjamin Hunter Miller
 Creating a Firefox extension that connects to Tor involves several steps, including creating the extension's manifest, background scripts, and user interface elements. Here's a step-by-step guide to creating a basic Firefox extension that connects to Tor:
Create the Firefox extension's manifest file (manifest.json)
Create a new file named "manifest.json" in a new directory for your extension. Add the following content to it:
{
  "manifest_version": 2,
  "name": "Tor Privacy Extension",
  "version": "1.0",
  "description": "Connects to Tor network for increased privacy.",
  "background": {
    "scripts": ["background.js"]
  },
  "permissions": [
    "webRequest",
    "webRequestBlocking",
    "webNavigate",
    "<all_urls>",
    "proxy"
  ]
}
Create the background script (background.js)
Create a new file named "background.js" in the same directory as your manifest.json file. Add the following content to it:
// Based on https://github.com/WillSC/webext-tor-proxy

const TOR_PROXY_HOST = '127.0.0.1';
const TOR_PROXY_PORT = 9050;

function updateProxy(details) {
  const config = {
    mode: "pac_script",
    pacScript: {
      data: createProxyPac(),
    },
  };

  return { config };
}

function createProxyPac() {
  return `
    function FindProxyForURL(url, host) {
      if (shExpMatch(host, '*.onion')) {
        return 'SOCKS5 127.0.0.1:9050';
      } else {
        return 'DIRECT';
      }
    }
  `;
}

browser.webRequest.onBeforeRequest.addListener(
  updateProxy,
  { urls: ["<all_urls>"] },
  ["blocking"]
);
This script sets the SOCKS5 proxy for onion websites to connect through the Tor network. Other connections will be made directly without the Tor network.
Build and test the extension
Open a new browser window in Firefox Developer Edition and open the Add-ons Manager (about:debugging#/runtime/this-firefox). Click "Load Temporary Add-on..." and browse to the directory where you saved your extension files. Select the "manifest.json" file, and the extension should load.
Please note that connecting to the Tor network requires the Tor browser or running the Tor daemon on the machine. The Tor daemon should already be running if you are using the Tor browser. Otherwise, download and install the Tor browser from <https://www.torproject.org/download/> and ensure the Tor daemon is running.
Remember that this is a basic extension, and you might want to add features such as user interface elements, options pages, or a more sophisticated proxy configuration.
