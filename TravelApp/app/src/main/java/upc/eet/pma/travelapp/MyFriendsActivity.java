package upc.eet.pma.travelapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MyFriendsActivity extends AppCompatActivity {
    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;

    private ArrayList<UserChild> userList;
    private AdapterMyFriends adapter;
    private ListView mListView;

    private String friend_uid;
    private UserChild userChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
        }
        setContentView(R.layout.activity_myfriends);



        mListView = (ListView) findViewById(R.id.listViewMyFriends);

        MyDatabaseUtil.getDatabase(); //Per inicialitzar el Firebase
        usersDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentuserId = user.getUid();
        usersDatabaseReference = usersDatabase.getReference().child("Users").child(currentuserId).child("friendsList");
        addValueEventListener(usersDatabaseReference);

        userList = new ArrayList<>();

        for ( String key : User.currentUser.friendsList.keySet() ) {
            friend_uid = User.currentUser.friendsList.get(key).toString();
            friend_uid = friend_uid.substring(12);
            friend_uid = friend_uid.substring(0, friend_uid.length()-1);

            DatabaseReference usersRef = usersDatabase.getReference().child("Users");
            usersRef.child(friend_uid).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            if (dataSnapshot.getValue() != null){
                            String full_name = dataSnapshot.getValue(User.class).full_name;
                            String uLocation = dataSnapshot.getValue(User.class).ulocation;


                            userChild = new UserChild(full_name,uLocation);
                            userList.add(userChild);


                            adapter = new AdapterMyFriends(
                                    MyFriendsActivity.this,
                                    userList);
                            mListView.setAdapter(adapter);}}



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            }


    }


    private void addValueEventListener(final DatabaseReference userReference) {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot data = iterator.next();
                    Object Uid_friend = data.child("Uid_friend").getValue();

                    if (User.currentUser == null) {
                        User.currentUser = new User();
                    }
                    String S_UidFriend = Uid_friend.toString();

                    User.currentUser.friendsList.put("Uid_friend", S_UidFriend);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
