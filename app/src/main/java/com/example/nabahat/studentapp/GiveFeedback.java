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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GiveFeedback extends AppCompatActivity {

    EditText Feedback;
    TextView Details;
    Button Send;
    RatingBar Bar;
    String name, email, number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
        Details = (TextView) findViewById(R.id.Displaydetails);
        Feedback = (EditText) findViewById(R.id.lay_feedback);
        Send = (Button) findViewById(R.id.lay_send);
        Bar = (RatingBar) findViewById(R.id.ratingBar);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            name =(String) b.get("Name");
            email = (String) b.get("Email");
            number = (String) b.get("Phone Number");
            Details.setText("Name: "+ name+"\n" + "Email: "+ email+"\n"+"Phone: "+ number+"\n");

            //Toast.makeText(StudentMapsActivity.this, j, Toast.LENGTH_SHORT).show();

        }
        Send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                final float ratings = Bar.getRating();
                final Date c = Calendar.getInstance().getTime();
              //  System.out.println("Current time => " + c);

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
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    Log.i("Data", ds.getValue(Driver.class).id);
                                                    Driver driver = ds.getValue(Driver.class);
                                                    String id = driver.id;

                                                    Feedback feedbackstore = new Feedback(userId,id,Feedback.getText().toString(), formattedDate, ratings);//it will have id stored in it, you can use it further as you like
                                                    mref.child("Feedback").child(id).setValue(feedbackstore);
                                                    DatabaseReference newref = FirebaseDatabase.getInstance().getReference().child("Driver").child(id);
                                                    newref.child("ratings").child(userId).setValue(ratings);
                                                    //Toast.makeText(GiveFeedback.this, name+email+number, Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(GiveFeedback.this, StudentHome.class));
                                                    finish();

                                               }


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
