package oliver.bookhunter.Login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;


public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:

    public static final String DISPLAY_EMAIL_KEY = "email";
    public static final String DISPLAY_PASSWORD_KEY = "password";

    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, Context.MODE_PRIVATE);
        String mEmail = prefs.getString(DISPLAY_EMAIL_KEY, null);
        String mPassword = prefs.getString(DISPLAY_PASSWORD_KEY,null);
        if (mEmail == null || mPassword == null) {
            setContentView(R.layout.activity_login);
            mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
            mPasswordView = (EditText) findViewById(R.id.login_password);

            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            // TODO: Grab an instance of FirebaseAuth

        } else {
            setContentView(R.layout.loading);

            mAuth.signInWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        dialog("Login attempt failed check your connection to the internet");
                    }else {
                        //SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS,0);
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                }
            });
        }
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        // TODO: Call attemptLogin() here
        attemptLogin();
    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        if(email.equals("") || password.equals("")) return;
        Toast.makeText(this,"Login in Progress",Toast.LENGTH_LONG).show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    dialog("Login attempt failed check if your email and password is correct or if you're connected to the internet");
                }else {
                    SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS,0);
                    prefs.edit().putString(DISPLAY_EMAIL_KEY,email).apply();
                    prefs.edit().putString(DISPLAY_PASSWORD_KEY,password).apply();
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });



        // TODO: Use FirebaseAuth to sign in with email & password



    }
    private void dialog(String messege){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(messege)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    // TODO: Show error on screen with an alert dialog



}