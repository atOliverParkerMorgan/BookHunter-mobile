package oliver.bookhunter;

import android.annotation.SuppressLint;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;

import oliver.bookhunter.FoundHistory.FoundHistory;
import oliver.bookhunter.Home.HomeFragment;
import oliver.bookhunter.KeywordFragment.KeywordFragment;
import oliver.bookhunter.Website.WebsiteFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment,
                    new HomeFragment()).commit();
        }
        Serializable fragment = getIntent().getSerializableExtra("Fragment");
        if(fragment!=null) {
            if ("Search".equals(fragment)) {
                navView.setSelectedItemId(R.id.navigation_home);
                getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment,
                        new HomeFragment()).commit();
            } else if ("Website".equals(fragment)) {
                navView.setSelectedItemId(R.id.navigation_website);
                getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment,
                        new WebsiteFragment()).commit();
            } else if ("Keyword".equals(fragment)) {
                navView.setSelectedItemId(R.id.navigation_keywords);
                getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment,
                        new KeywordFragment()).commit();
            }else if ("History".equals(fragment)) {
                navView.setSelectedItemId(R.id.navigation_keywords);
                getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment,
                        new FoundHistory()).commit();
            }
        }


    }
    // navigation
    @SuppressLint("NonConstantResourceId")
    public BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {

                Fragment selectedFragment = null;
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.navigation_website:
                        selectedFragment = new WebsiteFragment();
                        break;
                    case R.id.navigation_keywords:
                        selectedFragment = new KeywordFragment();
                        break;

                    case R.id.navigation_history:
                        selectedFragment = new FoundHistory();
                        break;
                }

                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.HostFragment, selectedFragment).commit();
                return true;
            };

    @Override
    protected void onResume() {
        super.onResume();
    }

}
