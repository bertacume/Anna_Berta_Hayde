package upc.eet.pma.travelapp;

import android.app.ProgressDialog;
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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class NewUserActivity extends AppCompatActivity {

    private static final String TAG ="" ;
    private Button mNewUserBtn,mBackLog;
    private EditText mName;
    private EditText mEmail;
    private EditText mCreatePassword;
    private EditText mConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = mRef.child("Users");



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mName = (EditText) findViewById(R.id.nameTxt);
        mEmail = (EditText) findViewById(R.id.emailTxt);
        mCreatePassword = (EditText) findViewById(R.id.passwordtxt);
        mConfirmPassword = (EditText) findViewById(R.id.confirmtxt);
        mAuth = FirebaseAuth.getInstance();
        mNewUserBtn = (Button) findViewById(R.id.createUserBtn);
        mBackLog=(Button) findViewById(R.id.backLoginBtn);

        mBackLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveNewUser();

                startRegistration();

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    String userId=user.getUid();
                    String name = mName.getText().toString();
                    String email = mEmail.getText().toString();
                    String ulocation="";
                    Map<String,Object> friendList= new TreeMap<String,Object>();

                   // String friendsList="";
                    boolean isFantasma = false;

                    writeNewUser(userId,name,email,ulocation,isFantasma,friendList);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };
    }
    @Override
    public void onStart() {
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
    private void writeNewUser(String userId, String name,String email,String ulocation ,boolean isFantasma, Map<String,Object> friendList) {

        User user = new User(name,email,ulocation,isFantasma,friendList);
        mRef.child("Users").child(userId).setValue(user);

    }


    private void startRegistration() {
        String name = mName.getText().toString();
        String email = mEmail.getText().toString();
        String password = mCreatePassword.getText().toString();
        String confirmpassword = mConfirmPassword.getText().toString();

        if(!password.equals(confirmpassword)){
            Toast.makeText(NewUserActivity.this, "Passwords don't match", Toast.LENGTH_LONG).show();
        }
        if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)||TextUtils.isEmpty(confirmpassword)||TextUtils.isEmpty(name)){
            Toast.makeText(NewUserActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        }
        else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());


                    if (!task.isSuccessful()) {
                        Toast.makeText(NewUserActivity.this, "Registration Problem", Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(NewUserActivity.this, "You can log in now", Toast.LENGTH_LONG).show();

                }
            });


        }
    }
}
