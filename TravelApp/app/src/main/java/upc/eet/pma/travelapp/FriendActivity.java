package upc.eet.pma.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;
    private TextView user_emailuser;
    private String id_pos;
    private ToggleButton Follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        id_pos = getIntent().getExtras().getString("id_pos");
        int id_ = Integer.parseInt(id_pos);
        ArrayList<String> userList = (ArrayList<String>) getIntent().getSerializableExtra("userList");
        user_emailuser = (TextView) findViewById(R.id.useremail_txt);
        final String user_email = userList.get(id_);
        user_emailuser.setText(user_email);

        Follow = (ToggleButton) findViewById(R.id.FollowBtn);
        Follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(FriendActivity.this, String.format("Following '%s'", user_email), Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    Toast.makeText(FriendActivity.this, String.format("Unfollowing '%s'", user_email), Toast.LENGTH_SHORT).show();
                }
            }
        });}}