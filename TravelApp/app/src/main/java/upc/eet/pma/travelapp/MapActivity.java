package upc.eet.pma.travelapp;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback  {
    private Button mProfileBtn;
    private Button mSearchBtn;
    private Button mRequestsBtn;
    private Button mMyFriendsBtn;
    private GoogleMap mMap;
    private Marker marker;
    private TextView location_txt;
    private Button mLocation;
    double lat = 0.0;
    double lng = 0.0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();

        if (User.currentUser != null) {
            Log.v("Username", User.currentUser.full_name);
        } else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = mDatabase.child("Users");
            String userId = user.getUid();
            userRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String full_name = dataSnapshot.getValue(User.class).full_name;
                    String email = dataSnapshot.getValue(User.class).email;
                    String uLocation = dataSnapshot.getValue(User.class).ulocation;
                    Boolean isFantasma = dataSnapshot.getValue(User.class).isFantasma;
                    String Uid_ = dataSnapshot.getValue(User.class).Uid_;
                    Map friendsList = dataSnapshot.getValue(User.class).friendsList;
                    User.currentUser = new User(full_name,email,friendsList,uLocation,isFantasma,Uid_);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            // Firebase Current User Data saved into User.currentuser (static object)
        }


        mProfileBtn = (Button) findViewById(R.id.ProfileBtn);
        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.ProfileActivity");
                startActivity(i);
            }
        });

        mSearchBtn = (Button) findViewById(R.id.SearchBtn);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.SearchActivity");
                startActivity(i);

            }
        });

        mRequestsBtn = (Button) findViewById(R.id.RequestsBtn);
        mRequestsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.RequestsActivity");
                startActivity(i);

            }
        });

        mMyFriendsBtn = (Button) findViewById(R.id.MyFriendsBtn);
        mMyFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.MyFriendsActivity");
                startActivity(i);

            }
        });

        location_txt = (TextView) findViewById(R.id.locationTxt);
    }

    @Override
    public void onBackPressed() {
        return;
    }


    private void LocationToString(Location location) {

        if (location != null) {

            lat = location.getLatitude();
            lng = location.getLongitude();
            String lat_str = Double.toString(lat);
            String lng_str = Double.toString(lng);
            String latlng = lat_str + "," + lng_str;
            //return latlng;
            location_txt.setText(latlng);
        }
        //else {location_txt.setText("0.0");}
        //return "0.0";
    }

    private void addMarker(double lat, double lng){
        LatLng coordinates = new LatLng(lat,lng);
        CameraUpdate my_Location = CameraUpdateFactory.newLatLngZoom(coordinates,16);
        if (marker != null) marker.remove();
        MarkerOptions options = new MarkerOptions()
                .position(coordinates)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        mMap.addMarker(options);
        mMap.animateCamera(my_Location);
    }
    private void refreshLocation(Location location){
        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
        addMarker(lat,lng);
    }

    private void myLocation() {

        LocationManager locationManager = (LocationManager) this.getSystemService(android.content.Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        android.location.Location bestLocation = null;
        for (String provider : providers) {
            android.location.Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = location;
                }
            }
        }
        refreshLocation(bestLocation);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }



    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLocation();

        // Add a marker in Sydney and move the camera
        /* LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

    }



    /*@Override
    public void onMapReady(GoogleMap googleMap) {
       try {
            if (googleMap != null) {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "GOOGLE MAPS NOT LOADED");
        }
    }*/

}




