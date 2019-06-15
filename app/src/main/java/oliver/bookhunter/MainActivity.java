package oliver.bookhunter;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oliver.bookhunter.Home.HomeFragment;
import oliver.bookhunter.KeywordFragment.KeywordFragment;
import oliver.bookhunter.Website.WebsiteFragment;



public class MainActivity extends AppCompatActivity {

    //change fragments
    private int mFragmentContainer;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mSelectedFragment;


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


        //get reference
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
       // tempProfile = new Profile(name,email,password);

        //build child

        //grab info
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this is my user_class Class

//                    Log.i("TEST", tempProfile.getEmail() + ": " + dataSnapshot.child(tempProfile.getEmail()).getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        mFragmentContainer = R.id.fragment_container;
        mFragmentManager = getSupportFragmentManager();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





        mFragmentTransaction = mFragmentManager.beginTransaction();
        mSelectedFragment = new HomeFragment();
        mFragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        mFragmentTransaction.replace(mFragmentContainer, mSelectedFragment);
        mFragmentTransaction.commit();





    }





    



}
