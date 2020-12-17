package oliver.bookhunter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Connect.Connect;
import oliver.bookhunter.R;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private final List<Item> Items;
    private final boolean showWebsiteTitle;
    private final Context context;
    private final SharedPreferences userPreferences;
    private final String removeName;


    // RecyclerView recyclerView;
    public Adapter(List<Item> listdata, View view, boolean showWebsiteTitle, Context context, SharedPreferences preferences, String removeName) {
        this.Items = listdata;
        this.context = context;
        this.userPreferences = preferences;
        this.removeName = removeName;
        this.showWebsiteTitle = showWebsiteTitle;

        List<Integer> indexToAdd = new ArrayList<>();
        List<String> websitesToAdd = new ArrayList<>();
        if(showWebsiteTitle) {
            // add title
            String currentWebsite = null;
            for (int i = 0; i < this.Items.size(); i++) {
                if (currentWebsite == null || !currentWebsite.equals(this.Items.get(i).getWebsiteName())) {
                    currentWebsite = this.Items.get(i).getWebsiteName();
                    indexToAdd.add(i);
                    websitesToAdd.add(currentWebsite);
                }
            }
            for (int i = 0; i < indexToAdd.size(); i++) {
                this.Items.add(indexToAdd.get(i) + i, new Item(websitesToAdd.get(i), null));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(showWebsiteTitle){
            if(viewType==0){
                return new ViewHolder(layoutInflater.inflate(R.layout.item_title, parent, false));
            }
        }
        if(removeName.equals("Website")||removeName.equals("Keyword")){
            return new ViewHolder(layoutInflater.inflate(R.layout.item_edit, parent, false));
        }

        return new ViewHolder(layoutInflater.inflate(R.layout.item_found, parent, false));

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder.getItemViewType() == 0) {
            holder.textView.setText(Items.get(position).getItemName());
        }else{
            holder.textView.setText(Items.get(position).getItemName());
            holder.imageButton.setOnClickListener(v -> {
                if(removeName.equals("Pass")){
                    goToUrl(Items.get(position).getWebsiteName());
                }else {

                    new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                            .setTitle("Delete " + removeName)
                            .setMessage("Are you sure you want to delete " + Items.get(position).getItemName() + "?")
                            .setOnCancelListener(dialog -> {
                            })
                            .setNegativeButton("Cancel", (result, context) -> {})
                            .setPositiveButton("Continue", (dialog, which) -> {
                                Items.remove(Items.get(position));
                                new Connect(context, (result, context) -> {
                                }, "remove" + removeName, Items.get(position).getItemName()).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
                                notifyItemRangeChanged(position - 1, Items.size());
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
    }

    public List<Item> getItems() {
        return Items;
    }

    public void add(Item item) {
        Items.add(item);
    }

    @Override
    public int getItemViewType(int position) {
        if(Items.get(position).getWebsiteName()==null){
            return 0;
        }
        return 1;
    }


    @Override
    public int getItemCount() {
        return Items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView textView;
        RelativeLayout relativeLayout;
        ViewHolder(View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.button);
            this.textView = itemView.findViewById(R.id.name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
    private void goToUrl (String url) {
        Log.d("website",url.substring(3));
        Uri uriUrl = Uri.parse(url.substring(3));
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }
}
