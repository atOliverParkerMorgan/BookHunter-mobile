package oliver.bookhunter;

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

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "#bookshunter";

    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
    private final String file_name3 = "allfinds";
    private final String file_name4 = "newfinds";
    private List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();
    private boolean found = false;
    private List<String> finds = new ArrayList<>();
    private List<String> newfinds = new ArrayList<>();
    @Override
    public void onReceive(final Context arg0, Intent arg1) {
        Random rand = new Random();
        try {
            String Message;
            final FileInputStream fileinput = arg0.openFileInput(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {

                url.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String Message;
            final FileInputStream fileinput = arg0.openFileInput(file_name2);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {

                keywords.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // Obtain a number between [0 - 49].
        int id = rand.nextInt(999999999);


        // setting alarm
        Toast.makeText(arg0, "Going off", Toast.LENGTH_SHORT).show();
        Thread downloadThread = new Thread() {

            public void run() {

                for (String website : url) {

                    Document doc;

                    try {


                        String Message;
                        FileInputStream fileinput = arg0.openFileInput(file_name);
                        InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuffer stringBuffer = new StringBuffer();
                        while (((Message = bufferedReader.readLine()) != null)) {
                            stringBuffer.append(Message + "\n");

                        }
                        final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
                        String line = null;

                        while ((line = bufReader.readLine()) != null) {
                            doc = Jsoup.connect(website).timeout(60 * 10000).get();
                            String webpagecontent = doc.toString();



                            int index = webpagecontent.lastIndexOf("<style>");
                            int index2 = webpagecontent.lastIndexOf("</style>");
                            int index3 = webpagecontent.lastIndexOf("<script>");
                            int index4 = webpagecontent.lastIndexOf("</script>");
                            if (index == -1 || index2 == -1) {
                                Log.d("Error:", "no css on webpage");
                            } else {
                                webpagecontent = webpagecontent.substring(0, index) + webpagecontent.substring(index2);
                            }
                            if (index3 == -1 || index4 == -1) {
                                Log.d("Error:", "no javasrcript on webpage");
                            } else {
                                index3 = webpagecontent.lastIndexOf("<script>");
                                index4 = webpagecontent.lastIndexOf("</script>");
                                webpagecontent = webpagecontent.substring(0, index3) + webpagecontent.substring(index4);
                            }
                            webpagecontent = webpagecontent.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");

                            for (String key : keywords) {
                                if (webpagecontent.toLowerCase().contains(key.toLowerCase())) {
                                    String indexofelement = Integer.toString(webpagecontent.indexOf(key.toLowerCase()));
                                    String find = "Website: " + website + " Keyword: " + key+" #?# " + indexofelement;

                                    finds.add(find);
                                }
                            }
                            Log.d("FINDS",finds.toString());


                        }
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


        try {

            downloadThread.join();
        } catch (InterruptedException e) {
            Log.d("ERROR","ERROR");
            e.printStackTrace();
        }


        String Message;
        FileInputStream fileinput = null;
        try {

            FileOutputStream fileoutput = arg0.openFileOutput(file_name3, Context.MODE_APPEND);
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
            Log.d("In file",Alltext.toString());
            for(String element : finds) {

                Log.d("Element",element);
                if(!Alltext.toString().toLowerCase().contains(element.toLowerCase())){

                    newfinds.add(element.substring(0, element.indexOf("#?#")));
                    fileoutput.write(element.getBytes());


                }

            }
            fileoutput.close();

            FileOutputStream fileoutput2 = arg0.openFileOutput(file_name4, Context.MODE_PRIVATE);

            for(String element: newfinds){

                Log.d("Add","added");
                element+="\n";
                fileoutput2.write(element.getBytes());
            }
            fileoutput2.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }


        StringBuilder alert = new StringBuilder();
        try {

            // GETTING stings out of file
            Message = null;
            fileinput = arg0.openFileInput(file_name4);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message=bufferedReader.readLine())!=null)){
                stringBuffer.append(Message+"\n");


            }

            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;


            while( (line=bufReader.readLine()) != null ) {
                alert.append(line);
                alert.append("\n");



            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Intent intent = new Intent(arg0, FindsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(arg0, 0, intent, 0);
        NotificationCompat.Builder builder;
        if(alert.toString().length()>0) {
           builder = new NotificationCompat.Builder(arg0, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_check_box_black_24dp)
                    .setContentTitle("BookHunter")

                    .setContentText("Books found:")

                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(alert.toString()))

                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }else{
            builder = new NotificationCompat.Builder(arg0, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_close_black_24dp)
                    .setContentTitle("BookHunter")

                    .setContentText("No books found")



                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(arg0);
        Log.d("NOTYFITEXT",Integer.toString(newfinds.size()));
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(id, builder.build());


    }

}