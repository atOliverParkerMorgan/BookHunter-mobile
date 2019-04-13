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
                            doc = Jsoup.connect("http://www.morganbooks.eu/").timeout(60 * 10000).get();

                            //String title = doc.title();
                            String link = doc.toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");





                            Log.d("link",link);
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
