package oliver.bookhunter.Alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oliver.bookhunter.R;

//this class handles the alarm
public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "#bookshunter";

    //a list for all the keywords and websites
    private List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    //the list of all the found websites with keywords in them
    private List<String> finds = new ArrayList<>();

    //string for the found element
    private StringBuilder alert;

    //number of found items
    private int alertnum;

    //user + database
    private FirebaseUser user;
    private FirebaseFirestore db;


    @Override
    public void onReceive(final Context arg0, Intent arg1) {

        // get a id for each notification
        Random rand = new Random();
        // Obtain a number between [0 - 49].
        final int id = rand.nextInt(999999999);

        //get all the websites from the file
        //getting data from database
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        Log.d("USer",user.getUid());







        final Thread downloadThread = new Thread() {

            public void run() {
                //go throw all websites

                for (String website : url) {

                    //get the html as doc
                    Document doc;


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

                                        for (String data : AllDATA) {
                                            Alltext.append(data + "\n");
                                        }






                                    }
                                }



                            }
                        });



                    } catch (IOException e) {
                        e.printStackTrace();

                    }



                    // not internet / space error






                }


            }

        };
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> AllDATA = (List<String>) document.get("keywords");
                        Log.d("ALLDATA",AllDATA.toString());
                        for (String data : AllDATA) {
                            keywords.add(data);
                        }

                        AllDATA = (List<String>) document.get("websites");

                        for (String data : AllDATA) {
                            url.add(data);
                        }
                        Log.d("WEBSITES",url.toString());
                        Log.d("KEYWORD",keywords.toString());
                        Log.d("finds",finds.toString());
                        downloadThread.start();
                        try {
                            downloadThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }else{
                        Log.d("Error","ERROR");
                    }
                }else{
                    Log.d("Error","ERROR");
                }
            }
        });



        //init alert
        alertnum = 0;




        // add to elements to show in Activity Show
        //getting allfinds from database
        final StringBuffer Alltext = new StringBuffer();

        docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> AllDATA = (List<String>) document.get("finds");
                        Log.d("HERE","HERE");
                        for(String data: AllDATA){
                            Alltext.append(data);
                        }

                        //check if the file is new or not
                        alert = new StringBuilder();
                        for(String element : finds) {

                            if(!Alltext.toString().toLowerCase().contains(element.toLowerCase())){
                                // add element to find file since it isn't new anymore
                                db.collection("users").document(user.getUid()).update("finds", FieldValue.arrayUnion(element));
                                db.collection("users").document(user.getUid()).update("show", FieldValue.arrayUnion(element));
                                alertnum++;
                                //format file with no index at the end
                                alert.append(element.substring(0, element.indexOf("#?#"))+'\n');


                            }

                        }
                        Log.d("ALLTEXT",Alltext.toString());



                        //Create notification

                        Intent intent = new Intent(arg0, Show.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
                        NotificationCompat.Builder builder;
                        Log.d("Alarmnum",Integer.toString(alertnum));
                        if (alertnum > 0) {
                            builder = new NotificationCompat.Builder(arg0, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_check_box_black_24dp)


                                    .setContentTitle(Integer.toString(alertnum)+" new books: ")

                                    .setContentText(alert.toString())


                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(arg0);

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(id, builder.build());


                        }

                    }

                }
            }
        });

    }
}