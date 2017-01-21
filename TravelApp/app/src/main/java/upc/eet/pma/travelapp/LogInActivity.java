package upc.eet.pma.travelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "";
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn,mNewUserBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mNewUserBtn=(Button) findViewById(R.id.newuserBtn);

        MyDatabaseUtil.getDatabase();


        //boton Log In -> starSignIn()
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignIn();
            }
        });

        //boton New User: Crea un nuevo intent : NewUserActivy
        mNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("upc.eet.pma.travelapp.NewUserActivity");
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    if (User.currentUser != null){
                        User.currentUser = new User();
                    }

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference userRef = mDatabase.child("Users");
                    String userId = user.getUid();

                    userRef.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String full_name = dataSnapshot.getValue(User.class).full_name;
                            String email = dataSnapshot.getValue(User.class).email;
                            String uLocation = dataSnapshot.getValue(User.class).ulocation;
                            Boolean isFantasma = dataSnapshot.getValue(User.class).isFantasma;
                            String Uid_ = dataSnapshot.getValue(User.class).Uid_;
                            Map friendsList = dataSnapshot.getValue(User.class).friendsList;
                            User.currentUser = new User(full_name,email,friendsList,uLocation,isFantasma,Uid_);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    // Firebase Current User Data saved into User.currentuser (static object)

                    // User is signed in
                    Intent i = new Intent("upc.eet.pma.travelapp.MapActivity");
                    startActivity(i);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        return;
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //comprueba que los datos sean correctos y realiza el signInWithEmailAndPassword si lo son
    private void startSignIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
            Toast.makeText(LogInActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        }
        else {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(LogInActivity.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                }



            }
        });


    }
    }
}
