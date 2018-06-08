package com.example.nabahat.studentapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.TokenWatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterationForm extends AppCompatActivity {

   private EditText Name, FatherName, Email, Phone, EmergencyPhone, Address,  StopName, rollnum, prgname, semester;
    Spinner StudentBusNumber, year, course;;
    Button submit;
    private static CheckBox terms_conditions;
    ImageView image;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Uri resulturi;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_form);

        Name = (EditText) findViewById(R.id.reg_name);
        FatherName = (EditText) findViewById(R.id.reg_fathername);
        Email = (EditText) findViewById(R.id.reg_email);
        Phone = (EditText) findViewById(R.id.reg_phone);
        EmergencyPhone = (EditText) findViewById(R.id.reg_ephone);
        Address = (EditText) findViewById(R.id.reg_address);
        StudentBusNumber = (Spinner) findViewById(R.id.lay_reg_bus);
        StopName = (EditText) findViewById(R.id.reg_stop);
        terms_conditions = (CheckBox)findViewById(R.id.reg_dec);
        radioGroup = (RadioGroup) findViewById(R.id.RGroup);
        submit = (Button) findViewById(R.id.submit);
        semester = (EditText)findViewById(R.id.Lay_semester);
        year = (Spinner) findViewById(R.id.lay_year);
        year.setPrompt("Year");
        course = (Spinner) findViewById(R.id.lay_coursenamee);
        year.setPrompt("Course");
        rollnum = (EditText)findViewById(R.id.lay_rollnumber) ;

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                R.array.Bus_Numbers, android.R.layout.simple_spinner_dropdown_item);
        StudentBusNumber.setAdapter(dataAdapter);
        StudentBusNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> dataAdapter2 = ArrayAdapter.createFromResource(this,
                R.array.years, android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(dataAdapter2);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));
                // OR ((TextView)parentView.getChildAt(0)).setTextColor(Color.RED);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> dataAdapter3 = ArrayAdapter.createFromResource(this,
                R.array.coursename, android.R.layout.simple_spinner_dropdown_item);
        course.setAdapter(dataAdapter3);
        course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));
                // OR ((TextView)parentView.getChildAt(0)).setTextColor(Color.RED);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // perform click event on submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = Name.getText().toString();
                final String fathername = FatherName.getText().toString();
                final String email = Email.getText().toString();
                final String phone = Phone.getText().toString();
                final String emerrgencyphone = EmergencyPhone.getText().toString();
                final String address = Address.getText().toString();
                final String busnumber = StudentBusNumber.getSelectedItem().toString();
                final String stopname = StopName.getText().toString();
                final String getRegistrationNumber = year.getSelectedItem().toString() + "-" + course.getSelectedItem().toString() + "-" + rollnum.getText().toString();
                final String semes = semester.getText().toString();
                final Calendar c = Calendar.getInstance();
                SimpleDateFormat df1 = new SimpleDateFormat("h:mm a");
                final String formattedTime = df1.format(c.getTime());
                DateFormat dateFormatter = new SimpleDateFormat("yyyy:MM:dd");
                final String formattedDate = dateFormatter.format(c.getTime());
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                radioButton1 = (RadioButton) findViewById(R.id.program_bs);
                radioButton2 = (RadioButton) findViewById(R.id.program_ms);
                int flatTypeId = radioGroup.getCheckedRadioButtonId();
                final String flat_type = ((RadioButton) findViewById(flatTypeId)).getText().toString();
                Pattern p = Pattern.compile(Utils.regEx);
                Matcher m = p.matcher(email);

                if (name.equals("") || name.length() == 0
                        || fathername.equals("") || fathername.length() == 0
                        || email.equals("") || email.length() == 0
                        || getRegistrationNumber.equals("") || getRegistrationNumber.length() == 0
                        || flat_type.equals("") || flat_type.length() == 0
                        || semes.equals("") || semes.length() == 0
                        || phone.equals("") || phone.length() == 0
                        || emerrgencyphone.equals("") || emerrgencyphone.length() == 0
                        || address.equals("") || address.length() == 0
                        || busnumber.equals("") || busnumber.length() == 0
                        || stopname.equals("") || stopname.length() == 0) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast2,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("All fields are required");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast
                }

                // Check if email id valid or not
                else if (!m.find()) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast2,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Your Email Id is Invalid");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast
                }

                // Check if both password should be equal
                else if (phone.equals(emerrgencyphone)) {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast2,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Both phone numbers should be different");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast

                } else if (!terms_conditions.isChecked()) {

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // inflate the layout over view
                    View layout = inflater.inflate(R.layout.custom_toast2,
                            (ViewGroup) findViewById(R.id.toast_root));

                    // Get TextView id and set error
                    TextView text = (TextView) layout.findViewById(R.id.toast_error);
                    text.setText("Please select Terms and Conditions");

                    Toast toast = new Toast(getApplicationContext());// Get Toast Context
                    toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                    // Toast
                    // gravity
                    // and
                    // Fill
                    // Horizoontal

                    toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                    toast.setView(layout); // Set Custom View over toast

                    toast.show();// Finally show toast


                }






                else {

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Registration").child(getRegistrationNumber);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                // inflate the layout over view
                                View layout = inflater.inflate(R.layout.custom_toast2,
                                        (ViewGroup) findViewById(R.id.toast_root));

                                // Get TextView id and set error
                                TextView text = (TextView) layout.findViewById(R.id.toast_error);
                                text.setText("Registration Number already registered");

                                Toast toast = new Toast(getApplicationContext());// Get Toast Context
                                toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
                                // Toast
                                // gravity
                                // and
                                // Fill
                                // Horizoontal

                                toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
                                toast.setView(layout); // Set Custom View over toast

                                toast.show();// Finally show toast
                            }

                            else
                            {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Registration");
                                form regform = new form(getRegistrationNumber, flat_type, semes, name, fathername, email, phone, emerrgencyphone, address, busnumber, stopname,formattedDate, formattedTime);
                                mDatabase.child(getRegistrationNumber).setValue(regform);
                                Toast.makeText(RegisterationForm.this, "Registration Succssful", Toast.LENGTH_SHORT).show();
                                Intent goback = new Intent(RegisterationForm.this, StudentHome.class);
                                startActivity(goback);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent viewprofile = new Intent(RegisterationForm.this, StudentHome.class);
        startActivity(viewprofile);
        finish();// mAuth.removeAuthStateListener(firebaseauthlistener);




    }
}
