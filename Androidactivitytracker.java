 more_vert To track the usage of installed apps on an Android device, you can create an Android Service that logs the activity of each installed app. Here's a simplified example to get you started.
Create a new Android project in Android Studio.
Update the AndroidManifest.xml to declare the Service:
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.apptracker">

    <!-- Declare the AppTrackerService -->
    <service
        android:name=".AppTrackerService"
        android:enabled="true" />

</manifest>
Create a new Java class named AppTrackerService that extends Service:
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AppTrackerService extends Service {

    private static final String TAG = "AppTrackerService";

    public AppTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AppTrackerService started");

        // Start monitoring app usage
        AppUsageMonitor.startMonitoring(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AppTrackerService stopped");

        // Stop monitoring app usage
        AppUsageMonitor.stopMonitoring();
    }
}
Create a new Java class named AppUsageMonitor:
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AppUsageMonitor {

    private static final String TAG = "AppUsageMonitor";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static void startMonitoring(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        // Get the usage stats manager
        if (usageStatsManager != null) {
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 60 * 5, time);

            // Sort the stats by the last time used
            if (stats != null) {
                sortByLastTimeUsed(stats);

                // Log the app usage
                for (UsageStats usageStats : stats) {
                    Log.d(TAG, "App: " + usageStats.getPackageName() +
                            " - Last time used: " + sdf.format(new Date(usageStats.getLastTimeUsed())) +
                            " - Usage count: " + usageStats.getUsageCount());
                }
            }
        }
    }

    public static void stopMonitoring() {
        // Nothing to do
    }

    private static void sortByLastTimeUsed(List<UsageStats> stats) {
        for (int i = 0; i < stats.size() - 1; i++) {
            for (int j = 0; j < stats.size() - 1 - i; j++) {
                if (stats.get(j).getLastTimeUsed() < stats.get(j + 1).getLastTimeUsed()) {
                    UsageStats temp = stats.get(j);
                    stats.set(j, stats.get(j + 1));
                    stats.set(j + 1, temp);
                }
            }
        }
    }
}
Register the service in the MainActivity:
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the AppTrackerService
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), AppTrackerService.public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start the AppTrackerService
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), AppTrackerService.class));
            }
        });

        // Stop the AppTrackerService
        Button stopButton = findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), AppTrackerService.class));
            }
        });
    }
}
In this example, we have two buttons in the activity layout: start_button and stop_button. When the user clicks on the start_button, the AppTrackerService is started using the startService() method. Similarly, when the user clicks on the stop_button, the AppTrackerService is stopped using the stopService() method.
The AppTrackerService uses the AppUsageMonitor class to query the system for usage statistics about installed apps. The startMonitoring() method sorts the stats by the last time used and logs the app usage. The stopMonitoring() method is empty since it does not require any action to be performed.
Please note that tracking the usage of installed apps may require the user's consent and should be done in accordance with the Google Play Store developer policies and local laws and regulations.



        // Stop the AppTrackerService
        Button stopButton = findViewById(R.
