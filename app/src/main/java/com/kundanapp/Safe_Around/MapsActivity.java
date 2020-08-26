package com.kundanapp.Safe_Around;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener{
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker govtcasesMarkers;
    private ProgressDialog pDialog;
    FusedLocationProviderClient mFusedLocationClient;
    SharedPreferences pref;
    Marker marker;
    FirebaseAuth cAuth;
    FirebaseUser currentUser;
    View mapView;
    SharedPreferences.Editor editor;
    TextView confirm, active, recover, deac;
    private GoogleMap mMap;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                boolean val = pref.getBoolean("track", false);
                boolean val1 = pref.getBoolean("login", false);
                if (val == true && val1 == true) {
                    String path = "users/";
                    if (currentUser != null) {
                        String uid = currentUser.getUid();
                        path = path + uid;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                        govtcase usern = new govtcase();
                        usern.setLattitude(location.getLatitude());
                        usern.setLongitude(location.getLongitude());
                        usern.setName(currentUser.getPhoneNumber());
                        ref.setValue(usern);
                    }
                } else if (val == false && val1 == true) {
                    FirebaseUser currentUser = cAuth.getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + currentUser.getUid());
                    ref.removeValue();
                }
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                DatabaseReference mcurrent = FirebaseDatabase.getInstance().getReference("users");
                mcurrent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            govtcase user = s.getValue(govtcase.class);
                            LatLng location = new LatLng(user.getLattitude(), user.getLongitude());
                            Circle circle = mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(user.getLattitude(), user.getLongitude()))
                                    .radius(50)
                                    .strokeColor(Color.parseColor("#33c158dc"))
                                    .fillColor(Color.parseColor("#99c158dc")));
//                            mMap.addMarker(new MarkerOptions().position(location).icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.orange_circle)));
//                            mMap.addMarker(new MarkerOptions().position(location).title(user.getName()));
//                            mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference mvolunteer = FirebaseDatabase.getInstance().getReference("Volunteer");
                mvolunteer.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot s : dataSnapshot.getChildren()) {
                            volunteerinfo user = s.getValue(volunteerinfo.class);
                            LatLng location = new LatLng(user.getLat(), user.getLang());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(location);
                            markerOptions.title("Volunteer : " + user.getName());
                            markerOptions.snippet("Go to Volunteer Tab for Details and Contact Info");
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            mMap.addMarker(markerOptions);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
            }
        }
    };
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    private RequestQueue queue;
    Toolbar toolbar;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    TextView not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        boolean val = pref.getBoolean("newuser", true);
        if(val)
        {
            Intent intent = new Intent(getApplicationContext(),Intro.class);
            startActivity(intent);
        }
        setTitle("Overview");
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.drawermob);
        ImageView navpic = (ImageView) headerView.findViewById(R.id.drawerpic);

        mapView = mapFragment.getView();
        queue = Volley.newRequestQueue(this);
        ChildEventListener mChildEventListener;
        mUsers = FirebaseDatabase.getInstance().getReference("govtcases");
        mUsers.push().setValue(marker);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        // Initialize Firebase Auth
        cAuth = FirebaseAuth.getInstance();
        currentUser = cAuth.getCurrentUser();
        if(currentUser!=null)
        {
            navUsername.setText(currentUser.getPhoneNumber());
            navpic.setImageResource(R.drawable.circlelogin);
        }
        else
            navUsername.setText("No Login Info");

        findViewById(R.id.customDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AssessInfo.class);
                startActivity(i);
            }
        });



        findViewById(R.id.covidcases).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(i);
            }
        });

        confirm = findViewById(R.id.confirm_no);
        active = findViewById(R.id.active_no);
        recover = findViewById(R.id.recovered_no);
        deac = findViewById(R.id.deceased_no);
        String url = "https://api.covid19india.org/data.json";
        pDialog = ProgressDialog.show(this, "Loading",
                "Please wait while we fetch latest information for you", false);
        pDialog.setCancelable(true);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject result = new JSONObject(response).getJSONArray("statewise").getJSONObject(0);
                            confirm.setText(result.getString("confirmed"));
                            active.setText(result.getString("active"));
                            recover.setText(result.getString("recovered"));
                            deac.setText(result.getString("deaths"));

                            // To Show date when Last Updated

//                            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
//                            JSONObject date = new JSONObject(response);
//                            try {
//                                Date myDate = myFormat.parse(date.getString("lastRefreshed"));
//                                Toast.makeText(MapsActivity.this, "Updated At: " + myDate, Toast.LENGTH_SHORT).show();
//                            } catch (ParseException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }


                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "Please Check Your Internet Connection or try Restarting the App", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        findViewById(R.id.legend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.legenddetail, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    public void refresh(View view) {          //refresh is onClick name given to the button
        onRestart();
    }

    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(120000); // ten minute interval
//        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }
        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 350, 30, 0);

        }

        mUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    govtcase user = s.getValue(govtcase.class);
                    LatLng location = new LatLng(user.getLattitude(), user.getLongitude());
                    govtcasesMarkers = mMap.addMarker(new MarkerOptions().position(location).icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.point)).title(user.getName()));

                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));


                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(new LatLng(user.getLattitude(), user.getLongitude()))
                            .radius(500)
                            .clickable(true)
                            .strokeColor(Color.parseColor("#33ff0000"))
                            .fillColor(Color.parseColor("#26ff0000")));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home)
        {
            drawer.closeDrawer(Gravity.LEFT);
        }
        if(item.getItemId() == R.id.dashboard)
        {
            Intent i = new Intent(getApplicationContext(), Dashboard.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.resources)
        {
            Intent i = new Intent(getApplicationContext(), Resources.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.color)
        {
            Intent i = new Intent(getApplicationContext(), ColorZones.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.volunteer)
        {
            Intent i = new Intent(getApplicationContext(), volunteer.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.how)
        {
            Intent i = new Intent(getApplicationContext(), Intro.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.hotspot)
        {
            Intent i = new Intent(getApplicationContext(), AssessInfo.class);
            startActivity(i);
        }
        if(item.getItemId() == R.id.contact)
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","kundank102@gmail.com", null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        }
        if(item.getItemId() == R.id.share)
        {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Covid-19 Tracker");
                String shareMessage= "\nLet me recommend you this application. You can see Hotspot zones near your area, track live statistics and information about essentials and other resources\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setMessage("Are you sure you want to Exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void startcolor(View view) {
        Intent i = new Intent(getApplicationContext(), ColorZones.class);
        startActivity(i);
    }
}
