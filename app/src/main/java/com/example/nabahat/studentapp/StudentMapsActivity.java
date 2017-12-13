package com.example.nabahat.studentapp;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.nabahat.studentapp.R.id.map;

public class StudentMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {


    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation, loc;
    int i = 1;
    private List<Polyline> polylines;
    ArrayList<LatLng> MarkerPoints;
    String latitude = "";
    String longitude = "";
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorPrimary,R.color.colorAccent,R.color.primary_dark_material_light};
    LocationRequest mLocationRequest;
    FirebaseDatabase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            latitude =(String) b.get("latitude");
            longitude = (String) b.get("longitude");
            //Toast.makeText(StudentMapsActivity.this, j, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
        double lat= Double.parseDouble(latitude);
        double lon= Double.parseDouble(longitude);
        LatLng latLng = new LatLng(lat, lon);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        if (i==1){
            mMap.addMarker(new MarkerOptions().position(latLng).title("Driver Location"));
            //getDirection(latLng);
            i = 2;
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;



    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        startActivity(new Intent(StudentMapsActivity.this, StudentHome.class));
        finish();
        // optional depending on your needs
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //mLocationRequest = new LocationRequest();
       // mLocationRequest.setInterval(500);
       // mLocationRequest.setFastestInterval(500);
        requestlatlong();
       // mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(StudentMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StudentMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

        //double lati= Double.parseDouble(latitude);
        // double longi= Double.parseDouble(longitude);
        // LatLng exp = new LatLng(lati, longi);
        // mMap.addPolyline(new PolylineOptions().add(latLng,exp).width(10).color(R.color.colorPrimaryDark));
        MarkerOptions distance = new MarkerOptions();
        // distance.position(exp);
        distance.title("Destination");
        float results[] = new float[5];
        //Location.distanceBetween(latLng.latitude, latLng.longitude, exp.latitude,exp.longitude, results);
        distance.snippet("Distance = " + results[0] + "m");
        //mMap.addMarker(distance);
   }

    private void requestlatlong() {
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

                                                    String lat = String.valueOf(dataSnapshot.child("0").getValue(Double.class));
                                                    String lon = String.valueOf(dataSnapshot.child("1").getValue(Double.class));
                                                    Log.i("Data", String.valueOf(dataSnapshot.child("0").getValue(Double.class)));
                                                    Log.i("Data", String.valueOf(dataSnapshot.child("1").getValue(Double.class)));
                                                    double clat= Double.parseDouble(lat);
                                                    double clon= Double.parseDouble(lon);
                                                    LatLng latLng = new LatLng(clat, clon);
                                                    Toast.makeText(StudentMapsActivity.this, "new "+lat + "    " + lon, Toast.LENGTH_SHORT).show();
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                                    if (i==1){
                                                        mMap.addMarker(new MarkerOptions().position(latLng).title("Driver Location"));
                                                        //getDirection(latLng);
                                                        i = 2;
                                                    }
                                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
//
                                    }
                                    else{Toast.makeText(StudentMapsActivity.this, "No Snapshot", Toast.LENGTH_SHORT).show();}
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


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            //   Toast.makeText(this, "Error: " +e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        }


    @Override
    public void onRoutingCancelled() {
    }
    @Override
    protected void onStop(){
        super.onStop();

    }
}