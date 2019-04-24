package oliver.bookhunter.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;


public class RegisterActivity extends AppCompatActivity {

    // Constants
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "username";





    // TODO: Add member variables here:
    private FirebaseAuth mAuth;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private String displayName;
    private String email;
    private String password;

    // Firebase instance variables



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);


        // Keyboard sign in action
        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }
                return false;
            }
        });

        ImageButton mExit = (ImageButton) findViewById(R.id.exit);
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        // TODO: Get hold of an instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

    }

    // Executed when Sign Up button is pressed.
    public void signUp(View v) {
        attemptRegistration();
    }

    private void attemptRegistration() {

        // Reset errors displayed in the form.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String user = mUsernameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(user) || !isUsernameValid(user)) {
            mUsernameView.setError(getString(R.string.usererror));
            focusView = mUsernameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFirebaseUser();

        }
    }
    private void saveAll(){
        displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS,0);
        prefs.edit().putString(DISPLAY_NAME_KEY,displayName).apply();
        prefs.edit().putString(LoginActivity.DISPLAY_EMAIL_KEY,email).apply();
        prefs.edit().putString(LoginActivity.DISPLAY_PASSWORD_KEY,password).apply();

    }

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@");
    }
    private boolean isUsernameValid(String user) {
    return user.length()>5 && user.length()<15;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPasswordView.getText().toString();

        return confirmPassword.equals(password) && password.length() > 4;
    }

    // TODO: Create a Firebase user
    private  void createFirebaseUser(){
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    dialog("Registration attempt failed check if your connection to the internet");
                }else {
                    saveAll();
                    Profile profile = new Profile(email,password,displayName);
                    AddtoDataBase(profile);
                    login(email,password);

                }
            }
        });
    }

    // TODO: Save the display name to Shared Preferences


    // TODO: Create an alert dialog to show in case registration failed
    private void dialog(String messege){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(messege)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    private void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    dialog("Login attempt failed check your connection to the internet");
                }else {

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    finish();
                    startActivity(i);
                }
            }
        });
    }
    public void AddtoDataBase(Profile profile){
       DatabaseReference mDatabseRefrence =  FirebaseDatabase.getInstance().getReference();
       mDatabseRefrence.child(profile.getEmail()).push().setValue(profile.getPassword());

    }



}
