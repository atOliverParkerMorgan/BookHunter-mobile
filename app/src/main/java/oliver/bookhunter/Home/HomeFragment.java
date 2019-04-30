package oliver.bookhunter.Home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import oliver.bookhunter.Alarm.AlarmReceiver;
import oliver.bookhunter.R;


public class HomeFragment extends Fragment {
    //layout
    private View mDemoView;
    private Button mHunt;
    private TextView textView;


    private TextView User;

    //looking at websites
    private Document document;
    private final String file_name = "bookhunter_file";
    private final String file_name2 = "bookhunter_file2";
    private final String file_name3 = "allfinds";
    private final String file_name4 = "newfinds";
    private  List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();
    //finds
    private List<String> finds = new ArrayList<>();
    private List<String> newfinds = new ArrayList<>();

    //alarm
    private PendingIntent pendingIntent;
    private AlarmManager manager;

    //Account
    private FirebaseAuth mAuth;

    //database
    private DatabaseReference mDatabase;




    public void addtofile (int num){
        try {
            //add to file the item in the spinner
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final LayoutInflater minflater = inflater;
        final ViewGroup mcontainer = container;
        mDemoView = inflater.inflate(R.layout.fragment_home, container, false);

        //USER + DATABASE

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // Access a Cloud Firestore instance from your Activity
        //FirebaseFirestore db = FirebaseFirestore.getInstance();














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

            //format file
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
                //set the spinner to the saved value
                spinner.setAdapter(adapter);
                spinner.setSelection(pos);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            spinner.setAdapter(adapter);


        } catch (IOException e) {
            e.printStackTrace();
            spinner.setAdapter(adapter);


        }








        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String element = (String) parent.getItemAtPosition(position);
                // An item was selected. You can retrieve the selected item using
                if (element.equals("5 min")) {
                    addtofile(7);

                    setAlarm(300);
                }
                else if (element.equals("15 min")) {
                    addtofile(6);

                    setAlarm(900);
                } else if (element.equals("30 min")) {
                    addtofile(5);

                    setAlarm(1800);
                } else if (element.equals("1 h")) {
                    addtofile(4);

                    setAlarm(3600);
                } else if (element.equals("3 h")) {
                    addtofile(3);

                    setAlarm(10800);
                } else if (element.equals("6 h")) {
                    addtofile(2);

                    setAlarm(21600);
                } else if (element.equals("1 day")) {
                    addtofile(1);

                    setAlarm(86400);
                } else if (element.equals("1 week")) {
                    addtofile(0);

                    setAlarm(604800);
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });



        textView = (TextView)  mDemoView.findViewById(R.id.text);
        mHunt = (Button) mDemoView.findViewById(R.id.Hunt);

        mHunt.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Hunting...",Toast.LENGTH_LONG).show();
                getActivity().finish();
                startActivity(new Intent(getActivity(), FindsActivity.class));
            }

        });


        return mDemoView;
    }







    public void setAlarm(int interval){

        //get the current date
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        cal_alarm.setTime(dat);

        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = (PendingIntent) PendingIntent.getBroadcast(getActivity(),
                0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //set a alarm
        manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis()+interval*1000, interval * 1000, pendingIntent);






    }




}
