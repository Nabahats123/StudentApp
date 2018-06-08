package com.example.nabahat.studentapp.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by msnab on 5/4/2018.
 */

public class FirebaseService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Message received fb",remoteMessage.getNotification().getTitle());
    }
}
