package oliver.bookhunter.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import oliver.bookhunter.AlarmReceiver;
import oliver.bookhunter.FindsActivity;
import oliver.bookhunter.R;


public class HomeFragment extends Fragment {
    private View mDemoView;
    private Button mHunt;
    private ProgressBar mloading;
    private TextView textView;
    private Document document;
    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
    private final String file_name3 = "allfinds";
    private final String file_name4 = "newfinds";
    private  List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    private List<String> finds = new ArrayList<>();
    private List<String> newfinds = new ArrayList<>();

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    //private  Handler progressBarHandler = new Handler();

    private long fileSize = 0;

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    public void addtofile (int num){
        try {
            FileOutputStream fileoutput = getActivity().openFileOutput("spineritem", Context.MODE_PRIVATE);
            fileoutput.write(Integer.toString(num).getBytes());
            fileoutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDemoView = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner spinner = (Spinner) mDemoView.findViewById(R.id.time_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        try {
            String Message;
            final FileInputStream fileinput = getActivity().openFileInput("spineritem");
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {
                int pos = Integer.parseInt(line);
                spinner.setAdapter(adapter);
                spinner.setSelection(pos);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
        }








        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // An item was selected. You can retrieve the selected item using
                String element = (String) parent.getItemAtPosition(position);
                if (element.equals("5 min")) {
                    addtofile(1);
                    Toast.makeText(getActivity(),"hunting every 5 min",Toast.LENGTH_LONG).show();
                    setAlarm(300000);
                } else if (element.equals("15 min")) {
                    addtofile(2);
                    Toast.makeText(getActivity(),"hunting every 15 min",Toast.LENGTH_LONG).show();
                    setAlarm(900000);
                } else if (element.equals("30 min")) {
                    addtofile(3);
                    Toast.makeText(getActivity(),"hunting every 30 min",Toast.LENGTH_LONG).show();
                    setAlarm(1800000);
                } else if (element.equals("1 h")) {
                    addtofile(4);
                    Toast.makeText(getActivity(),"hunting every 1 h",Toast.LENGTH_LONG).show();
                    setAlarm(3600000);
                } else if (element.equals("3 h")) {
                    addtofile(5);
                    Toast.makeText(getActivity(),"hunting every 3 h",Toast.LENGTH_LONG).show();
                    setAlarm(10800000);
                } else if (element.equals("6 h")) {
                    addtofile(6);
                    Toast.makeText(getActivity(),"hunting every 6 h",Toast.LENGTH_LONG).show();
                    setAlarm(21600000);
                } else if (element.equals("24 h")) {
                    addtofile(7);
                    Toast.makeText(getActivity(),"hunting every 24 h",Toast.LENGTH_LONG).show();
                    setAlarm(86400000);
                } else if (element.equals("48 h")) {
                    addtofile(8);
                    Toast.makeText(getActivity(),"hunting every 48 h",Toast.LENGTH_LONG).show();
                    setAlarm(172800000);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });



        try {
            String Message;
            final FileInputStream fileinput = getActivity().openFileInput(file_name);
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
            final FileInputStream fileinput = getActivity().openFileInput(file_name2);
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

        textView = (TextView)  mDemoView.findViewById(R.id.text);
        mHunt = (Button) mDemoView.findViewById(R.id.Hunt);
        mloading = (ProgressBar)  mDemoView.findViewById(R.id.progressBar);
        mloading.setVisibility(View.INVISIBLE);
        mHunt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Thread downloadThread = new Thread() {

                    public void run() {

                        for (String website : url) {

                            Document doc;

                            try {


                                String Message;
                                FileInputStream fileinput = getContext().openFileInput(file_name);
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


                                }
                            } catch (FileNotFoundException e) {
                                Toast.makeText(getActivity(), "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "Error: no internet connection / no memory space", Toast.LENGTH_LONG).show();
                            }


                        }


                    }

                };

                downloadThread.start();
                mloading.setVisibility(View.VISIBLE);

                try {

                    downloadThread.join();
                } catch (InterruptedException e) {
                    Toast.makeText(getActivity(), "Please don't touch the screen", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                String Message;
                FileInputStream fileinput = null;
                try {

                    FileOutputStream fileoutput = getActivity().openFileOutput(file_name3, Context.MODE_APPEND);
                    fileinput = getActivity().openFileInput(file_name3);
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

                    FileOutputStream fileoutput2 = getActivity().openFileOutput(file_name4, Context.MODE_PRIVATE);
                    for(String element: newfinds){
                        Log.d("Add","added");
                        element+='\n';
                        fileoutput2.write(element.getBytes());
                    }
                    fileoutput2.close();



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.d("ERORR2","ERROR2");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("ERORR","ERROR");
                }

                Log.d("Size",Integer.toString(newfinds.size())+" "+Integer.toString(finds.size()));
                startActivity(new Intent(getActivity(), FindsActivity.class));
            }

        });


        return mDemoView;
    }
    public void setAlarm(int interval){

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarmIntent, 0);
        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Toast.makeText(getActivity(), "Alarm Set", Toast.LENGTH_SHORT).show();
    }



}
