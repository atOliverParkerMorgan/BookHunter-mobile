package oliver.bookhunter.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import oliver.bookhunter.Adapter.Adapter;
import oliver.bookhunter.Adapter.Item;
import oliver.bookhunter.Connect.Connect;
import oliver.bookhunter.Connect.ConnectAction;
import oliver.bookhunter.R;

public class HomeFragment extends Fragment {
    public static List<String> allWebsites;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button search = view.findViewById(R.id.Hunt);
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewHome);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ConnectAction runnable = (result, context) -> {
            List<String> allWebsites = new ArrayList<>();

            Iterator<String> keys;
            if (result != null) {
                keys = result.keys();
                while (keys.hasNext()) {
                    try {
                        allWebsites.add((String) result.get(keys.next()));
                    } catch (JSONException e) {
                        Toast.makeText(context,"Oops something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                HomeFragment.allWebsites = allWebsites;




            }
        };
        SharedPreferences userPreferences = Objects.requireNonNull(Objects.requireNonNull(getContext()).getSharedPreferences("credentials", android.content.Context.MODE_PRIVATE));

        search.setOnClickListener(v -> {

        });
        new Connect(getContext(), runnable,"getAllWebsites","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));



        return view;
    }



}
