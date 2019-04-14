package oliver.bookhunter.Home;

import java.io.IOException;
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

                            doc = Jsoup.connect("http://www.morganbooks.eu/1940-1947").timeout(60 * 10000).get();
                            String webpagecontent = doc.toString();


                            int index = webpagecontent.lastIndexOf("<style>");
                            int index2 = webpagecontent.lastIndexOf("</style>");
                            int index3 = webpagecontent.lastIndexOf("<script>");
                            int index4 = webpagecontent.lastIndexOf("</script>");
                            if(index==-1   || index2==-1) {
                                Log.d("Error:","no css on webpage");
                            }else {
                                webpagecontent = webpagecontent.substring(0, index) + webpagecontent.substring(index2);
                            }
                            if(index3==-1   || index4==-1) {
                               Log.d("Error:","no javasrcript on webpage");
                            }else {
                                index3 = webpagecontent.lastIndexOf("<script>");
                                index4 = webpagecontent.lastIndexOf("</script>");
                               webpagecontent = webpagecontent.substring(0, index3) + webpagecontent.substring(index4);
                            }
                            webpagecontent = webpagecontent.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");

 


                            Log.d("link",webpagecontent);
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
