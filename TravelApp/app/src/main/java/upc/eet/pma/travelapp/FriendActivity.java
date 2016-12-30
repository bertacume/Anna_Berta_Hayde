package upc.eet.pma.travelapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
    private TextView user_name;
    private String id_pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        id_pos = getIntent().getExtras().getString("id_pos");
        int id_ = Integer.parseInt(id_pos);
        ArrayList<String> userList = (ArrayList<String>) getIntent().getSerializableExtra("userList");
        user_name = (TextView) findViewById(R.id.username_txt);
        String user_email = userList.get(id_);
        user_name.setText(user_email);

    }
    }
