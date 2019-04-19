package oliver.bookhunter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class FindsActivity extends AppCompatActivity {


    private List<Database>itemsData;
    private final String file_name4 = "newfinds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);
        ImageButton exit = (ImageButton) findViewById(R.id.exit);
        // 1. get a reference to recyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView01);

        // this is data fro recycler view
        itemsData = new ArrayList<Database>();

        try {
            // GETTING stings out of file
            String Message;
            final FileInputStream fileinput = openFileInput(file_name4);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message=bufferedReader.readLine())!=null)){
                stringBuffer.append(Message+"\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;

            while( (line=bufReader.readLine()) != null ) {
                itemsData.add(new Database(line, R.drawable.ic_search_black_24dp));


            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final MyAdapter3 mAdapter = new MyAdapter3(itemsData,this);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindsActivity.this, MainActivity.class));
            }

        });








    }

}
