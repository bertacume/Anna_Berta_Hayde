package upc.eet.pma.travelapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
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
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback  {
    private Button mProfileBtn;
    private Button mSearchBtn;
    private Button mMyFriendsBtn;
    private Button myLocation;
    private GoogleMap mMap;
    private Marker marker;
    private Marker friendMarker;
    private TextView location_txt;
    double lat = 0.0;
    double lng = 0.0;

    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initMap();

        MyDatabaseUtil.getDatabase(); //Per inicialitzar el Firebase
        usersDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        usersDatabaseReference = usersDatabase.getReference().child("Users").child(userId).child("friendsList");


        if (User.currentUser != null) {
            Log.v("Username", User.currentUser.full_name + "");
        } else{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = mDatabase.child("Users");
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
            // Firebase Current User Data saved into User.currentuser (static object) per evitar un error si hi ha un fallo de log in
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

        mMyFriendsBtn = (Button) findViewById(R.id.MyFriendsBtn);
        mMyFriendsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.MyFriendsActivity");
                startActivity(i);

            }
        });

        myLocation = (Button) findViewById(R.id.MyLocationBtn);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng coordinates = getCoordinates();
                animateCamera(coordinates);
            }
        });

        location_txt = (TextView) findViewById(R.id.locationTxt);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void animateCamera(LatLng coordinates){
        CameraUpdate my_Location = CameraUpdateFactory.newLatLngZoom(coordinates,16);
        mMap.animateCamera(my_Location);
    }

    private void animateCameraFriend(LatLng coordinates){

    }

    private void addMarker(LatLng coordinates){
        if (marker != null) marker.remove();
        CameraUpdate my_Location = CameraUpdateFactory.newLatLngZoom(coordinates,16);
        MarkerOptions options = new MarkerOptions()
                .position(coordinates)
                .title("I am here!")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        mMap.addMarker(options);
        //animateCamera(coordinates);
        //CameraUpdate my_Location = CameraUpdateFactory.newLatLngZoom(coordinates,16);
        //mMap.animateCamera(my_Location);
        //marcador personal afegit

    }

    private void addFriendMarker(LatLng friendCoordinates, String email){
        if (friendMarker != null) friendMarker.remove();
        //LatLng friendCoordinates = new LatLng(Lat, Lng);
        MarkerOptions optionsFriend = new MarkerOptions()
                .position(friendCoordinates)
                .title(email)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        mMap.addMarker(optionsFriend);

    }

    private LatLng getCoordinates (){
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
        if(bestLocation!=null){
            lat = bestLocation.getLatitude();
            lng = bestLocation.getLongitude();
        }
        LatLng coordinates = new LatLng(lat,lng);
        return coordinates;
    }

    private void myLocation() {
        LatLng coordinates = getCoordinates();
        addMarker(coordinates);
    }

    public void myFriendsLocation (){
        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                while (iterator.hasNext()) {
                    DataSnapshot data = iterator.next();
                    Object Uid_friend = data.child("Uid_friend").getValue();
                    String S_UidFriend = Uid_friend.toString();

                    DatabaseReference usersRef = usersDatabase.getReference().child("Users");
                    usersRef.child(S_UidFriend).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Get each user email and Location
                                    String email = dataSnapshot.getValue(User.class).email;
                                    String uLocation = dataSnapshot.getValue(User.class).ulocation;
                                    if ( uLocation.equals("Location") || uLocation.equals(null)){
                                        uLocation = "Location";
                                    }
                                    else {
                                        LatLng LatLng_ulocation = getLocationFromAddress(uLocation);
                                        if (LatLng_ulocation.equals(null)){

                                        }else{
                                            addFriendMarker(LatLng_ulocation, email);
                                            Log.v("LocationFriend", uLocation);}
                                    }

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            }
                    );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }



        });}

    public LatLng getLocationFromAddress(String friendAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(friendAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLocation();
        myFriendsLocation ();
    }

}





