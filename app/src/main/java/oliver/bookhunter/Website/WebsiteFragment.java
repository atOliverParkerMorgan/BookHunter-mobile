package oliver.bookhunter.Website;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

import oliver.bookhunter.Login.GoogleSignInActivity;
import oliver.bookhunter.R;

public class WebsiteFragment extends Fragment {
    // the fragment view
    private View mDemoView;
    // the Website text
    private TextView mDemoTextView;

    private Button mSubmit;
    // add website text
    private EditText mWebsite_text;
    

    private String website;


    // the linear layout where the websites are shown
    private LinearLayout linearLayout;

    //check if a website exist
    private boolean isawebsite;

    //all website
    private List<ItemData> itemsData ;
    //Recycle View
    private DatabaseReference mDatabseRefrence;
    private MyAdapter mAdapter;

    //ALL DATA from database
    private List<String> AllDATA;


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

        //get user and database instance database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //getting data from data base
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        AllDATA = (List<String>) document.get("websites");
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
                                    public void run() {
                                        isawebsite = isServerReachable(getContext(), website);
                                        getActivity().runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {



                                                if(!isawebsite){

                                                    Toast.makeText(getActivity(), "Error not a website / not connected", Toast.LENGTH_LONG).show();

                                                }else{

                                                    mDatabseRefrence = FirebaseDatabase.getInstance().getReference();
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                    db.collection("users").document(user.getUid()).update("websites", FieldValue.arrayUnion(website));





                                                    //get reference
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

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
