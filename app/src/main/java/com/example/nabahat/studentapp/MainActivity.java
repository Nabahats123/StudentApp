package com.example.nabahat.studentapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    EditText StudentName, StudentEmail, StudentPhone, StudentPassword, StudentBusNumber;
    TextView SignUp, SignIn;

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseauthlistener;
    Student student;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Student");
        id = mDatabase.push().getKey();

        firebaseauthlistener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!= null){
                    Intent OpenMap = new Intent(MainActivity.this, StudentHome.class);
                    startActivity(OpenMap);
                    OpenMap.putExtra("Bus Number", StudentBusNumber.getText().toString());
                    finish();
                    return;
                }
            }
        };
        StudentName = findViewById(R.id.lay_name);
        StudentEmail = findViewById(R.id.lay_email);
        StudentPhone =  findViewById(R.id.lay_phone);
        StudentPassword = findViewById(R.id.lay_password);
        StudentBusNumber = findViewById(R.id.lay_busnumber);
        SignIn =  findViewById(R.id.lay_signin);
        SignUp = findViewById(R.id.lay_signup);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = StudentEmail.getText().toString();
                final String password = StudentPassword.getText().toString();
                if (email.equals("") || password.equals("") ) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Error")
                            .setMessage("Please enter Email and Password")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                builder.setTitle("Error")
                                        .setMessage(task.getException().getMessage())
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // continue with delete
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                              //  Toast.makeText(MainActivity.this, "Sign In Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String SName = StudentName.getText().toString();
                final String SEmail = StudentEmail.getText().toString();
                final  String SPassword = StudentPassword.getText().toString();
                final  String SPhone = StudentPhone.getText().toString();
                final  String SBusNumber = StudentBusNumber.getText().toString();
                if (SName.equals("") || SPassword.equals("") || SBusNumber.equals("") || SEmail.equals("") || SPhone.equals("")) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                    builder.setTitle("Error")
                            .setMessage("Please Enter All Details")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {

                    mAuth.createUserWithEmailAndPassword(SEmail, SPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                String user_Id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference("Student").child(user_Id);
                                current_user_db.setValue(true);
                                Student student = new Student(user_Id, SName, SEmail, SPassword, SPhone, SBusNumber);
                                current_user_db.setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                            builder.setTitle("Error")
                                                    .setMessage(task.getException().getMessage())
                                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // continue with delete
                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            // do nothing
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                            //Toast.makeText(MainActivity.this, "Record Not Added" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }

            }
        });

    }


    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(firebaseauthlistener);
    }
}
