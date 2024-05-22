Make sure to credit Benjamin Hunter Miller.To create an iOS app that tracks the usage of installed apps on an iPhone, you can use the `Codable` protocol and Swift's `Date` and `Calendar` classes to store and manage usage data. Here's a simple example to get you started.
1. Create a new iOS app project in Xcode.
2. Update your `Info.plist` file to request `NSUserTrackingUsageDescription` permission:

```xml
<key>NSUserTrackingUsageDescription</key>
<string>This app tracks your app usage for statistics</string>
```

3. Create a new Swift class named `AppUsage` that conforms to the `Codable` protocol:

```swift
import Foundation

struct AppUsage: Codable {
    let name: String
    let lastUsed: Date

    public init(name: String, lastUsed: Date) {
        self.name = name
        self.lastUsed = lastUsed
    }
}
```

4. Create a new class named `AppUsageManager` that handles the app usage tracking:

```swift
import Foundation

class AppUsageManager {

    private let usageFileName = "app_usages.json"
    private let calendar = Calendar.current

    func saveAppUsage(appUsages: [AppUsage]) {
        let encoder = JSONEncoder()
        if let encodedData = try? encoder.encode(appUsages) {
            if let fileHandle = FileHandle(forWritingAtPath: usageFileName) {
                defer { fileHandle.closeFile() }
                fileHandle.truncateFile(atOffset: 0)
                fileHandle.write(encodedData)
            } else {
                do {
                    try encodedData.write(to: URL(fileURLWithPath: usageFileName))
                } catch {
                    print("Error saving app usage: \(error)")
                }
            }
        } else {
            print("Error encoding app usage:")
        }
    }

    func loadAppUsage() -> [AppUsage] {
        if let fileHandle = FileHandle(forReadingAtPath: usageFileName) {
            if let data = fileHandle.readDataToEndOfFile() {
                if let decoder = JSONDecoder() {
                    if let appUsages = try? decoder.decode([AppUsage].self, from: data) {
                        return appUsages
                    } else {
                        print("Error decoding app usage.")
                    }
                } else {
                    print("Error creating JSON decoder.")
                }
            } else {
                print("Error reading app usage data.")
            }
        } else {
            print("Error opening app usage file.")
        }
        return []
    }

    func updateAppUsage(appName: String) {
        let appUsages = loadAppUsage()
        let currentDate = Date()
        let appUsage = AppUsage(name: appName, lastUsed: currentDate)

        if let index = appUsages.firstIndex(where: { $0.name == appName }) {
            appUsages[index] = appUsage
        } else {
            appUsages.append(appUsage)
        }

        saveAppUsage(appUsages: appUsages)
    }
}
```

5. Use the `AppUsageManager` class in your view controller to track app usage:

```swift
import UIKit

class ViewController: UIViewController {

    private let appUsageManager = AppUsageManager()

    override func viewDidLoad() {
        super.viewDidLoad()

        // Track app usage for the current app
        appUsageManager.updateAppUsage(appName: Bundle.main.bundleIdentifier!)
    }
}
```

This example shows how to store app usage data in a JSON file on the device and how to update the app usage for the current app. To track usage for other installed apps, you can use the `UIApplication` class to retrieve the list of running apps and then use the `AppUsageManager` class to update their usage.

Please note that tracking the usage of installed apps may require the user's consent and should be done in accordance with the App Store developer guidelines and local laws and regulations.
