package oliver.bookhunter.Home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import oliver.bookhunter.FindsActivity;
import oliver.bookhunter.R;


public class HomeFragment extends Fragment {
    private View mDemoView;
    private Button mHunt;
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

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



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
        mDemoView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView)  mDemoView.findViewById(R.id.text);
        mHunt = (Button) mDemoView.findViewById(R.id.Hunt);
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
}
