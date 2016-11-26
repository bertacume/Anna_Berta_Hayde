package upc.eet.pma.travelapp;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by hayde_000 on 25/11/2016.
 */

public class TravelApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
