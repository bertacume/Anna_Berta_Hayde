package upc.eet.pma.travelapp;

import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by bertacumellas on 9/12/16.
 */

public class travellerLocation extends AppCompatActivity {


    public travellerLocation() {

    }

    public String LocationToString(){
        Location location = myLocation();

        if(location!=null){
            double lat = 0.0;
            double lng = 0.0;
            lat = location.getLatitude();
            lng = location.getLongitude();
            String lat_str = Double.toString(lat);
            String lng_str = Double.toString(lng);
            String latlng = lat_str + "," + lng_str;
            return latlng;
            // location_txt.setText(latlng);
        }
        //else {location_txt.setText("0.0");}
        return "0.0";
    }


    public Location myLocation(){

        LocationManager locationManager = (LocationManager) this.getSystemService(android.content.Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        android.location.Location bestLocation = null;
        for (String provider : providers){
            android.location.Location location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()){
                    bestLocation = location;
                }
            }
        }

        //refreshLocation(bestLocation);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        return bestLocation;

    }


}
