package oliver.bookhunter.Alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oliver.bookhunter.R;

//this class handles the alarm
public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "#bookshunter";

    //these are the files that the app uses
    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
    private final String file_name3 = "allfinds";
    private final String file_name4 = "show";

    //a list for all the keywords and websites
    private List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    //the list of all the found websites with keywords in them
    private List<String> finds = new ArrayList<>();

    //string for the found element
    private String alert;

    //number of found items
    private int alertnum;


    @Override
    public void onReceive(final Context arg0, Intent arg1) {

        // get a id for each notification
        Random rand = new Random();
        // Obtain a number between [0 - 49].
        int id = rand.nextInt(999999999);

        //get all the websites from the file
        try {
            String Message;
            final FileInputStream fileinput = arg0.openFileInput(file_name);

            //formatting file
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }

            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {
                //adding file content to list
                url.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get all the keywords from the file
        try {
            String Message;
            final FileInputStream fileinput = arg0.openFileInput(file_name2);

            //formatting file
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {
                //adding file content to list
                keywords.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        // setting alarm
        //Toast.makeText(arg0, "Going off", Toast.LENGTH_SHORT).show();

        Thread downloadThread = new Thread() {

            public void run() {
                //same code as in FinsActivity (find keywords on websites)
                for (String website : url) {

                    Document doc;

                    try {
                            doc = Jsoup.connect(website).timeout(60 * 10000).get();
                            String webpagecontent = doc.toString();


                        try {
                            boolean all_js_found = true;
                            while (all_js_found) {

                                int index = webpagecontent.indexOf("<style>");
                                int index2 = webpagecontent.indexOf("</style>");
                                //index = -1 mean it does isn't in doc
                                if (index == -1 || index2 == -1) {
                                    all_js_found = false;
                                } else {
                                    //remove it
                                    index = webpagecontent.indexOf("<style>");
                                    index2 = webpagecontent.indexOf("</style>");
                                    webpagecontent = webpagecontent.substring(0, index) + webpagecontent.substring(index2);
                                }

                            }
                        }catch (OutOfMemoryError e){
                            Log.d("ERROR",e.toString());
                        }try {

                            //repeat until all tags <style> are removed
                            boolean all_css_found = true;
                            while (all_css_found) {
                                int index3 = webpagecontent.indexOf("<script>");
                                int index4 = webpagecontent.indexOf("</script>");


                                if (index3 == -1 || index4 == -1) {
                                    all_css_found = false;
                                } else {
                                    //remove the script from website (if a var contains a key word)
                                    index3 = webpagecontent.indexOf("<script>");
                                    index4 = webpagecontent.indexOf("</script>");
                                    webpagecontent = webpagecontent.substring(0, index3) + webpagecontent.substring(index4);
                                }
                            }
                        }catch (OutOfMemoryError e){
                            Log.d("ERROR",e.toString());
                        }
                            webpagecontent = webpagecontent.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");

                            for (String key : keywords) {
                                if (webpagecontent.toLowerCase().contains(key.toLowerCase())) {
                                    String indexofelement = Integer.toString(webpagecontent.indexOf(key.toLowerCase()));
                                    String find = "Website: " + website + " Keyword: " + key + "#?# " + indexofelement;

                                    finds.add(find);
                                }
                            }
                            Log.d("FINDS", finds.toString());


                    } catch (FileNotFoundException e) {
                        Toast.makeText(arg0, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(arg0, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                    }


                }


            }

        };

        downloadThread.start();

        //init alert
        alertnum = 0;
        alert = "";

        //wait for download to finish
        try {

            downloadThread.join();
        } catch (InterruptedException e) {
            Log.d("ERROR", "ERROR");
            e.printStackTrace();
        }

        // define
        String Message;
        FileInputStream fileinput;

        // add to elements to show in Activity Show
        try {

            FileOutputStream fileoutput = arg0.openFileOutput(file_name4, Context.MODE_PRIVATE);
            fileinput = arg0.openFileInput(file_name3);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();


            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;
            StringBuffer Alltext = new StringBuffer();

            while ((line = bufReader.readLine()) != null) {
                Alltext.append(line);

            }
            // Alltext contains all the previous finds

            Log.d("In file", Alltext.toString());
            for (String element : finds) {

                //check if the element is a new find
                if (!Alltext.toString().toLowerCase().contains(element.toLowerCase())) {

                    //opening show file again and making sure that the element isn't already in it
                    FileInputStream fileinput2 = arg0.openFileInput(file_name4);
                    InputStreamReader inputStreamReader2 = new InputStreamReader(fileinput2);
                    BufferedReader bufferedReader2 = new BufferedReader(inputStreamReader2);
                    StringBuffer stringBuffer2 = new StringBuffer();


                    while (((Message = bufferedReader2.readLine()) != null)) {
                        stringBuffer2.append(Message + "\n");

                    }
                    final BufferedReader bufReader2 = new BufferedReader(new StringReader(stringBuffer2.toString()));
                    String line2 = null;
                    StringBuffer Alltext2 = new StringBuffer();

                    while ((line2 = bufReader2.readLine()) != null) {
                        Alltext2.append(line2);

                    }
                    // Alltext2 contains the show file
                    alertnum++;
                    alert+=element+"\n";


                    if(!Alltext2.toString().contains(alert)) {

                        //element isn't in file => add it
                        fileoutput.write(alert.getBytes());
                    }



                }

            }
            fileoutput.close();




        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }



        //Create notification

        Intent intent = new Intent(arg0, Show.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
        NotificationCompat.Builder builder;
        if (alertnum > 0) {
            builder = new NotificationCompat.Builder(arg0, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_check_box_black_24dp)


                    .setContentTitle(Integer.toString(alertnum)+" new books: ")

                    .setContentText(alert.substring(0,alert.indexOf("#?#")))


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