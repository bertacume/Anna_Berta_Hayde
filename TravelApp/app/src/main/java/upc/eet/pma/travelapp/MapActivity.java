package upc.eet.pma.travelapp;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapActivity extends AppCompatActivity {
    private Button mProfileBtn;
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

        mProfileBtn = (Button) findViewById(R.id.ProfileBtn);

        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.ProfileActivity");
                startActivity(i);
            }
        });

        location_txt = (TextView) findViewById(R.id.locationTxt);
        myLocation();

       /* mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                travellerLocation actualLocation = new travellerLocation();
                String locationString = actualLocation.LocationToString();
                location_txt.setText(locationString);
            }
        }); */


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

        LocationToString(bestLocation);
    }
}
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        //return bestLocation;



