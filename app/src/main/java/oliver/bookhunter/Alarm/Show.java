package oliver.bookhunter.Alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import oliver.bookhunter.Home.MyAdapter3;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class Show extends AppCompatActivity {


    private List<Database>itemsData;

    //used files
    private final String file_name3 = "allfinds";
    private final String file_name4 = "show";


    //Progressbar (will be set to invisible)
    private ProgressBar bar;
    private TextView procent;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);


        procent = (TextView) findViewById(R.id.procent);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);

        // this is data for recycler view
        itemsData = new ArrayList<Database>();

        //getting rid of bar
        bar.setVisibility(View.GONE);
        procent.setText("");


        // going throw show file
        // and adding all element to find file
        try {
            String Message;
            FileOutputStream fileoutput = openFileOutput(file_name3, Context.MODE_APPEND);
            final FileInputStream fileinput = openFileInput(file_name4);

            //formatting
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while ((line = bufReader.readLine()) != null) {

                //add element to recycle viewr
                itemsData.add(new Database(line.substring(0,line.indexOf("#?#")), R.drawable.ic_search_black_24dp));
                fileoutput.write(line.getBytes());

                //this make sure that the element don't show again

            }
            fileoutput.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Stuff that updates the UI
        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView01);


        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final MyAdapter3 mAdapter = new MyAdapter3(itemsData,this);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 6. notify recycle viewer
        mAdapter.notifyDataSetChanged();




        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Show.this, MainActivity.class));
            }

        });









    }




}
