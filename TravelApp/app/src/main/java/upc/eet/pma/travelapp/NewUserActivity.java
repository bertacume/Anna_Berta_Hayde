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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserActivity extends AppCompatActivity {

    private Button mNewUserBtn;
    private EditText mName;
    private EditText mEmail;
    private EditText mCreatePassword;
    private EditText mConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mName = (EditText) findViewById(R.id.nameTxt);
        mEmail = (EditText) findViewById(R.id.emailTxt);
        mCreatePassword = (EditText) findViewById(R.id.passwordtxt);
        mConfirmPassword = (EditText) findViewById(R.id.confirmtxt);
        mAuth = FirebaseAuth.getInstance();

        mNewUserBtn = (Button) findViewById(R.id.createUserBtn);

        mNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistration();
            }
        });

    }
    private void startRegistration() {
        String email = mEmail.getText().toString();
        String password = mCreatePassword.getText().toString();
        String confirmpassword = mConfirmPassword.getText().toString();

        if(!password.equals(confirmpassword)){
            Toast.makeText(NewUserActivity.this, "Password don't match", Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(NewUserActivity.this, "Registration Problem", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}
