package com.example.nabahat.studentapp;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabahat.studentapp.services.CustomIntentServiceJava;
import com.example.nabahat.studentapp.services.FirebaseService;
import com.example.nabahat.studentapp.services.NotificationService;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static android.R.attr.name;

public class StudentHome extends AppCompatActivity {
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    CardView  StartTracking, feedback, profile, Logout, register, viewnotification ;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Button startser;
    NotificationCompat.Builder notification;

    Notification objnot;
    private static final int uniqueID = 45612;

    SharedPreferences pref;
    SharedPreferences.Editor editor;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        StartTracking = (CardView) findViewById(R.id.cardtracking);
        feedback = (CardView) findViewById(R.id.feedback);
        register = (CardView) findViewById(R.id.cardregister);
        Logout = (CardView) findViewById(R.id.cardsignout);
        viewnotification = (CardView) findViewById(R.id.cardviewnotification);
        profile = (CardView) findViewById(R.id.cardprofile);
        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference("Student");

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

//        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putString("TimeStamp", timeStamp);
        editor.putFloat("TimeStampTime",timestamp.getTime());
        editor.commit();
        //  editor = pref.edit();
//       String initialval = pref.getString("TimeStamp", null);
       // editor.putString("TimeStamp", timeStamp);
       // editor.commit();
        Intent serviceIntent = new Intent(StudentHome.this, CustomIntentServiceJava.class);
        startService(serviceIntent);

        Toast.makeText(StudentHome.this, timestamp.toString(), Toast.LENGTH_SHORT).show();

        StartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String Bus = "";
                Intent OpenMap = new Intent(StudentHome.this, StudentMapsActivity.class);
                //String user_Id = mAuth.getCurrentUser().getUid();
                // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    Bus = extras.getString("Bus Number");
                }
                OpenMap.putExtra("Bus Number", Bus);


                //Toast.makeText( StudentHome.this ,Bus.toString(), Toast.LENGTH_SHORT).show();
                startActivity(OpenMap);
                finish();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.getInstance().signOut();
                startActivity(new Intent(StudentHome.this, MainActivity.class));
                finish();
            }
        });


        //  mTimer = new Timer();
        //  mTimer.schedule(timerTask, 2000, 2*1000);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String store= dataSnapshot.child("busnumber").getValue(String.class);

                    FirebaseMessaging.getInstance().subscribeToTopic(store);

}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String store= dataSnapshot.child("busnumber").getValue(String.class);
                            final DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                            mref.child("Driver").orderByChild("busnumber").equalTo(store)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.i("Data Received",dataSnapshot.toString());
                                            if (dataSnapshot.exists()) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                    Log.i("Data", ds.getValue(Driver.class).id);
                                                    Driver driver = ds.getValue(Driver.class);
                                                    String id = driver.id;
                                                    //  Toast.makeText(StudentHome.this, id, Toast.LENGTH_SHORT).show();
                                                    final String name = driver.username;
                                                    final String email = driver.email;
                                                    final String number = driver.phonenumber;
                                                    Intent feedback = new Intent(StudentHome.this, GiveFeedback.class);
                                                    feedback.putExtra("Name",name);
                                                    feedback.putExtra("Email", email);
                                                    feedback.putExtra("Phone Number",number);
                                                    feedback.putExtra("id", id);
                                                    startActivity(feedback);
                                                    finish();
//it will have id stored in it, you can use it further as you like

                                                }
//                                                String DriverID = dataSnapshot.child("id").getValue(String.class);
                                                //Toast.makeText(StudentHome.this, id, Toast.LENGTH_SHORT).show();

//                                            Log.i("Data",dataSnapshot.getValue(Driver.class).email);

//                                            //Log.i("DriverId",dataSnapshot.child("id").getValue(String.class));
//                                                String DriverID = dataSnapshot.getValue(String.class);
//                                                String DriverName = dataSnapshot.child("username").getValue(String.class);
//                                                String DriverEmail = dataSnapshot.child("email").getValue(String.class);
//                                                String DriverBus = dataSnapshot.child("busnumber").getValue(String.class);
//                                                //Log.d("MyApp",DriverBus);
                                                //Toast.makeText(StudentHome.this, DriverID, Toast.LENGTH_SHORT).show()

                                            }
                                            else{Toast.makeText(StudentHome.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//

                Intent viewprof = new Intent(StudentHome.this, ViewProfile.class);
                startActivity(viewprof);
                finish();
            }
        });

        viewnotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewnot = new Intent(StudentHome.this, ViewNotification.class);
                startActivity(viewnot);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewnot = new Intent(StudentHome.this, RegisterationForm.class);
                startActivity(viewnot);
                finish();
            }
        });
        }
    public void startNotificationListener() {
        //start's a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                //fetching notifications from server
                //if there is notifications then call this method

                DatabaseReference dbrefa = FirebaseDatabase.getInstance().getReference("Notification").child("NotificationbyDriver");
                dbrefa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            String jey =  ds.getKey();
                            objnot = ds.getValue(Notification.class);
                            final String busvalue = ds.child("bus").getValue(String.class);
                            final String busmessage = ds.child("message").getValue(String.class);
                            final String bussender = ds.child("sender").getValue(String.class);


                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("Data Received",dataSnapshot.toString());
                                    if (dataSnapshot.exists()) {


                                        String store = dataSnapshot.child("busnumber").getValue(String.class);

                                        if (store.equals(busvalue)){

                                            ShowNotification();
//                                    listiew.setAdapter(adapter);
                                        }



                                    }
                                    else{
                                        //  Toast.makeText(ViewNotification.this, "No Snapshot", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        }).start();
    }
    public void ShowNotification() {
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        //Build the notification
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setTicker("This is the ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Notification");
        notification.setContentText("Hello and Welcome to our App");



        Intent intent = new Intent(this,StudentHome.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        //Builds notification and issues it
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());


    }
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        String newtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
       // editor.putString("TimeStamp", newtimeStamp);
       // editor.commit();
    }

}
