package upc.eet.pma.travelapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendActivity extends AppCompatActivity {

    private TextView user_emailuser;
    private TextView user_nameuser;
    private TextView user_locationuser;
    private String user_email;
    private String user_uid;
    private  Button FollowBtn;
    private Button UnfollowBtn;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = mRef.child("Users");
    private ArrayList<UserChild> userList;
    private String Clau;
    private Boolean ja_el_seguim;
    private String Toast_follow;
    private String Toast_unfollow;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        user_email = getIntent().getExtras().getString("email");
        user_uid = getIntent().getExtras().getString("uid");

        user_emailuser = (TextView) findViewById(R.id.useremail_txt);
        user_nameuser = (TextView) findViewById(R.id.username_txt);
        //user_locationuser = (TextView) findViewById(R.id.userlocation_txt);

        Toast_follow = getString(R.string.Following);
        Toast_unfollow = getString(R.string.Unfollowing);


        user_emailuser.setText(user_email);

        //Agafem les dades de l'amic del firebase
        userRef.child(user_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user_name = dataSnapshot.getValue(User.class).full_name;
                String user_location = dataSnapshot.getValue(User.class).ulocation;
                user_nameuser.setText(user_name);
                //user_locationuser.setText(user_location);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        FollowBtn = (Button) findViewById (R.id.Follow_Btn);
        UnfollowBtn = (Button) findViewById(R.id.Unfollow_Btn);

        // Mirem si el 'user_uid' està a User.currentUser.friendsList
        ja_el_seguim = false;
        Clau = "";
        if (User.currentUser != null) {
            for ( String key : User.currentUser.friendsList.keySet() ) {
                String friend_uid = User.currentUser.friendsList.get(key).toString();
                //Afegim "{Uid_friend=" pk no sabem com obtenir el valor de la uid_friend del User.currentUser.friendList
                String user_uid_check = "{Uid_friend=" + user_uid + "}";
                if (friend_uid.equals(user_uid_check)) {
                    ja_el_seguim = true;
                    Clau = key;
            }}

        // Aquí s'hauria simplement de canviar el text d'un sol botó!
        // Es posa només un listener i llavors es decideix què es fa en funció de la situació...
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
                Toast.makeText(FriendActivity.this, Toast_follow + ": " + user_email, Toast.LENGTH_SHORT).show();
                Log.v("FOLLOW", "FOLLOW");
            }
        });


        UnfollowBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopFollowing(user_email, user_uid);
                Toast.makeText(FriendActivity.this, Toast_unfollow + ": " + user_email, Toast.LENGTH_SHORT).show();
                Log.v("UNFOLLOW", "UNFOLLOW");
            }
        });
    }
    }


    //Agregar al contacto a tu lista de amigos
    public void follow(final String userEmail, final String userId){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser ();
        final String currentuserId = user.getUid();

        String key = mRef.child("Users").push().getKey();

        Map<String, String> friendsList = new HashMap<>();

        friendsList.put("Uid_friend",userId);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/"+currentuserId+ "/friendsList/"+"/" + key, friendsList);
        mRef.updateChildren(childUpdates);

        // Aquí s'hauria simplement de canviar el text del botó!
        FollowBtn.setVisibility (View.GONE);
        UnfollowBtn.setVisibility (View.VISIBLE);

        Log.v("Added", userEmail);
        }


    //Eliminar al contacto de tu lista de amigos
    public void stopFollowing(final String userEmail, final String userId ){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentuserId = user.getUid();

        Map<String, String> friendsList = new HashMap<>();
        friendsList.remove(Clau);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/"+currentuserId+ "/friendsList/"+"/" + Clau, friendsList);
        mRef.updateChildren(childUpdates);

        // Aquí s'hauria simplement de canviar el text del botó!
        FollowBtn.setVisibility(View.VISIBLE);
        UnfollowBtn.setVisibility(View.GONE);

        Log.v("Removed", userEmail);
    }
    }