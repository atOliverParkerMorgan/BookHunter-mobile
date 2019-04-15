package oliver.bookhunter.Website;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import oliver.bookhunter.R;

public class WebsiteFragment extends Fragment {
    // the fragment view
    private View mDemoView;
    // the Website text
    private TextView mDemoTextView;

    private Button mSubmit;
    // add website text
    private EditText mWebsite_text;

    private String website;
    // file where websites are saved
    private final String file_name = "bookhunter_file";
    // the linear layout where the websites are shown
    private LinearLayout linearLayout;
    //list with all the buttons
    private List<Button> Alldeletebuttons;
    //list with all the text veiws
    private List<TextView> Allwebsitetext;

    //index for for loop
    private int index;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get main view
        mDemoView = inflater.inflate(R.layout.fragment_website, container, false);

        mSubmit = (Button) mDemoView.findViewById(R.id.submit);
        //input text
        mWebsite_text = (EditText) mDemoView.findViewById(R.id.website_new);
        //linearLayout were the website text is displayed
        linearLayout = (LinearLayout) mDemoView.findViewById(R.id.LinearLayout01);

        //Arrays
        Alldeletebuttons = new ArrayList<Button>();
        Allwebsitetext = new ArrayList<TextView>();
        index = 0;

        try {
            // GETTING stings out of file
            String Message;
            final FileInputStream fileinput = getContext().openFileInput(file_name);
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



                final Button command = new Button(getActivity());

                command.setId(Integer.parseInt(String.valueOf(index)));
                command.setText("delete");

                //command.setBackgroundResource(R.drawable.costum_button);
                command.setTextColor(Color.WHITE);
                command.setTextSize(14);

                index++;
                final TextView textView = new TextView(getActivity());
                Integer intInstance = new Integer(index);
                textView.setText(" "+intInstance.toString()+")  "+line);

                Alldeletebuttons.add(command);
                Allwebsitetext.add(textView);



                command.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        int id = command.getId();
                        TextView delete = Allwebsitetext.get(id);
                        Button delete2 = Alldeletebuttons.get(id);
                        linearLayout.removeView(delete2);
                        linearLayout.removeView(delete);
                        try {


                            //get file content
                            String Message;
                            final FileInputStream fileinput = getContext().openFileInput(file_name);
                            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            StringBuffer stringBuffer = new StringBuffer();
                            while (((Message = bufferedReader.readLine()) != null)) {
                                stringBuffer.append(Message + "\n");

                            }
                            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
                            String line = null;
                            FileOutputStream fileoutput = getContext().openFileOutput(file_name, Context.MODE_PRIVATE);
                            while ((line = bufReader.readLine()) != null) {
                                    Log.d("xxxtext",line);

                                    Log.d("getText",delete.getText().toString().substring(5));
                                    Log.d("False",Boolean.toString(line.equals(delete.getText().toString().substring(5))));

                                if(line.equals(delete.getText().toString().substring(5))) {
                                    Log.d("True","true");
                                }else {
                                    line += '\n';
                                    fileoutput.write(line.getBytes());
                                }




                            }
                            fileoutput.close();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                linearLayout.addView(textView);
                linearLayout.addView(command);


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
                website+='\n';
                try{
                    FileOutputStream fileoutput = getContext().openFileOutput(file_name,Context.MODE_APPEND);
                    fileoutput.write(website.getBytes());
                    fileoutput.close();
                    final TextView textView = new TextView(getActivity());
                    index++;
                    Integer intInstance = new Integer(index);

                    textView.setText(" "+intInstance.toString()+")  "+website);


                    final Button command = new Button(getActivity());
                    command.setId(Integer.parseInt(String.valueOf(Allwebsitetext.size())));
                    command.setText("delete");

                    //command.setBackgroundResource(R.drawable.costum_button);
                    command.setTextColor(Color.WHITE);
                    command.setTextSize(14);
                    Alldeletebuttons.add(command);
                    Allwebsitetext.add(textView);



                    command.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {


                            int id = command.getId();



                            TextView delete = Allwebsitetext.get(id);
                            Button delete2 = Alldeletebuttons.get(id);
                            linearLayout.removeView(delete2);
                            linearLayout.removeView(delete);
                            try {


                                //get file content
                                String Message;
                                final FileInputStream fileinput = getContext().openFileInput(file_name);
                                InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                StringBuffer stringBuffer = new StringBuffer();
                                while (((Message = bufferedReader.readLine()) != null)) {
                                    stringBuffer.append(Message + "\n");

                                }
                                final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
                                String line = null;
                                FileOutputStream fileoutput = getContext().openFileOutput(file_name, Context.MODE_PRIVATE);
                                while ((line = bufReader.readLine()) != null) {
                                    Log.d("xxxtext",line);

                                    Log.d("getText",delete.getText().toString().substring(5));
                                    Log.d("False",Boolean.toString(line.equals(delete.getText().toString().substring(5))));

                                    if(line.equals(delete.getText().toString().substring(5))) {
                                        Log.d("True","true");
                                    }else {
                                        line += '\n';
                                        fileoutput.write(line.getBytes());
                                    }




                                }
                                fileoutput.close();


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    linearLayout.addView(textView);
                    linearLayout.addView(command);



                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }





            }
        });

        return mDemoView;
    }


}
