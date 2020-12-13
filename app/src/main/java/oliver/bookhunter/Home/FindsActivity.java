package oliver.bookhunter.Home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Database;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class FindsActivity extends AppCompatActivity {


    private List<Database>itemsData;



    // the list of all the web pages and keywords
    private  List<String> url = new ArrayList<>();
    private  List<String> keywords = new ArrayList<>();

    //finds
    private List<String> finds = new ArrayList<>();

    //Progressbar + percent textview
    private ProgressBar bar;
    private TextView percent;
    private double onepercent;
    private double currentpercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.finds);



        //look for books with hunt method












    }
}
