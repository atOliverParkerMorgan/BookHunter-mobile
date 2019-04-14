package oliver.bookhunter.Home;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import oliver.bookhunter.R;

public class HomeFragment extends Fragment {
    private View mDemoView;
    private Button mHunt;
    private TextView textView;
    private Document document;
    private final String file_name = "bookhunter_file";
    private String[] allwebsites;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDemoView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView)  mDemoView.findViewById(R.id.text);
        mHunt = (Button) mDemoView.findViewById(R.id.Hunt);
        mHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url ="http://www.morganbooks.eu/";




                Thread downloadThread = new Thread() {
                    public void run() {
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
                                doc = Jsoup.connect(line).timeout(60 * 10000).get();
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


                                Log.d("link", webpagecontent);

                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }

                };
                downloadThread.start();
            }
        });

        return mDemoView;
    }
}
