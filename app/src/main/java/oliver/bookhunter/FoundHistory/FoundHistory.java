package oliver.bookhunter.FoundHistory;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

public class FoundHistory extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        RecyclerView recycler = view.findViewById(R.id.RecyclerViewFound);
        ProgressBar progressBar = view.findViewById(R.id.progressBarFound);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);

        SharedPreferences userPreferences = Objects.requireNonNull(Objects.requireNonNull(getContext()).getSharedPreferences("credentials", android.content.Context.MODE_PRIVATE));
        ConnectAction runnable = (result, context)->{
            List<Item> found = new ArrayList<>();
            Iterator<String> keys;
            keys = result.keys();

            String currentDate = "";
            String currentWebsite = "";
            String[] strings = new String[]{"","",""};
            int count = 0;
            while (keys.hasNext()) {

                strings[count] = keys.next();

                if (count == 2) {
                   try {
                       if (!currentDate.equals(result.get(strings[2]))) {
                           currentDate = result.get(strings[2]).toString();
                           found.add(new Item(  currentDate ,null));
                       }
                       if(!currentWebsite.equals(result.get(strings[0]))){
                           currentWebsite = result.get(strings[0]).toString();
                           found.add(new Item( null,currentWebsite));
                       }

                       found.add(new Item((String) result.get(strings[1]), currentWebsite));
                       count = 0;
                       strings = new String[]{"", "", ""};
                   }catch (JSONException e){
                      Connect.Alert("Error", "Oops something went wrong. Check your internet connection", context,  android.R.drawable.ic_dialog_alert);
                   }
                }else count++;

            }

            recycler.setAdapter(new Adapter(found,true, getContext(), userPreferences, "Pass"));
            progressBar.setVisibility(View.INVISIBLE);

        };
        new Connect(getContext(), runnable,"getAllFound","").execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));

        return view;
    }

    }