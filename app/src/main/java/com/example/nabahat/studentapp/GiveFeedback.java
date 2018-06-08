package com.example.nabahat.studentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GiveFeedback extends AppCompatActivity {

    EditText Feedback;
    TextView Details, average;
    Button Send;
    RatingBar Bar, Bar2;
    String name, email, number, did;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
        Details = (TextView) findViewById(R.id.Displaydetails);
        average = (TextView) findViewById(R.id.rating);
        Feedback = (EditText) findViewById(R.id.lay_feedback);
        Send = (Button) findViewById(R.id.lay_send);
        Bar = (RatingBar) findViewById(R.id.ratingBar);
        Bar2 = (RatingBar) findViewById(R.id.ratingBar2);


        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            name =(String) b.get("Name");
            email = (String) b.get("Email");
            number = (String) b.get("Phone Number");
            Details.setText(name+"\n" + email+"\n"+ number+"\n");
            did = (String) b.get("id");



        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Driver").child(did);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Data Received",dataSnapshot.toString());
                if (dataSnapshot.exists()) {




                    int ratingsum = 0;
                    int ratingtotal = 0;
                    float ratingsaverage = 0;
                    for (DataSnapshot child: dataSnapshot.child("ratings").getChildren()){
                        ratingsum = ratingsum + Integer.valueOf(child.getValue().toString());
                        ratingtotal++;

                    }
                    if(ratingtotal!=0){
                        ratingsaverage = ratingsum/ratingtotal;
                        Bar2.setRating(ratingsaverage);
                        String avg = Float.toString(ratingsaverage);

                        average.setText(avg+"("+ratingtotal+")");
                        //Toast.makeText(ViewProfile.this, avg+" ("+ratingtotal+") " , Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                  //  Toast.makeText(ViewProfile.this, "No Snapshot", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final float ratings = Bar.getRating();
                final Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                final String formattedDate = df.format(c);
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                                                String name = "";
                                                String email = "";
                                                String number = "";
                                                String drivid = "";

                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    Driver driver = ds.getValue(Driver.class);
                                                    name = driver.username;
                                                    email = driver.email;
                                                    number = driver.phonenumber;
                                                    drivid = driver.id;

                                                }

                                                        DatabaseReference mdref = FirebaseDatabase.getInstance().getReference("Feedback");
                                                        Feedback feedbackstore = new Feedback(userId,drivid,Feedback.getText().toString(), ratings);//it will have id stored in it, you can use it further as you like



                                               mdref.child(userId).setValue(feedbackstore);
                                                DatabaseReference newref = FirebaseDatabase.getInstance().getReference().child("Driver").child(drivid);
                                                       newref.child("ratings").child(userId).setValue(ratings);
                                                        startActivity(new Intent(GiveFeedback.this, StudentHome.class));
                                                        finish();






                                            }
                                            else{Toast.makeText(GiveFeedback.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
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
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(GiveFeedback.this, StudentHome.class));
        finish();
    }
}
