package upc.eet.pma.travelapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

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
        mAuth = FirebaseAuth.getInstance();

        mNewUserBtn = (Button) findViewById(R.id.newuserBtn);
        mName = (EditText) findViewById(R.id.nameTxt);
        mEmail = (EditText) findViewById(R.id.emailTxt);
        mCreatePassword = (EditText) findViewById(R.id.passwordtxt);
        mConfirmPassword = (EditText) findViewById(R.id.confirmtxt);
    }
}
