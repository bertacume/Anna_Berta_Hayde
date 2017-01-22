package upc.eet.pma.travelapp;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;
import java.util.Locale;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "";
    private TextView mUserName,mlocationTxt;
    private Button msignOut,meditProfile;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = mDatabase.child("Users");
    private static final int MY_PERMISION_REQUEST_LOCATION=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mlocationTxt = (TextView) findViewById(R.id.locationTxt);

        if(ContextCompat.checkSelfPermission(ProfileActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)){
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISION_REQUEST_LOCATION);
            }
            else{
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISION_REQUEST_LOCATION);
            }

        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try{
                mlocationTxt.setText(hereLocation(location.getLatitude(),location.getLongitude()));
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ProfileActivity.this,"Not found",Toast.LENGTH_SHORT).show();
            }
        }

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
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                }
        );

        String uLocation = mlocationTxt.getText().toString();
        usersRef.child(userId).child("ulocation").setValue(uLocation);

        meditProfile =(Button) findViewById(R.id.editProfileBtn);
        meditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));

            }
        });
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
                    startActivity(new Intent(ProfileActivity.this, LogInActivity.class));
                    finish();
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       switch (requestCode){
           case MY_PERMISION_REQUEST_LOCATION:{
               if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                 if(ContextCompat.checkSelfPermission(ProfileActivity.this,
                         Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                     LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                     Location location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                     try{
                         mlocationTxt.setText(hereLocation(location.getLatitude(),location.getLongitude()));///////////////////////////////////hereLocation
                     }catch (Exception e){
                         e.printStackTrace();
                         Toast.makeText(ProfileActivity.this,"Not found",Toast.LENGTH_SHORT).show();
                     }
                 }
               }else{
                   Toast.makeText(ProfileActivity.this,"Not permission granted",Toast.LENGTH_SHORT).show();
               }
           }
       }



    }

    public String hereLocation(double lat, double lon){
        String curCity="";
        Geocoder geocoder =new Geocoder(ProfileActivity.this, Locale.getDefault());
        List<Address> addressList;
        try{
            addressList = geocoder.getFromLocation(lat,lon,1);
            if(addressList.size()>0){
                curCity=addressList.get(0).getLocality()+ ", ";//para retornar el estado (USA) usar getAdminArea()
                curCity += addressList.get(0).getAddressLine(2)+", "; //en el caso de España esta linea regresa la provincia
                curCity += addressList.get(0).getCountryName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curCity;
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
}
