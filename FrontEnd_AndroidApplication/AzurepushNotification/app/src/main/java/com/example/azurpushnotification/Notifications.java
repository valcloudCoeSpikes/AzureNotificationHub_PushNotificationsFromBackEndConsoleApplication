package com.example.azurpushnotification;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.microsoft.windowsazure.messaging.NotificationHub;

public class Notifications {
    private static final String PREFS_NAME = "BreakingNewsCategories";
    private FirebaseInstanceId fcm;
    private NotificationHub hub;
    private Context context;
    private String senderId;
    public static String FCM_token = "";
    private static final String TAG = "Notifications";

    public Notifications(Context context, String hubName, String listenConnectionString) {
        this.context = context;
        this.senderId = senderId;

        fcm = FirebaseInstanceId.getInstance();
        hub = new NotificationHub(hubName, listenConnectionString, context);
    }

    public void storeCategoriesAndSubscribe(Set<String> categories)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putStringSet("categories", categories).commit();
        subscribeToCategories(categories);
    }

    public Set<String> retrieveCategories() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getStringSet("categories", new HashSet<String>());
    }

    public void subscribeToCategories(final Set<String> categories) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                        @Override
                        public void onSuccess(InstanceIdResult instanceIdResult) {
                            FCM_token = instanceIdResult.getToken();
                            Log.d(TAG, "FCM Registration Token: " + FCM_token);
                        }
                    });

                    TimeUnit.SECONDS.sleep(1);

                    String templateBodyFCM = "{\"data\":{\"message\":\"$(messageParam)\"}}";

                    hub.registerTemplate(FCM_token,"simpleFCMTemplate", templateBodyFCM,
                            categories.toArray(new String[categories.size()]));
                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to register - " + e.getMessage());
                    return e;
                }
                return null;
            }

            protected void onPostExecute(Object result) {
                String message = "Subscribed for categories: "
                        + categories.toString();
                Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

}