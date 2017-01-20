package upc.eet.pma.travelapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class MyFriendsActivity extends AppCompatActivity {
    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;
    private ArrayList<String> userList;


    private ArrayAdapter adapter;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);

        mListView = (ListView) findViewById(R.id.listViewMyFriends);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        usersDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentuserId = user.getUid();

        usersDatabaseReference = usersDatabase.getReference().child("Users").child(currentuserId).child("friendsList");
        Log.v("1", usersDatabaseReference.toString());
        Log.v("2", usersDatabase.getReference().child("Users").child(currentuserId).child("friendsList").toString());
        Log.v("user", user.toString());
        Log.v("currentuserid", currentuserId);
        addValueEventListener(usersDatabaseReference);


        /*
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                Toast.makeText(MyFriendsActivity.this, String.format("'%s' Profile",
                        userList.get(pos).getName()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(SearchActivity.this, String.format("'%s' Profile", userList.get(pos).getDescription()), Toast.LENGTH_SHORT).show();

                Intent i = new Intent("upc.eet.pma.travelapp.FriendActivity");
                String id_pos = Integer.toString(pos);

                i.putExtra("id_pos", id_pos);
                i.putExtra("userList", userList);
                startActivity(i);


            }
        });*/

    }

    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //firebaseListAdapter.notifyDataSetChanged(newText);
                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    } */


    private void addValueEventListener(final DatabaseReference userReference) {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                //String key = friendsRef.getKey().toString();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot data = iterator.next();
                    //String key = mRef.child("Users").push().getKey();
                    Object Uid_friend = data.child("Uid_friend").getValue();
                    //Object Uid_ = data.child("Uid_").getValue();

                    // String S_Uid_ = Uid_.toString();

                    String S_UidFriend = Uid_friend.toString();
                    userList.add(S_UidFriend);
                    DatabaseReference usersRef = usersDatabase.getReference().child("Users");

                    usersRef.child(S_UidFriend).child("full_name").addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user value
                                            String uName = dataSnapshot.getValue(String.class);
                                            //mUserName = (TextView) findViewById(R.id.userNametxt);

                                            //mUserName.setText(uName);
                                            Log.v("FriendName", uName);

                                            // ...
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            // ...
                                        }
                                    }
                            );

                }
                adapter = new ArrayAdapter<>(
                        MyFriendsActivity.this,
                        android.R.layout.simple_list_item_1,
                        userList);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    }
