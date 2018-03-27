package com.example.nabahat.studentapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nabahat.studentapp.fragments.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class StudentMaps2Activity extends AppCompatActivity {

    private String latitude,longitude;
    private MapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps2);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            latitude =(String) b.get("latitude");
            longitude = (String) b.get("longitude");
            //Toast.makeText(StudentMapsActivity.this, j, Toast.LENGTH_SHORT).show();

        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Permission granteed",Toast.LENGTH_SHORT).show();
            mapFragment = new MapFragment();
            mapFragment.setLocationListener(new MapFragment.LocationPassedListener() {
                @Override
                public LatLng onLocationPassed() {
                    double lat= Double.parseDouble(latitude);
                    double lon= Double.parseDouble(longitude);
                    LatLng latLng = new LatLng(lat, lon);
                    return latLng;
                }
            });
            getSupportFragmentManager().beginTransaction().add(R.id.mapContainer,mapFragment,"Map Fragment").commit();
        }
    }
}



 