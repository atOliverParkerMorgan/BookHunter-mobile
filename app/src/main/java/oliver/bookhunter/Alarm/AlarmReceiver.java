package oliver.bookhunter.Alarm;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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



    @Override
    public void onReceive(final Context arg0, Intent arg1) {

        // get a id for each notification
        Random rand = new Random();
        // Obtain a number between [0 - 49].
        final int id = rand.nextInt(999999999);


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
                                int wordindex = webpagecontent.toLowerCase().indexOf(key.toLowerCase())+key.length();
                                String indexofelement = webpagecontent.toLowerCase().substring(wordindex,wordindex+5);

                                String find = "Website: " + website + " Keyword: " + key+"#?# " + indexofelement;

                                //we got find => add it to the list
                                finds.add(find);
                            }
                        }





                    } catch (IOException e) {
                        e.printStackTrace();

                    }



                    // not internet / space error






                }


            }

        };


        //init alert
        alertnum = 0;




        // add to elements to show in Activity Show
        //getting allfinds from database
        final StringBuffer Alltext = new StringBuffer();


    }
}