package com.example.azurpushnotification;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import java.util.HashSet;
import java.util.Set;
import android.view.View;


public class MainActivity extends AppCompatActivity {

public static MainActivity mainActivity;
public static Boolean isVisible = false;
private static final String TAG = "MainActivity";
private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
private Notifications notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    mainActivity = this;

    FirebaseService.createChannelAndHandleNotifications(getApplicationContext());
    notifications = new Notifications(this, NotificationSettings.HubName, NotificationSettings.HubListenConnectionString);
    notifications.subscribeToCategories(notifications.retrieveCategories());

    // registerWithNotificationHubs();
    // FirebaseService.createChannelAndHandleNotifications(getApplicationContext());
    }
  

//    @Override
// protected void onStart() {
//     super.onStart();
//     isVisible = true;
// }

@Override
protected void onStart() {

    super.onStart();
    isVisible = true;

    Set<String> categories = notifications.retrieveCategories();

    CheckBox world = (CheckBox) findViewById(R.id.worldBox);
    world.setChecked(categories.contains("world"));
    CheckBox politics = (CheckBox) findViewById(R.id.politicsBox);
    politics.setChecked(categories.contains("politics"));
    CheckBox business = (CheckBox) findViewById(R.id.businessBox);
    business.setChecked(categories.contains("business"));
    CheckBox technology = (CheckBox) findViewById(R.id.technologyBox);
    technology.setChecked(categories.contains("technology"));
    CheckBox science = (CheckBox) findViewById(R.id.scienceBox);
    science.setChecked(categories.contains("science"));
    CheckBox sports = (CheckBox) findViewById(R.id.sportsBox);
    sports.setChecked(categories.contains("sports"));
}


@Override
protected void onPause() {
    super.onPause();
    isVisible = false;
}

@Override
protected void onResume() {
    super.onResume();
    isVisible = true;
}

@Override
protected void onStop() {
    super.onStop();
    isVisible = false;
}

public void ToastNotify(final String notificationMessage) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
            TextView helloText = (TextView) findViewById(R.id.text_hello);
            helloText.setText(notificationMessage);
        }
    });
}


    private boolean checkPlayServices() {
    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
        if (apiAvailability.isUserResolvableError(resultCode)) {
            apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show();
        } else {
            Log.i(TAG, "This device is not supported by Google Play Services.");
            ToastNotify("This device is not supported by Google Play Services.");
            finish();
        }
        return false;
    }
    return true;
}

public void registerWithNotificationHubs()
{
    if (checkPlayServices()) {
        // Start IntentService to register this application with FCM.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}

public void subscribe(View sender) {
    final Set<String> categories = new HashSet<String>();

    CheckBox world = (CheckBox) findViewById(R.id.worldBox);
    if (world.isChecked())
        categories.add("world");
    CheckBox politics = (CheckBox) findViewById(R.id.politicsBox);
    if (politics.isChecked())
        categories.add("politics");
    CheckBox business = (CheckBox) findViewById(R.id.businessBox);
    if (business.isChecked())
        categories.add("business");
    CheckBox technology = (CheckBox) findViewById(R.id.technologyBox);
    if (technology.isChecked())
        categories.add("technology");
    CheckBox science = (CheckBox) findViewById(R.id.scienceBox);
    if (science.isChecked())
        categories.add("science");
    CheckBox sports = (CheckBox) findViewById(R.id.sportsBox);
    if (sports.isChecked())
        categories.add("sports");

    notifications.storeCategoriesAndSubscribe(categories);
}

}
