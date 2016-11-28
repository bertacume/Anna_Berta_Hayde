package upc.eet.pma.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class NewUserActivity extends AppCompatActivity {

    private Button mNewUserBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mNewUserBtn = (Button) findViewById(R.id.newuserBtn);
    }
}
