package upc.eet.pma.travelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "";
    private Button meditBtn;
    private EditText meditFullName;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = mDatabase.child("Users");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        meditFullName = (EditText) findViewById(R.id.editFullName) ;
        usersRef.child(userId).child("full_name").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        String uName = dataSnapshot.getValue(String.class);
                        meditFullName.setText(uName);


                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                }
        );

        meditBtn =(Button) findViewById(R.id.editBtn);
        meditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newFullName = meditFullName.getText().toString();
                usersRef.child(userId).child("full_name").setValue(newFullName);
                onBackPressed();

            }
        });
    }
}
