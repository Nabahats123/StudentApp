package com.example.nabahat.studentapp.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.nabahat.studentapp.R;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CustomIntentServiceJava extends IntentService {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    public CustomIntentServiceJava() {

        super("ServiceName");

        setIntentRedelivery(true);
    }

com.example.nabahat.studentapp.Notification notificationstu, notificationadm;
    private Handler handler;
    Context ctx;
    SharedPreferences.Editor editor;
    FirebaseDatabase fb;
    DatabaseReference ref;
    DatabaseReference mDatabase;
    NotificationManager notif;
    SharedPreferences pref;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        ctx = getApplicationContext();
        ref = FirebaseDatabase.getInstance().getReference("Student");
        notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        pref = ctx.getSharedPreferences("MyPref", 0);
        // 0 - for private mode
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        handler.post(new Runnable() {
            @Override
            public void run() {

                FirebaseApp.initializeApp(ctx);
                mDatabase = FirebaseDatabase.getInstance().getReference("Student");


                DatabaseReference fbRefStudentNotify = FirebaseDatabase.getInstance().getReference("Notification")
                        .child("AllStudents");
                fbRefStudentNotify.addValueEventListener(new ValueEventListener() {


//                    final String mobiledate = pref.getString("TimeStamp", null);
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String content = "";
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                notificationadm = ds.getValue(com.example.nabahat.studentapp.Notification.class);
                                final String busmessage = ds.child("notification").getValue(String.class);
                                final String timestampdatech = ds.child("date").getValue(String.class);
//                                final Date dbTimeStampDate = ds.child("date").getValue(Date.class);
                                final String mobiledate = pref.getString("TimeStamp", null);

                                try {
                                    Date prefDate = simpleDateFormat.parse(mobiledate);
                                    Date dbDate = simpleDateFormat.parse(timestampdatech);

                                    int val = dbDate.compareTo(prefDate);
                                    if (val<0) {

                                        Notification notify = new Notification.Builder
                                                (ctx).setContentText(busmessage).
                                                setContentTitle("Admin").setSmallIcon(R.drawable.ic_directions_bus_black_24dp).setColor(Color.BLUE).build();
                                        Random random = new Random();
                                        int m = random.nextInt(9999 - 1000) + 1000;
                                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                        notify.defaults |= Notification.DEFAULT_SOUND;
                                        notif.notify(m, notify);
                                        editor = pref.edit();
                                        editor.remove("TimeStamp");
                                        String newtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                                        editor.putString("TimeStamp", newtimeStamp);
                                        editor.commit();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                final long storedTimeStanp = pref.getLong("Timestamp",0);
//                                final Timestamp timestamp = new Timestamp(storedTimeStanp);
//                                final Timestamp dbTimeStamp = new Timestamp(dbTimeStampDate.getTime());

//                                Log.i("DateTag",timestamp.toString());
//                                Log.i("DaateTag",dbTimeStamp.toString());
                               // String test = Integer.toString(val);
                               // Toast.makeText(ctx, test, Toast.LENGTH_SHORT).show();




                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                DatabaseReference fbRefStudentNotify2 = FirebaseDatabase.getInstance().getReference("Notification")
                        .child("Auto");
                fbRefStudentNotify2.addValueEventListener(new ValueEventListener() {


                    //                    final String mobiledate = pref.getString("TimeStamp", null);
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String content = "";
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                notificationadm = ds.getValue(com.example.nabahat.studentapp.Notification.class);
                                final String busmessage = ds.child("notification").getValue(String.class);
                                final String timestampdatech = ds.child("date").getValue(String.class);
//                                final Date dbTimeStampDate = ds.child("date").getValue(Date.class);
                                final String mobiledate = pref.getString("TimeStamp", null);

                                try {
                                    Date prefDate = simpleDateFormat.parse(mobiledate);
                                    Date dbDate = simpleDateFormat.parse(timestampdatech);

                                    int val = dbDate.compareTo(prefDate);
                                    if (val<0) {

                                        Notification notify = new Notification.Builder
                                                (ctx).setContentText(busmessage).
                                                setContentTitle("Weather").setSmallIcon(R.drawable.ic_directions_bus_black_24dp).setColor(Color.BLUE).build();
                                        Random random = new Random();
                                        int m = random.nextInt(9999 - 1000) + 1000;
                                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                        notify.defaults |= Notification.DEFAULT_SOUND;
                                        notif.notify(m, notify);
                                        editor = pref.edit();
                                        editor.remove("TimeStamp");
                                        String newtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                                        editor.putString("TimeStamp", newtimeStamp);
                                        editor.commit();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                final long storedTimeStanp = pref.getLong("Timestamp",0);
//                                final Timestamp timestamp = new Timestamp(storedTimeStanp);
//                                final Timestamp dbTimeStamp = new Timestamp(dbTimeStampDate.getTime());

//                                Log.i("DateTag",timestamp.toString());
//                                Log.i("DaateTag",dbTimeStamp.toString());
                                // String test = Integer.toString(val);
                                // Toast.makeText(ctx, test, Toast.LENGTH_SHORT).show();




                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                DatabaseReference dbrefa = FirebaseDatabase.getInstance().getReference("Notification").child("NotificationbyDriver");
                dbrefa.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            notificationstu = ds.getValue(com.example.nabahat.studentapp.Notification.class);
                            final String busvalue = ds.child("bus").getValue(String.class);
                            final String busmessage = ds.child("message").getValue(String.class);
                            final String bussender = ds.child("sender").getValue(String.class);
                            final String timestampdate = ds.child("date").getValue(String.class);
//                            final Date dbTimeStampDate = ds.child("date").getValue(Date.class);
                            notificationstu.setBus(busvalue);
                            notificationstu.setMessage(busmessage);
                            notificationstu.setSender(bussender);
                            notificationstu.setDate(timestampdate);
                            final String mobiledate = pref.getString("TimeStamp", null);
//                            final long storedTimeStanp = pref.getLong("Timestamp",0);
//                            final Timestamp timestamp = new Timestamp(storedTimeStanp);
//                            final Timestamp dbTimeStamp = new Timestamp(dbTimeStampDate.getTime());

//                            Log.i("DateTag",timestamp.toString());
//                            Log.i("DaateTag",dbTimeStamp.toString());

                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.i("Data Received",dataSnapshot.toString());
                                    if (dataSnapshot.exists()) {
                                        String store = dataSnapshot.child("busnumber").getValue(String.class);

                                        if (store.equals(busvalue)){
                                            try {
                                                Date dbDate = simpleDateFormat.parse(timestampdate);
                                                Date prefDate = simpleDateFormat.parse(mobiledate);
                                                int val = dbDate.compareTo(prefDate);



                                                if (val>0) {
                                                    Random random = new Random();
                                                    int m = random.nextInt(9999 - 1000) + 1000;
                                                    Log.d("Tag", "In run");
                                                    Notification notify = new Notification.Builder
                                                            (ctx).setContentText(busmessage).
                                                            setContentTitle("Driver").setSmallIcon(R.drawable.ic_directions_bus_black_24dp).setColor(Color.BLUE).build();

                                                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                                    notify.defaults |= Notification.DEFAULT_SOUND;
                                                    notif.notify(m, notify);
                                                    editor = pref.edit();
                                                    editor.remove("TimeStamp");
                                                    String newtimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                                                    editor.putString("TimeStamp", newtimeStamp);
                                                    editor.commit();

                                                }
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


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
        });
        Log.d("Tag","Service Started");
    }

    class CustomTask extends AsyncTask<Integer,Integer,String>{

        @Override
        protected String doInBackground(Integer... integers) {
            try{
                while(true){
                    performTask();
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Done";
        }
        private void performTask(){

        }

    }
}
