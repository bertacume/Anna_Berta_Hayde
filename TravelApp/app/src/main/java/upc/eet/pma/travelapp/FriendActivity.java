package upc.eet.pma.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = mRef.child("Users");
    DatabaseReference friendsRef = mRef.child("users").child("friendsList");
    public Map<String,Boolean> friendsList = new TreeMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        id_pos = getIntent().getExtras().getString("id_pos");
        final int id_ = Integer.parseInt(id_pos);
        ArrayList<String> userList = (ArrayList<String>) getIntent().getSerializableExtra("userList");
        user_emailuser = (TextView) findViewById(R.id.useremail_txt);
        final String user_email = userList.get(id_);
        user_emailuser.setText(user_email);

        Follow = (ToggleButton) findViewById(R.id.FollowBtn);
        Follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Botón encendido
                    follow(user_email);
                    Toast.makeText(FriendActivity.this, String.format("Following '%s'", user_email), Toast.LENGTH_SHORT).show();
                } else {
                    // Botón apagado
                    stopFollowing(user_email);
                    Toast.makeText(FriendActivity.this, String.format("Unfollowing '%s'", user_email), Toast.LENGTH_SHORT).show();
                }
            }
        });}

  //Eliminar al contacto de tu lista de amigos
    public void stopFollowing(final String uidFriend){
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Aquí hi ha l'error
                friendsList.remove(uidFriend);
                Log.v("Removed", uidFriend);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    //Agregar al contacto a tu lista de amigos
    public void follow(final String uidFriend){
        friendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Aquí hi ha l'error
                friendsList.put(uidFriend,true);
                //friendsRef.push().setValue(uidFriend);
                Log.v("Added", uidFriend);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }}

//Coses que he provat
//final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//final String userId = user.getUid();
//usersRef.child("friendsList").push().setValue(user_email);
//mRef.child("users").child("friendsList");
//String key = mRef.child("Users").push().getKey();
//Map<String, Object> childUpdates = new HashMap<>();
//Map<String, Object> postValues = user.toMap();
// childUpdates.put("/Users/" + userId, postValues);
// childUpdates.put("/Users/"+myuserUid+"/friendsList/"+"/" + key, id_);
// childUpdates.put("/Users/" + myuserUid +"/friendsList/", id_);
//mRef.child("/Users/" + userId +"/friendsList/").push().setValue(user_email);
//mRef.updateChildren (childUpdates);
//mRef.push("/Users/"+ myuserUid + "/friendsList/").setValue(id_pos);
