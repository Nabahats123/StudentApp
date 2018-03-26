package com.example.nabahat.studentapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Normalizer;

public class RegisterationForm extends AppCompatActivity {

    EditText Name, Email, Phone, Address, BusNumber, StopName, Date;
    Button Choose, Upload, submit;
    ImageView image;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Uri resulturi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_form);


        Name = (EditText) findViewById(R.id.Reg_name);
        Email = (EditText) findViewById(R.id.reg_email);
        Phone = (EditText) findViewById(R.id.reg_phone);
        Address = (EditText) findViewById(R.id.reg_address);
        BusNumber = (EditText) findViewById(R.id.reg_bus);
        StopName = (EditText) findViewById(R.id.reg_stop);

        image = (ImageView) findViewById(R.id.profileimg);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        // perform click event on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Name.getText().toString();
                String email = Email.getText().toString();
                String phone = Phone.getText().toString();
                String address = Address.getText().toString();
                String busnumber = BusNumber.getText().toString();
                String stopname = StopName.getText().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Registration");
                form regform = new form(name, email, phone, address, busnumber,stopname);
                mDatabase.child(userId).setValue(regform);
                if (resulturi!= null){

                }

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageuri = data.getData();
            resulturi = imageuri;
             image.setImageURI(resulturi);
        }
    }
}
