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
                Intent OpenMap = new Intent(StudentHome.this, StudentMapsActivity.class);
                //String user_Id = mAuth.getCurrentUser().getUid();
                // DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Driver").child(user_Id);

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


        GiveFeeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Student").child(userId);


                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       if (dataSnapshot.exists()){
                           String store= dataSnapshot.child("busnumber").getValue(String.class);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver");



                        FirebaseDatabase.getInstance().getReference().child("Driver").orderByChild("busnumber").equalTo(store)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                                String DrivrID = data.child("id").getValue(String.class);
                                                String DriverName
                                                String DriverEmail
                                                String DriverBus
                                                //starItem.setIcon(R.drawable.star);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.i("fyales","The dataChanged");
                                    }
                                });}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
              }
        });

    }
}
