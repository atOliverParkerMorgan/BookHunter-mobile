package oliver.bookhunter.Website;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class WebsiteFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static Adapter adapter;
    public static ConnectAction runnable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_website, container, false);
        SharedPreferences userPreferences = Objects.requireNonNull(Objects.requireNonNull(getContext()).getSharedPreferences("credentials", android.content.Context.MODE_PRIVATE));
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewWebsite);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        Button submit = view.findViewById(R.id.submit);
        EditText editText = view.findViewById(R.id.keyword_new);
        submit.setOnClickListener(v -> {
            String add = editText.getText().toString();
            submit.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            ConnectAction runnable = (result, context) -> {
                if(result!=null){
                    try {
                        if(result.getString("success").equals("true")){
                            submit.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                            update(getContext(), userPreferences, view);
                            editText.setText("");
                            Connect.Alert("Success",add +" has been successfully added", context, android.R.drawable.ic_menu_add);
                        }else{
                            Connect.Alert("Error","Oops something went wrong", context, android.R.drawable.ic_dialog_alert);
                        }
                    } catch (JSONException e) {
                        Connect.Alert("Error","Oops something went wrong", context, android.R.drawable.ic_dialog_alert);
                    }
                }


            };

            new Connect(getContext(), runnable,null,"addWebsite",add).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));

        });
        submit.setVisibility(View.INVISIBLE);

        runnable = (result, context) -> {
            List<Item> allWebsites = new ArrayList<>();
            adapter = new Adapter(allWebsites,false, context, userPreferences, "Website", view);
            recyclerView.setAdapter(adapter);
            Iterator<String> keys;
            if (result != null) {
                keys = result.keys();

                while (keys.hasNext()) {
                    try {
                        String s = (String) result.get(keys.next());
                        allWebsites.add(new Item(s, "website"));
                    } catch (JSONException e) {
                        Toast.makeText(context,"Oops something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                adapter = new Adapter(allWebsites,false, context, userPreferences, "Website",view);
                recyclerView.setAdapter(adapter);



            }
        };
        TextView textView = view.findViewById(R.id.Title);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnSearchClickListener(v -> textView.setVisibility(View.INVISIBLE));
        searchView.setOnCloseListener(() -> {
            textView.setVisibility(View.VISIBLE);
            new Connect(getContext(), runnable,null,"getAllWebsites","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
            return false;
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                new Connect(getContext(), runnable,null,"getFindWebsites",query).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        update(getContext(), userPreferences, view);

        return view;
    }

    public static void update(Context context, SharedPreferences userPreferences, View view){
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new Connect(context, runnable,null,"getAllWebsites","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
    }



}
