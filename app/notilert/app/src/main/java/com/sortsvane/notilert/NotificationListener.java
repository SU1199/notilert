package com.sortsvane.notilert;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Service Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service Destroyed");
    }

    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        CharSequence notificationText = sbn.getNotification().tickerText;

        executorService.execute(() -> {
            try {
                sendNotificationToServer(packageName, notificationText != null ? notificationText.toString() : "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendNotificationToServer(String title, String message) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "title=" + title + "&message=" + message + "&url=https://notilert.pages.dev");
        Request request = new Request.Builder()
                .url("https://api.pushalert.co/rest/v1/send")
                .post(body)
                .addHeader("Authorization", "api_key=get your own lole")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // Handle the response or log it
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.d(TAG, "Notification Removed: " + sbn.getPackageName());
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "Listener Connected");
        Toast.makeText(this, "Listener Connected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.d(TAG, "Listener Disconnected");
        Toast.makeText(this, "Listener Disconnected", Toast.LENGTH_SHORT).show();
    }
}
