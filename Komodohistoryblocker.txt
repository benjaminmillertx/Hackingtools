Make sure to credit Benjamin Hunter Miller. Here's the complete implementation for a browser history blocker for Firefox, Safari, Internet Explorer, and Opera using their corresponding extension mechanisms. Please note that these are simplified versions of the extensions, and you may need to add additional features to meet your specific requirements.
Firefox
Create a new directory, then inside the directory create a file named manifest.json and a folder named lib.
manifest.json:
{
  "manifest_version": 2,
  "name": "Browser History Blocker",
  "version": "1.0",
  "description": "Block browser history for specific websites",
  "permissions": [
    "webRequest",
    "webRequestBlocking",
    "*://*/*"
  ],
  "background": {
    "scripts": ["lib/background.js"]
  }
}
lib/background.js:
const blockedUrls = [
  "http://example.com/path",
  "https://example.org/path"
];

function blockRequest(details) {
  for (const url of blockedUrls) {
    if (details.url.includes(url)) {
      return { cancel: true };
    }
  }
  return { cancel: false };
}

browser.webRequest.onBeforeRequest.addListener(
  blockRequest,
  { urls: ["<all_urls>"] }
);
person more_vert Finish the code
 Contact Photo more_vert
Safari
Create a new Safari App Extension using Swift and the Safari App Extension API. Implement the WebExtensionProvider protocol and use the webRequest API to intercept requests and block those that are related to browser history.
Example code snippet:
SafariBrowserHistoryBlocker.entitlements:
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>com.apple.developer.safari-extension</key>
    <dict>
        <key>Allowed Web Origins</key>
        <array>
            <string>*</string>
        </array>
    </dict>
</dict>
</plist>
SafariBrowserHistoryBlocker/SafariBrowserHistoryBlocker.swift:
import SafariServices
import WebKit

class SafariBrowserHistoryBlocker: NSObject, NSExtensionRequestHandling {
    var blockedUrls = [
        "http://example.com/path",
        "https://example.org/path"
    ]

    func beginRequest(with context: NSExtensionContext) {
        let item = SFSafariWebExtensionBrowserCommandItem(webView: nil, command: "blockRequest", from: nil, for: nil)
        context.completeRequest(returningItems: [item], completionHandler: nil)
    }
}

extension SafariBrowserHistoryBlocker: WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if message.name == "blockRequest" {
            let details = message.body as! [String: Any]
            for url in blockedUrls where (details["url"] as! String).contains(url) {
                NotificationCenter.default.post(name: NSNotification.Name(rawValue: "webViewBlockRequest"), object: nil, userInfo: ["url": url])
                return
            }
        }
    }
}
Internet Explorer
Create a new Internet Explorer Browser Helper Object (BHO) using C++ and the Internet Explorer COM Interfaces. Implement the IObjectWithSite and IDispatch interfaces to intercept requests and block those that are related to browser history.
Example code snippet:
BrowserHistoryBlocker.h:
#pragma once

#include <Exdispid.h>
#include <atlbase.h>
#include <atlcom.h>
#include <Exdispid.h>

class ATL_NO_VTABLE CBrowserHistoryBlocker :
    public CComObjectRootEx<CComSingleThreadModel>,
    public CComCoClass<CBrowserHistoryBlocker, &CLSID_BrowserHistoryBlocker>,
    public IObjectWithSiteImpl<CBrowserHistoryBlocker>,
    public IDispatchImpl<IBrowserHistoryBlocker, &IID_IBrowserHistoryBlocker, &LIBID_BrowserHistoryBlockerLib, /*wMajorVerNum*/ 1, /*wMinorVerNum*/ 0>
{
public:
    CBrowserHistoryBlocker();

    DECLARE_REGISTRY_RESOURCEID(IDR_BROWSERHISTORYBLOCKER)

    BEGIN_COM_MAP(CBrowserHistoryBlocker)
        COM_INTERFACE_ENTRY(IObjectWithSite)
        COM_INTERFACE_ENTRY(IDispatch)
        COM_INTERFACE_ENTRY(IBrowserHistoryBlocker)
    END_COM_MAP()

    DECLARE_PROTECT_FINAL_CONSTRUCT()

    HRESULT FinalConstruct() override;
    void FinalRelease() override;

    // IObjectWithSite
    HRESULT STDMETHODCALLTYPE SetSite(IUnknown* punkSite) override;
    HRESULT STDMETHODCALLTYPE GetSite(REFIID riid, void** ppvSite) override;

    // IBrowserHistoryBlocker
    HRESULT STDMETHODCALLTYPE BlockRequest(BSTR url) override;

private:
    CComPtr<IWebBrowser2> m_spWebBrowser;
    CComBSTR m_blockedUrls[2];
};

OBJECT_ENTRY_AUTO(__uuidof(BrowserHistoryBlocker), CBrowserHistoryBlocker)
BrowserHistoryBlocker.cpp:
#include "stdafx.h"
#include "BrowserHistoryBlocker.h"
#include <Exdispid.h>

CBrowserHistoryBlocker::CBrowserHistoryBlocker()
{
    m_blockedUrls[0] = L"http://example.com/path";
    m_blockedUrls[1] = L"https://example.org/path";
}

HRESULT CBrowserHistoryBlocker::FinalConstruct()
{
    return S_OK;
}

void CBrowserHistoryBlocker::FinalRelease()
{
}



Internet Explorer (continued)
BrowserHistoryBlocker.cpp:
HRESULT CBrowserHistoryBlocker::SetSite(IUnknown* punkSite)
{
    HRESULT hr = IObjectWithSiteImpl<CBrowserHistoryBlocker>::SetSite(punkSite);

    if (SUCCEEDED(hr))
    {
        // QI for the IWebBrowser2 interface
        hr = punkSite->QueryInterface(IID_PPV_ARGS(&m_spWebBrowser));
    }

    return hr;
}

HRESULT CBrowserHistoryBlocker::GetSite(REFIID riid, void** ppvSite)
{
    return IObjectWithSiteImpl<CBrowserHistoryBlocker>::GetSite(riid, ppvSite);
}

HRESULT CBrowserHistoryBlocker::BlockRequest(BSTR url)
{
    for (const CComBSTR& blockedUrl : m_blockedUrls)
    {
        if (wcsstr(url, blockedUrl) != nullptr)
        {
            // Block the request by returning E_FAIL
            return E_FAIL;
        }
    }

    // Allow the request if no blocked URL matched
    return S_OK;
}
Opera
Create a new Opera Extension using JavaScript and the Opera Extension API. Implement a content script that injects a <script> tag into the page DOM, which intercepts requests and blocks those that are related to browser history.
manifest.json:
{
  "manifest": "1.0",
  "name": "Browser History Blocker",
  "version": "1.0",
  "description": "Block browser history for specific websites",
  "icons": {
    "16": "icon16.png",
    "32": "icon32.png",
    "48": "icon48.png"
  },
  "content_scripts": [
    {
      "matches": ["<all_urls>"],
      "js": ["lib/content.js"]
    }
  ]
}
lib/content.js:
const blockedUrls = [
  "http://example.com/path",
  "https://example.org/path"
];

function blockRequest(details) {
  for (const url of blockedUrls) {
    if (details.url.includes(url)) {
      return { cancel: true };
    }
  }
  return { cancel: false };
}

// Intercept XMLHttpRequest
const originalOpen = XMLHttpRequest.prototype.open;
XMLHttpRequest.prototype.open = function(method, url, async, user, password) {
  if (!async) {
    const result = blockRequest(url);
    if (result.cancel) {
      return;
    }
  }
  originalOpen.apply(this, arguments);
};

// Intercept fetch API
const originalFetch = window.fetch;
window.fetch = function(input, init) {
  if (init && init.method && init.method.toLowerCase() === 'get') {
    const result = blockRequest(input);
    if (result.cancel) {
      return Promise.reject(new Error('Request blocked'));
    }
  }
  return originalFetch.apply(this, arguments);
};
These are basic implementations of browser history blockers for Firefox, Safari, Internet Explorer, and Opera. You may need to customize these examples to meet your specific requirements. Please note that these code snippets are for educational purposes only and should not be used for malicious purposes.
