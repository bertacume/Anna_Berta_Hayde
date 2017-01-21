package upc.eet.pma.travelapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FriendActivity extends AppCompatActivity {
    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;
    private TextView user_emailuser;
    private String id_pos;
    private ToggleButton Follow;
    private  Button FollowBtn;
    private Button UnfollowBtn;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference friendsRef = mRef.child("Users").child("friendsList");
    private ArrayList<Category> userList;
    private String Clau;
    private Boolean ja_el_seguim;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        id_pos = getIntent().getExtras().getString("id_pos");
        final int id_ = Integer.parseInt(id_pos);
        userList = (ArrayList<Category>) getIntent().getSerializableExtra("userList");
        user_emailuser = (TextView) findViewById(R.id.useremail_txt);
        final String user_email = userList.get(id_).getName(); //user_email és l'email de l'amic seleccionat
        final String user_uid = userList.get(id_).getDescription(); //user_uid és l'uid de l'amic seleccionat
        user_emailuser.setText(user_email);

        Follow = (ToggleButton) findViewById(R.id.FollowBtn);
        FollowBtn = (Button) findViewById (R.id.Follow_Btn);
        UnfollowBtn = (Button) findViewById(R.id.Unfollow_Btn);

        // Mirem si el 'user_uid' està a User.currentUser.friendsList

        boolean ja_el_seguim = false;
        if (User.currentUser != null) {
            for (Map.Entry<String, Object> friend_uid : User.currentUser.friendsList.entrySet()) {
                if (friend_uid.equals(user_uid)) {
                    ja_el_seguim = true;
                }else {
                    ja_el_seguim = false;
                }
            }
        }

        if (ja_el_seguim!=true) {
            FollowBtn.setVisibility(View.VISIBLE);
            UnfollowBtn.setVisibility(View.GONE);
        }else {
            FollowBtn.setVisibility(View.GONE);
            UnfollowBtn.setVisibility(View.VISIBLE);
        }

        FollowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                follow(user_email, user_uid);
                Log.v("FOLLOW", "FOLLOW");
            }
        });


        UnfollowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopFollowing(user_email);
                Log.v("UNFOLLOW", "UNFOLLOW");
            }
        });



       /* Follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Botón encendido
                    follow(user_email, user_uid);
                    Toast.makeText(FriendActivity.this, String.format("Following '%s'", user_email), Toast.LENGTH_SHORT).show();
                } else {
                    // Botón apagado
                    stopFollowing(user_email);
                    Toast.makeText(FriendActivity.this, String.format("Unfollowing '%s'", user_email), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }


    //Agregar al contacto a tu lista de amigos
    public void follow(final String userEmail, final String userId){

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String currentuserId = user.getUid();
            String key = mRef.child("Users").push().getKey();
            Map<String, String> friendsList = new HashMap<>();
            friendsList.put("Uid_friend",userId);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Users/"+currentuserId+ "/friendsList/"+"/" + key, friendsList);
            mRef.updateChildren(childUpdates);

            Clau = key;

            FollowBtn.setVisibility(View.GONE);
            UnfollowBtn.setVisibility(View.VISIBLE);

            Log.v("Added", userEmail);
        }


    //Eliminar al contacto de tu lista de amigos
    public void stopFollowing(final String userEmail){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentuserId = user.getUid();

        String key2 = Clau;
        friendsRef.child(key2).removeValue();

        Map<String, String> friendsList = new HashMap<>();
        friendsList.remove(key2);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/"+currentuserId+ "/friendsList/"+"/" + key2, friendsList);
        mRef.updateChildren(childUpdates);

        FollowBtn.setVisibility(View.VISIBLE);
        UnfollowBtn.setVisibility(View.GONE);

        Log.v("Removed", userEmail);
    }

}
