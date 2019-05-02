package oliver.bookhunter.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Database;
import oliver.bookhunter.Login.GoogleSignInActivity;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class FindsActivity extends AppCompatActivity {


    private List<Database>itemsData;

    //files

    private final String file_name3 = "allfinds";
    private final String file_name4 = "newfinds";

    // the list of all the web pages and keywords
    private  List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    //finds
    private List<String> finds = new ArrayList<>();
    private List<String> newfinds = new ArrayList<>();

    //Progressbar + percent textview
    private ProgressBar bar;
    private TextView percent;
    private double onepercent;
    private double currentpercent;

    //user + database
    private FirebaseUser user;
    private  FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);

        percent = (TextView) findViewById(R.id.procent);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);

        // this is data for recycler view
        itemsData = new ArrayList<Database>();

        //get user and database instance database
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        //getting data from database
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> AllDATA = (List<String>) document.get("keywords");

                        for(String data: AllDATA){
                            keywords.add(data);
                        }

                        AllDATA = (List<String>) document.get("websites");

                        for(String data: AllDATA){
                            url.add(data);
                        }
                        Hunt(getApplicationContext());


                    } else {
                        Alert("Oops","Something went wrong try restarting the app");
                    }
                } else {
                    Alert("Oops","Something went wrong try restarting the app");

                }
            }
        });


        //look for books with hunt method







        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindsActivity.this, MainActivity.class));
            }

        });









    }

    public void Hunt(final Context context){


        Thread downloadThread = new Thread() {

            public void run() {
                //go throw all websites

                //get the percentage progress

                //if there are zero web pages then there is no main loop and progress is finished
                if(url.size()==0){
                    currentpercent = 100.000;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            //UI format
                            percent.setText(String.format("%s%%",Double.toString(currentpercent).substring(0,Double.toString(currentpercent).indexOf("."))));
                            bar.setVisibility(View.GONE);

                        }
                    });





                }else {

                    //get the percentage to add after every loop (the end result is 100%)
                    onepercent =100.000/url.size();
                }

                //main loop
                for (String website : url) {

                    //get the html as doc
                    Document doc;

                    String Message;
                    FileInputStream fileinput;
                    InputStreamReader inputStreamReader;
                    BufferedReader bufferedReader;
                    StringBuffer stringBuffer;

                    try {
                        //connect to website and format
                        doc = Jsoup.connect(website).timeout(60 * 10000).get();
                        String webpagecontent = doc.toString();


                        //remove css and javascript
                        while (true) {
                            if(!webpagecontent.contains("<style")){
                                break;
                            }else {
                                webpagecontent = webpagecontent.replaceAll("(?s)<style.*?</style>", "");
                            }
                        }
                        while (true) {
                            if(!webpagecontent.contains("<script")) {
                                break;
                            }else {
                                webpagecontent = webpagecontent.replaceAll("(?s)<script.*?</script>", "");
                            }

                            // remove all tags from websites
                        }
                        webpagecontent = webpagecontent.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");

                            Log.d("WEBCONTENT",webpagecontent);
                            //go throw keywords
                            for (String key : keywords) {
                                //compare
                                if (webpagecontent.toLowerCase().contains(key.toLowerCase())) {
                                    String indexofelement = Integer.toString(webpagecontent.indexOf(key.toLowerCase()));
                                    String find = "Website: " + website + " Keyword: " + key+"#?# " + indexofelement;

                                    //we got find => add it to the list
                                    finds.add(find);
                                }
                            }


                                //getting allfinds from database
                                final StringBuffer Alltext = new StringBuffer();

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

                                                //check if the file is new or not
                                                for(String element : finds) {

                                                    if(!Alltext.toString().toLowerCase().contains(element.toLowerCase())){
                                                        // add element to find file since it isn't new anymore
                                                        db.collection("users").document(user.getUid()).update("finds", FieldValue.arrayUnion(element));

                                                        //format file with no index at the end
                                                        String newfind = element.substring(0, element.indexOf("#?#"));

                                                        //add it to the recycle viewer
                                                        itemsData.add(new Database(newfind, R.drawable.ic_search_black_24dp));








                                                    }

                                                }
                                                //add the percentage
                                                currentpercent += onepercent;
                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        //update percentage
                                                        percent.setText(String.format("%s%%",Double.toString(currentpercent).substring(0,Double.toString(currentpercent).indexOf("."))));

                                                        // Stuff that updates the UI
                                                        // 1. get a reference to recyclerView
                                                        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView01);


                                                        // 2. set layoutManger
                                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                                        // 3. create an adapter
                                                        final MyAdapter3 mAdapter = new MyAdapter3(itemsData,context);
                                                        // 4. set adapter
                                                        recyclerView.setAdapter(mAdapter);
                                                        // 5. set item animator to DefaultAnimator
                                                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                                                        mAdapter.notifyDataSetChanged();

                                                        //hide progress bar at 100%
                                                        if(currentpercent >=100) {


                                                            bar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });





                                            } else {
                                                Alert("ERROR","You're not logged into any account you try to login or create a new account");
                                            }
                                        } else {
                                            Alert("You're offline","You're not connected to the internet all of your saved websites should be saved after you connect to the internet");

                                        }
                                    }
                                });



                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(context,"There as been an error",Toast.LENGTH_LONG).show();
                            }



                    // not internet / space error






                }


            }

        };
        //start the download
        downloadThread.start();



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
