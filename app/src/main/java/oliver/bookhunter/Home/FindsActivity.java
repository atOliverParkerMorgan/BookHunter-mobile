package oliver.bookhunter.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import oliver.bookhunter.Database;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class FindsActivity extends AppCompatActivity {


    private List<Database>itemsData;

    //files
    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
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

    //toast error
    private boolean toast_error = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);

        percent = (TextView) findViewById(R.id.procent);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);

        // this is data for recycler view
        itemsData = new ArrayList<Database>();


        //getting all websites
        try {
            String Message;
            final FileInputStream fileinput = openFileInput(file_name);

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
                //adding file content into the url list
                url.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // getting all keywords
        try {
            String Message;
            final FileInputStream fileinput = openFileInput(file_name2);

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
                //adding file content into the keywords list
                keywords.add(line);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //look for books with hunt method

        Hunt(this);





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
                   toast_error = false;
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

                        Log.d("OG Content", webpagecontent);

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


                            // format into a file

                            try {
                                //get file with all finds
                                FileOutputStream fileoutput = openFileOutput(file_name3, Context.MODE_APPEND);
                                fileinput = openFileInput(file_name3);

                                //format file
                                inputStreamReader = new InputStreamReader(fileinput);
                                bufferedReader = new BufferedReader(inputStreamReader);
                                StringBuffer Alltext = new StringBuffer();
                                while (((Message = bufferedReader.readLine()) != null)) {
                                    Alltext.append(Message + "\n");


                                }



                                //check if the file is new or not
                                for(String element : finds) {

                                    if(!Alltext.toString().toLowerCase().contains(element.toLowerCase())){
                                        // add element to find file since it isn't new anymore
                                        fileoutput.write((element+'\n').getBytes());

                                        //format file with no index at the end
                                        String newfind = element.substring(0, element.indexOf("#?#"));

                                        //add it to the recycle viewer
                                        itemsData.add(new Database(newfind, R.drawable.ic_search_black_24dp));






                                    }

                                }
                                fileoutput.close();



                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context,"There as been an error",Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(context,"There as been an error",Toast.LENGTH_LONG).show();
                            }



                    // not internet / space error
                    } catch (FileNotFoundException e) {
                        Toast.makeText(context, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                    }

                    //add the percentage
                    currentpercent += onepercent;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //show toast error
                            if(toast_error){
                                Toast.makeText(context ,("Error: your device doesn't have enough memory to process: "+url+" (You may get keywords that aren't on the website)"),Toast.LENGTH_LONG).show();
                            }


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




                }


            }

        };
        //start the download
        downloadThread.start();



    }


}
