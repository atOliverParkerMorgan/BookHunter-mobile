package oliver.bookhunter.KeywordFragment;

import android.annotation.SuppressLint;
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

public class KeywordFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static Adapter adapter;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        SharedPreferences userPreferences = Objects.requireNonNull(Objects.requireNonNull(getContext()).getSharedPreferences("credentials", android.content.Context.MODE_PRIVATE));
        RecyclerView recyclerView = view.findViewById(R.id.RecyclerViewKeyWords);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        EditText editText = view.findViewById(R.id.keyword_new);
        Button submit = view.findViewById(R.id.submit);
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
                            adapter.add(new Item(add, null));
                            recyclerView.setAdapter(adapter);
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

            new Connect(getContext(), runnable,"addKeyword",add).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));

        });
        submit.setVisibility(View.INVISIBLE);

        ConnectAction runnable = (result, context) -> {
            List<Item> allWebsites = new ArrayList<>();
            adapter = new Adapter(allWebsites,view, false, context, userPreferences, "Keyword");
            recyclerView.setAdapter(adapter);
            Iterator<String> keys;
            if (result != null) {
                keys = result.keys();

                int index = 1;
                while (keys.hasNext()) {
                    try {
                        String s = (String) result.get(keys.next());
                        allWebsites.add(new Item(s, null));
                    } catch (JSONException e) {
                        Toast.makeText(context,"Oops something went wrong",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    index++;
                }
                progressBar.setVisibility(View.INVISIBLE);
                submit.setVisibility(View.VISIBLE);
                adapter = new Adapter(allWebsites,view, false, context, userPreferences, "Keyword");
                recyclerView.setAdapter(adapter);



            }
        };

        TextView textView = view.findViewById(R.id.Title);
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnSearchClickListener(v -> {
            textView.setVisibility(View.INVISIBLE);
        });
        searchView.setOnCloseListener(() -> {
            textView.setVisibility(View.VISIBLE);
            new Connect(getContext(), runnable,"getAllKeywords","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));

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
