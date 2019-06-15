package oliver.bookhunter.Website;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import oliver.bookhunter.Login.GoogleSignInActivity;
import oliver.bookhunter.R;

public class WebsiteFragment extends Fragment {

    private Button mSubmit;
    // add website text
    private EditText mWebsite_text;
    

    private String website;


    //check if a website exist
    private boolean isawebsite;

    //all website
    private List<ItemData> itemsData ;
    private MyAdapter mAdapter;

    //ALL DATA from database
    private List<String> AllDATA;

    public WebsiteFragment() {
        // the Website text
    }


    // method if a website exist
    private static boolean isServerReachable(Context context, String url) {
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
        // the fragment view
        View mDemoView = inflater.inflate(R.layout.fragment_website, container, false);

        mSubmit = mDemoView.findViewById(R.id.submit);
        //input text
        mWebsite_text = mDemoView.findViewById(R.id.website_new);
        //linearLayout were the website text is displayed



        //Arrays

        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = mDemoView.findViewById(R.id.RecyclerView01);

        // this is data fro recycler view
        itemsData = new ArrayList<>();

        //get user and database instance database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //getting data from data base
        assert user != null;
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        AllDATA = (List<String>) document.get("websites");
                        assert AllDATA != null;
                        Log.d("ALLDATA",AllDATA.toString());
                        for(String data: AllDATA){
                            itemsData.add(new ItemData(data, R.drawable.ic_delete_black_24dp));
                        }
                        // 2. set layoutManger
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        // 3. create an adapter
                        mAdapter = new MyAdapter(itemsData,getActivity());
                        // 4. set adapter
                        recyclerView.setAdapter(mAdapter);
                        // 5. set item animator to DefaultAnimator
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        mSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                website = mWebsite_text.getText().toString();


                                Thread downloadThread = new Thread() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    public void run() {
                                        isawebsite = isServerReachable(Objects.requireNonNull(getContext()), website);
                                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {



                                                if(!isawebsite){

                                                    Toast.makeText(getActivity(), "Error not a website / not connected", Toast.LENGTH_LONG).show();

                                                }else{

                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                    assert user != null;
                                                    db.collection("users").document(user.getUid()).update("websites", FieldValue.arrayUnion(website));





                                                    //get reference

                                                    itemsData.add(new ItemData(website, R.drawable.ic_delete_black_24dp));
                                                    mAdapter.notifyDataSetChanged();
                                                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });




                                    }

                                };

                                downloadThread.start();

                                try {
                                    downloadThread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }
                        });




                    } else {
                        Alert("ERROR","You're not logged into any account you should try to login or create a new account");
                    }
                } else {
                    Alert("You're offline","You're not connected to the internet all of your saved websites should be saved after you connect to the internet");

                }
            }
        });






        return mDemoView;
    }
    private void Alert(String Title,String text){
        new AlertDialog.Builder(getActivity())
                .setTitle(Title)
                .setMessage(text)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        startActivity(new Intent(getActivity(), GoogleSignInActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(R.string.continue1, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


}
