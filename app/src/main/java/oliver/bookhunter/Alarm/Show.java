package oliver.bookhunter.Alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Database;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class Show extends AppCompatActivity {


    private List<Database> itemsData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);

        TextView procent = findViewById(R.id.procent);
        //Progressbar (will be set to invisible)
        ProgressBar bar = findViewById(R.id.progressBar);
        ImageButton exit = findViewById(R.id.exit);

        // this is data for recycler view
        itemsData = new ArrayList<>();

        //getting rid of bar
        bar.setVisibility(View.GONE);
        procent.setText("");


        // going throw show file
        // and adding all element to find file
        //getting allfinds from database


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Show.this, MainActivity.class));
            }

        });


    }


}
