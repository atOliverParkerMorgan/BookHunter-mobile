package oliver.bookhunter.KeywordFragment;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import oliver.bookhunter.Adapter.Adapter;
import oliver.bookhunter.Adapter.Item;
import oliver.bookhunter.Connect.Connect;
import oliver.bookhunter.Connect.ConnectAction;
import oliver.bookhunter.MainActivity;
import oliver.bookhunter.R;

public class KeywordFragment extends Fragment {

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewKeyWords);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);

        ConnectAction runnable = (result, context) -> {
            List<Item> allWebsites = new ArrayList<>();
            recyclerView.setAdapter(new Adapter(allWebsites));
            Iterator<String> keys;
            if (result != null) {
                keys = result.keys();

                int index = 1;
                while (keys.hasNext()) {
                    try {
                        String s = (String) result.get(keys.next());
                        allWebsites.add(new Item(s));
                    } catch (JSONException e) {
                        Toast.makeText(context,"Oops something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    index++;
                }
                progressBar.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(new Adapter(allWebsites));



            }
        };

        SharedPreferences userPreferences = Objects.requireNonNull(Objects.requireNonNull(getContext()).getSharedPreferences("credentials", android.content.Context.MODE_PRIVATE));

        TextView textView = view.findViewById(R.id.Title);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnSearchClickListener(v -> {
            textView.setVisibility(View.INVISIBLE);
        });
        searchView.setOnCloseListener(() -> {
            textView.setVisibility(View.VISIBLE);
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                new Connect(getContext(), runnable,"getFindKeywords",query).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        new Connect(getContext(), runnable,"getAllKeywords","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));


        return view;
    }
}
