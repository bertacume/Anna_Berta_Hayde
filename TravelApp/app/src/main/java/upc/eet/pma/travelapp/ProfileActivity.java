package upc.eet.pma.travelapp;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.core.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "";
    private TextView mUserName;
    private Button msignOut;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = mDatabase.child("Users");

    private GoogleMap mMap;
    private Marker marker;
    double lat = 0.0;
    double lng = 0.0;
    private TextView location_txt;
    private Button mRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();


        usersRef.child(userId).child("full_name").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        String uName = dataSnapshot.getValue(String.class);
                        mUserName = (TextView) findViewById(R.id.userNametxt);

                        mUserName.setText(uName);

                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                }
        );



        msignOut = (Button)findViewById(R.id.signoutBtn);

        msignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        auth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
                    finish();
                }
            }
        };

        location_txt = (TextView) findViewById(R.id.location_txt);
        mRefresh = (Button) findViewById(R.id.refresh_btn);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myLocation();
            }
        });


    }

    private void signOut() {
        auth.signOut();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    private void refreshLocation(Location location){
        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();
            String lat_str = Double.toString(lat);
            String lng_str = Double.toString(lng);
            String latlng = lat_str + "," + lng_str;
            location_txt.setText(latlng);
        }
        //else {location_txt.setText("0.0");}
    }

    /* LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            refreshLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }
    }; */

    private void myLocation(){

        LocationManager locationManager = (LocationManager) this.getSystemService(android.content.Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers){
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()){
                    bestLocation = location;
                }
            }
        }

        refreshLocation(bestLocation);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }



}
