package oliver.bookhunter.Website;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.BufferOverflowException;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import oliver.bookhunter.R;

public class WebsiteFragment extends Fragment {
    private View mDemoView;
    private TextView mDemoTextView;
    private Button mSubmit;
    private EditText mWebsite_text;
    private String website;
    private final String file_name = "bookhunter_file";
    private LinearLayout linearLayout;
    private int index;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDemoView = inflater.inflate(R.layout.fragment_website, container, false);
        mSubmit = (Button) mDemoView.findViewById(R.id.submit);
        mWebsite_text = (EditText) mDemoView.findViewById(R.id.website_new);
        linearLayout = (LinearLayout) mDemoView.findViewById(R.id.LinearLayout01);
        index = 0;

        try {
            String Message;
            FileInputStream fileinput = getContext().openFileInput(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message=bufferedReader.readLine())!=null)){
                stringBuffer.append(Message+"\n");
            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while( (line=bufReader.readLine()) != null )
            {
                index++;
                final TextView textView = new TextView(getActivity());
                Integer intInstance = new Integer(index);
                textView.setText(" "+intInstance.toString()+")  "+line);
                linearLayout.addView(textView);


            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                website = mWebsite_text.getText().toString();

                try{
                    FileOutputStream fileoutput = getContext().openFileOutput(file_name,Context.MODE_APPEND);
                    fileoutput.write(website.getBytes());
                    fileoutput.close();
                    TextView textView = new TextView(getActivity());
                    index++;
                    Integer intInstance = new Integer(index);

                    textView.setText(" "+intInstance.toString()+")  "+website);
                    linearLayout.addView(textView);

                    Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }




            }
        });
        return mDemoView;
    }


}
