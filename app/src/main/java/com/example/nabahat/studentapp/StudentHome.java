package com.example.nabahat.studentapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StudentHome extends AppCompatActivity {
    TextView StartTracker, GiveFeeback, Logout;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        StartTracker =findViewById(R.id.lay_starttracker);
        GiveFeeback = findViewById(R.id.lay_feedback);
        Logout = findViewById(R.id.lay_signout);
        mDatabase = FirebaseDatabase.getInstance().getReference("Student");

        StartTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                                    Log.i("Data", ds.getValue(Driver.class).id);
                                                    Driver driver = ds.getValue(Driver.class);
                                                    String id = driver.id; //it will have id stored in it, you can use it further as you like
                                                    mref.child("DriverLocation").child(id).child("l").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            // Log.i("Longitude",
                                                            String lat = String.valueOf(dataSnapshot.child("0").getValue(Double.class));
                                                            String lon = String.valueOf(dataSnapshot.child("1").getValue(Double.class));

                                                            Toast.makeText(StudentHome.this, lat + "    " + lon, Toast.LENGTH_SHORT).show();
                                                            Log.i("Data", String.valueOf(dataSnapshot.child("0").getValue(Double.class)));
                                                            Log.i("Data", String.valueOf(dataSnapshot.child("1").getValue(Double.class)));

                                                            Intent OpenMap = new Intent(StudentHome.this, StudentMapsActivity.class);
                                                            OpenMap.putExtra("latitude",lat);
                                                            OpenMap.putExtra("longitude",lon);
                                                            //String user_Id = mAuth.getCurrentUser().getUid();
                                                            // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);

                                                            startActivity(OpenMap);
                                                            finish();

//                                                        for(DataSnapshot ds:dataSnapshot.getChildren()){
//                                                            Log.i("Some Data",ds.toString());
////                                                            Log.i("Latitude",ds.child("l").getValue(String.class));
////                                                            Log.i("Latitude",ds.child("0").getValue().toString());
//                                                        }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

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
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.getInstance().signOut();
                startActivity(new Intent(StudentHome.this, MainActivity.class));
                finish();
            }
        });
        GiveFeeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent feedback = new Intent(StudentHome.this, GiveFeedback.class);
                startActivity(feedback);
                finish();
            }
        });




    }

}
