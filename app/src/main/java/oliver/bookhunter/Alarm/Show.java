package oliver.bookhunter.Alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Database;
import oliver.bookhunter.Home.MyAdapter3;
import oliver.bookhunter.Login.BaseActivity;
import oliver.bookhunter.Login.GoogleSignInActivity;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class Show extends BaseActivity {


    private List<Database>itemsData;



    //Progressbar (will be set to invisible)
    private ProgressBar bar;
    private TextView procent;

    //user + database
    private FirebaseUser user;
    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);

        showProgressDialog();
        procent = (TextView) findViewById(R.id.procent);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);

        // this is data for recycler view
        itemsData = new ArrayList<Database>();

        //getting rid of bar
        bar.setVisibility(View.GONE);
        procent.setText("");


        // going throw show file
        // and adding all element to find file
        //getting allfinds from database
        final StringBuffer Alltext = new StringBuffer();


        //get user and database instance database
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> AllDATA = (List<String>) document.get("finds");

                        for(String data: AllDATA){
                            Alltext.append(data+"\n");
                        }
                        AllDATA = (List<String>) document.get("show");

                        hideProgressDialog();
                        //check if the file is new or not
                        for(String element : AllDATA) {


                                // add element to find file since it isn't new anymore
                                db.collection("users").document(user.getUid()).update("show", FieldValue.arrayRemove(element));

                                //format file with no index at the end
                                String newfind = element.substring(0, element.indexOf("#?#"));

                                //add it to the recycle viewer
                                itemsData.add(new Database(newfind, R.drawable.ic_search_black_24dp));
                                db.collection("users").document(user.getUid()).update("finds", FieldValue.arrayUnion(element));
                                // Stuff that updates the UI
                                // 1. get a reference to recyclerView
                                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView01);


                                // 2. set layoutManger
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                // 3. create an adapter
                                final MyAdapter3 mAdapter = new MyAdapter3(itemsData,getApplicationContext());
                                // 4. set adapter
                                recyclerView.setAdapter(mAdapter);
                                // 5. set item animator to DefaultAnimator
                                recyclerView.setItemAnimator(new DefaultItemAnimator());

                                mAdapter.notifyDataSetChanged();





                        }



                    } else {
                        Alert("ERROR","You're not logged into any account you try to login or create a new account");
                    }
                } else {
                    Alert("You're offline","You're not connected to the internet all of your saved websites should be saved after you connect to the internet");

                }
            }
        });




        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Show.this, MainActivity.class));
            }

        });









    }


    private void Alert(String Title,String text){
        new AlertDialog.Builder(this)
                .setTitle(Title)
                .setMessage(text)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        startActivity(new Intent(getApplicationContext() , GoogleSignInActivity.class));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(R.string.continue1, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }




}
