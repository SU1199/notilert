package com.sortsvane.notilert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnEnableNotificationListener = findViewById(R.id.btnEnableNotificationListener);
        btnEnableNotificationListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNotificationServiceEnabled()) {
                    startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                } else {
                    Toast.makeText(MainActivity.this, "Notification Listener is already enabled", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnRestartService = findViewById(R.id.btnRestartService);
        btnRestartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartNotificationListenerService();
            }
        });

    }

    private void restartNotificationListenerService() {
        if (isNotificationServiceEnabled()) {
            Toast.makeText(this, "Please disable and re-enable the Notification Access for the app.", Toast.LENGTH_LONG).show();
            // Redirect user to the notification access settings
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
        } else {
            Toast.makeText(this, "Notification Listener is not enabled yet.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNotificationServiceEnabled() {
        String enabledListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        String packageName = getPackageName();

        // Check if the notification listener service is enabled
        return enabledListeners != null && enabledListeners.contains(packageName);
    }


}
