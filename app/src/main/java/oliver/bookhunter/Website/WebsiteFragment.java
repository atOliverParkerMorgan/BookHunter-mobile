package oliver.bookhunter.Website;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.InputClass;
import oliver.bookhunter.Login.LoginActivity;
import oliver.bookhunter.Login.Profile;
import oliver.bookhunter.R;
import oliver.bookhunter.Login.RegisterActivity;

public class WebsiteFragment extends Fragment {
    // the fragment view
    private View mDemoView;
    // the Website text
    private TextView mDemoTextView;

    private Button mSubmit;
    // add website text
    private EditText mWebsite_text;

    private String website;
    // file where websites are saved
    private final String file_name = "bookhunter_file";
    // the linear layout where the websites are shown
    private LinearLayout linearLayout;

    //check if a website exist
    private boolean isawebsite;

    //all website
    private List<ItemData> itemsData ;
    //Recycle View
    private DatabaseReference mDatabseRefrence;


    // method if a website exist
    static public boolean isServerReachable(Context context,String url) {
        ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL urlServer = new URL(url);

                HttpURLConnection urlConn = (HttpURLConnection) urlServer.openConnection();
                urlConn.setConnectTimeout(3000);
                urlConn.connect();
                return urlConn.getResponseCode() == 200;
            } catch (MalformedURLException e1) {
                return false;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        //get main view
        mDemoView = inflater.inflate(R.layout.fragment_website, container, false);

        mSubmit = (Button) mDemoView.findViewById(R.id.submit);
        //input text
        mWebsite_text = (EditText) mDemoView.findViewById(R.id.website_new);
        //linearLayout were the website text is displayed


        //Arrays

        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = (RecyclerView) mDemoView.findViewById(R.id.RecyclerView01);

        // this is data fro recycler view
        itemsData = new ArrayList<ItemData>();

        try {

            // GETTING stings out of file
            String Message;
            final FileInputStream fileinput = getContext().openFileInput(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message=bufferedReader.readLine())!=null)){
                stringBuffer.append(Message+"\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;
            int index = 0;
            while( (line=bufReader.readLine()) != null ) {
                itemsData.add(new ItemData(line, R.drawable.ic_delete_black_24dp));

                index++;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        final MyAdapter mAdapter = new MyAdapter(itemsData,getActivity());
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());






        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website = mWebsite_text.getText().toString();




               Thread downloadThread = new Thread() {
                    public void run() {

                isawebsite = isServerReachable(getContext(),website);


                    }

                };
                downloadThread.start();


                try {
                    downloadThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(!isawebsite){
                    Toast.makeText(getActivity(), "Error not a website / not connected", Toast.LENGTH_LONG).show();
                }else{
                    mDatabseRefrence = FirebaseDatabase.getInstance().getReference();
                    SharedPreferences prefs = getActivity().getSharedPreferences(RegisterActivity.CHAT_PREFS,Context.MODE_PRIVATE);
                    final String name = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
                    final String email = prefs.getString(LoginActivity.DISPLAY_EMAIL_KEY,null);
                    final String password = prefs.getString(LoginActivity.DISPLAY_PASSWORD_KEY,null);

                    //get firebase user
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //get reference
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Profile tempProfile = new Profile(name,email,password);
                    InputClass input = new InputClass(website);
                    mDatabseRefrence.child(tempProfile.getEmail()).push().setValue(input);

                    itemsData.add(new ItemData(website, R.drawable.ic_delete_black_24dp));
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();

                    try {
                        FileOutputStream fileoutput = getContext().openFileOutput(file_name, Context.MODE_APPEND);
                        website+='\n';
                        fileoutput.write(website.getBytes());
                        fileoutput.close();


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }









            }
        });

        return mDemoView;
    }


}
