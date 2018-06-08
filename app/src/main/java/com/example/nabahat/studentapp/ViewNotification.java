package com.example.nabahat.studentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewNotification extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference ref;
    ListView listiew;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this,R.layout.notif, R.id.info, list);

        //notification = new Notification();
        listiew = (ListView)findViewById(R.id.listview);
        listiew.setAdapter(adapter);



        database = FirebaseDatabase.getInstance();

        DatabaseReference fbRefStudentNotify = FirebaseDatabase.getInstance().getReference("Notification")
                .child("AllStudents");
        fbRefStudentNotify.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String notification = ds.child("notification").getValue(String.class);
                    String bus = ds.child("bus").getValue(String.class);
                    adapter.add(notification+" sent by ADMIN" );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        DatabaseReference fbRefStudentNotify2 = FirebaseDatabase.getInstance().getReference("Auto")
                .child("AllStudents");
        fbRefStudentNotify2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String notification = ds.child("notification").getValue(String.class);
                    String bus = ds.child("bus").getValue(String.class);
                    adapter.add(notification+" sent by Weather Report" );
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
                    notification = ds.getValue(Notification.class);
                    final String busvalue = ds.child("bus").getValue(String.class);
                    final String busmessage = ds.child("message").getValue(String.class);
                    final String bussender = ds.child("sender").getValue(String.class);
                    notification.setBus(busvalue);
                    notification.setMessage(busmessage);
                    notification.setSender(bussender);

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("Data Received",dataSnapshot.toString());
                            if (dataSnapshot.exists()) {
                                String store = dataSnapshot.child("busnumber").getValue(String.class);

                                if (store.equals(busvalue)){
                                    //Toast.makeText(ViewNotification.this, store+busvalue,Toast.LENGTH_SHORT).show();
                                    adapter.add(busmessage+" sent by DRIVER");
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


        DatabaseReference dbrefb = FirebaseDatabase.getInstance().getReference("Notification").child("BusStudents");

    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goback = new Intent(ViewNotification.this, StudentHome.class);
        startActivity(goback);
        finish();
    }
}
