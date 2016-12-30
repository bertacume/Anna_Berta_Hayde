package upc.eet.pma.travelapp;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity  {

    private FirebaseDatabase usersDatabase;
    private DatabaseReference usersDatabaseReference;
    private ArrayList userList;
    //private User value;

    private ArrayAdapter adapter;
    private  ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.listViewSearch);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        usersDatabase = FirebaseDatabase.getInstance();
        usersDatabaseReference = usersDatabase.getReference("Users");
        addValueEventListener(usersDatabaseReference);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                Toast.makeText(SearchActivity.this, String.format("'%s' Profile", userList.get(pos)), Toast.LENGTH_SHORT
                ).show();
                Intent i = new Intent("upc.eet.pma.travelapp.FriendActivity");
                String id_pos = Integer.toString(pos);
                /*Toast.makeText(SearchActivity.this, String.format("Has fet click a '%s'", id_pos), Toast.LENGTH_SHORT
                ).show();*/
                //String id_pos = userList.get(pos).toString();
                i.putExtra("id_pos", id_pos);
                i.putExtra("userList", userList);
                startActivity(i);
            }
        });

    }


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
                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void addValueEventListener(final DatabaseReference userReference){
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList = new ArrayList<>();
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while (iterator.hasNext()){
                    User value = iterator.next().getValue(User.class);
                    userList.add(value.email);
                }
                adapter = new ArrayAdapter<>(
                        SearchActivity.this,
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
