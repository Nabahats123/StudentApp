package com.example.nabahat.studentapp.services;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nabahat.studentapp.Driver;
import com.example.nabahat.studentapp.MainActivity;
import com.example.nabahat.studentapp.Notification;
import com.example.nabahat.studentapp.R;
import com.example.nabahat.studentapp.Student;
import com.example.nabahat.studentapp.StudentHome;
import com.example.nabahat.studentapp.ViewNotification;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.firebase.client.Firebase;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class NotificationService extends Service {

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(getApplicationContext(),"Service started",Toast.LENGTH_LONG).show();

        return START_STICKY;

    }

    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");
        initializeLocationManager();

   }


    @Override
    public void onDestroy()
    {/*to stop the service and destroy it*/
        Log.e(TAG, "onDestroy");
        super.onDestroy();

//moving to activty , broadcasting values , no more updates because service destroyed

    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");

    }

}


