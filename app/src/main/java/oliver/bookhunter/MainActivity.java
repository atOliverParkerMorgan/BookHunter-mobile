package oliver.bookhunter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Map;

import oliver.bookhunter.Home.HomeFragment;
import oliver.bookhunter.KeywordFragment.KeywordFragment;
import oliver.bookhunter.Login.Profile;
import oliver.bookhunter.Website.WebsiteFragment;



public class MainActivity extends AppCompatActivity {

    //change fragments
    private int mFragmentContainer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mSelectedFragment;

    //filenames
    private final String file_name = "bookhunter_file2";
    private final String file_name2 = "bookhunter_file";

    // profile object
    private Profile tempProfile;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_keywords:
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mSelectedFragment = new KeywordFragment();
                    mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    mFragmentTransaction.replace(mFragmentContainer, mSelectedFragment);
                    mFragmentTransaction.commit();


                    return true;
                case R.id.navigation_home:
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mSelectedFragment = new HomeFragment();
                    mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    mFragmentTransaction.replace(mFragmentContainer, mSelectedFragment);
                    mFragmentTransaction.commit();


                    return true;
                case R.id.navigation_website:
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mSelectedFragment = new WebsiteFragment();
                    mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    mFragmentTransaction.replace(mFragmentContainer, mSelectedFragment);
                    mFragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        //SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS,Context.MODE_PRIVATE);
        //final String name = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        //final String email = prefs.getString(LoginActivity.DISPLAY_EMAIL_KEY,null);
       // final String password = prefs.getString(LoginActivity.DISPLAY_PASSWORD_KEY,null);
       // Log.d("EMAIL",email);
       // Log.d("NAME",name);
       // Log.d("PASSWORD",password);
        //get firebase user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //get reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
       // tempProfile = new Profile(name,email,password);

        //build child
       // ref.child(user.getUid()).setValue(email);
        //grab info
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is my user_class Class

//                    Log.i("TEST", tempProfile.getEmail() + ": " + dataSnapshot.child(tempProfile.getEmail()).getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        mFragmentContainer = R.id.fragment_container;
        mFragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


       // ref = FirebaseDatabase.getInstance().getReference().child(tempProfile.getName());
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        //collectKeywords((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });




        mFragmentTransaction = mFragmentManager.beginTransaction();
        mSelectedFragment = new HomeFragment();
        mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        mFragmentTransaction.replace(mFragmentContainer, mSelectedFragment);
        mFragmentTransaction.commit();





    }


    private void UpdateAllFiles(){




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void collectKeywords(Map<String,Object> users) {

        ArrayList<String> keywords = new ArrayList<>();
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            keywords.add((String) singleUser.get("keyword"));


        }

        for (String keyword : keywords) {

            try {
                // GETTING stings out of file
                String Message;
                final FileInputStream fileinput = openFileInput(file_name);
                InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while (((Message = bufferedReader.readLine()) != null)) {
                    stringBuffer.append(Message + "\n");

                }
                final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
                String line = null;

                boolean infile = false;
                while ((line = bufReader.readLine()) != null) {
                    if (line.equals(keyword)) {
                        Log.d("infile", "true");
                        infile = true;


                    }


                }
                if (!infile) {
                    try {
                        Log.d("Adding", keyword);
                        FileOutputStream fileoutput = openFileOutput(file_name, Context.MODE_APPEND);
                        keyword += '\n';
                        fileoutput.write(keyword.getBytes());
                        fileoutput.close();


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // GETTING stings out of file
                String Message;
                final FileInputStream fileinput = openFileInput(file_name2);
                InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while (((Message = bufferedReader.readLine()) != null)) {
                    stringBuffer.append(Message + "\n");

                }
                final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
                String line = null;

                boolean infile = false;
                while ((line = bufReader.readLine()) != null) {
                    if (line.equals(keyword)) {
                        Log.d("infile", "true");
                        infile = true;


                    }


                }
                if (!infile) {
                    try {
                        Log.d("Adding", keyword);
                        FileOutputStream fileoutput = openFileOutput(file_name2, Context.MODE_APPEND);
                        keyword += '\n';
                        fileoutput.write(keyword.getBytes());
                        fileoutput.close();


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }
}
