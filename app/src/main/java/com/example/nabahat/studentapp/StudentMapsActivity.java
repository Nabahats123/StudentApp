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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
    Double slat, slon;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,R.color.colorPrimary,R.color.colorPrimary,R.color.colorAccent,R.color.primary_dark_material_light};
    LocationRequest mLocationRequest;
    FirebaseDatabase ref;

    Marker userLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_maps);

        buildGoogleApiClient();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

//        Intent iin= getIntent();
//        Bundle b = iin.getExtras();
//        if(b!=null)
//        {
//            latitude =(String) b.get("latitude");
//            longitude = (String) b.get("longitude");
//            //Toast.makeText(StudentMapsActivity.this, j, Toast.LENGTH_SHORT).show();
//
//        }
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
//                                            Log.i("Data", ds.getValue(Driver.class).id);
                                            Driver driver = ds.getValue(Driver.class);
                                            String id = driver.id;
                                           // Toast.makeText(StudentMapsActivity.this, id, Toast.LENGTH_SHORT).show();
                                            //it will have id stored in it, you can use it further as you like
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
                                                    //Toast.makeText(StudentMapsActivity.this, "N Cordinates"+lat + "    " + lon, Toast.LENGTH_SHORT).show();
                                                    if(userLocation==null){
                                                        MarkerOptions options = new MarkerOptions()
                                                                .title("Location")
                                                                .draggable(false)
                                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                                                                .position(latLng);
                                                        userLocation = mMap.addMarker(options);


//                                                        Float dis = mLastLocation.distanceTo(loc);
//                                                        Toast.makeText(StudentMapsActivity.this, dis.toString(), Toast.LENGTH_SHORT).show();




                                                    }else{
                                                        userLocation.setPosition(latLng);
                                                    }
                                                    CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(18).build();
                                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //getDirection(latLng);

//
//
//        i=2;}
    //    mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


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
        if (ActivityCompat.checkSelfPermission(StudentMapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StudentMapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return; }
        loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
        MarkerOptions distance = new MarkerOptions();
        distance.title("Destination");
        float results[] = new float[5];
        distance.snippet("Distance = " + results[0] + "m");


   }

        @Override
    public void onConnectionSuspended(int i) {
        startActivity(new Intent(StudentMapsActivity.this, StudentHome.class));
        finish();
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