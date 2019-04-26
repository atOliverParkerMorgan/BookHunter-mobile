package oliver.bookhunter.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import oliver.bookhunter.MyAdapter3;
import oliver.bookhunter.R;

public class FindsActivity extends AppCompatActivity {


    private List<Database>itemsData;

    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
    private final String file_name3 = "allfinds";
    private final String file_name4 = "newfinds";
    private  List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    //finds
    private List<String> finds = new ArrayList<>();
    private List<String> newfinds = new ArrayList<>();

    //layout
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);
        // this is data fro recycler view
        itemsData = new ArrayList<Database>();


        //################## LOOKING FOR BOOKS ######################################

        //getting all websites
        try {
            String Message;
            final FileInputStream fileinput = openFileInput(file_name);
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
        // getting all keywords
        try {
            String Message;
            final FileInputStream fileinput = openFileInput(file_name2);
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
                for (String website : url) {
                    //get the html as doc
                    Document doc;

                    try {


                        String Message;
                        FileInputStream fileinput = openFileInput(file_name);
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

                            //remove css and javascript
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

                            //Find with keywords
                            for (String key : keywords) {
                                if (webpagecontent.toLowerCase().contains(key.toLowerCase())) {
                                    String indexofelement = Integer.toString(webpagecontent.indexOf(key.toLowerCase()));
                                    String find = "Website: " + website + " Keyword: " + key+"#?# " + indexofelement;

                                    finds.add(find);
                                }
                            }


                            // format into a file

                            try {
                                //get file with all finds
                                FileOutputStream fileoutput = openFileOutput(file_name3, Context.MODE_APPEND);
                                fileinput = openFileInput(file_name3);
                                inputStreamReader = new InputStreamReader(fileinput);
                                bufferedReader = new BufferedReader(inputStreamReader);
                                StringBuffer Alltext = new StringBuffer();
                                while (((Message = bufferedReader.readLine()) != null)) {
                                    Alltext.append(Message + "\n");
                                    Log.d("HERE",Alltext.toString());

                                }




                                for(String element : finds) {

                                    if(!Alltext.toString().toLowerCase().contains(element.toLowerCase())){

                                        fileoutput.write((element+'\n').getBytes());
                                        Log.d("Element",element.substring(element.indexOf("Keyword:")+9,element.indexOf("#?#")));
                                        newfinds.add(element.substring(0, element.indexOf("#?#")));
                                        String newfind = element.substring(0, element.indexOf("#?#"));
                                        Log.d("FINDS",newfind);
                                        itemsData.add(new Database(newfind, R.drawable.ic_search_black_24dp));
                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {

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
                                                bar.setVisibility(View.GONE);
                                            }
                                        });


                                    }

                                }
                                fileoutput.close();



                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Log.d("ERORR2","ERROR2");
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.d("ERORR","ERROR");
                            }





                        }
                    // not internet / space error
                    } catch (FileNotFoundException e) {
                        Toast.makeText(context, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                    }


                }


            }

        };
        //start the download
        downloadThread.start();


        //wait until it finishes downloading and finding books




    }


}
