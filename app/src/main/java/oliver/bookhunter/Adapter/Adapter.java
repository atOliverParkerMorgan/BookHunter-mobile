package oliver.bookhunter.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import oliver.bookhunter.Connect.Connect;
import oliver.bookhunter.KeywordFragment.KeywordFragment;
import oliver.bookhunter.R;
import oliver.bookhunter.Website.WebsiteFragment;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private final List<Item> Items;
    private final boolean showWebsiteTitle;
    private final Context context;
    private final SharedPreferences userPreferences;
    private final String removeName;
    private final View view;


    // RecyclerView recyclerView;
    public Adapter(List<Item> listdata, boolean showWebsiteTitle, Context context, SharedPreferences preferences, String removeName, View view) {
        this.Items = listdata;
        this.context = context;
        this.userPreferences = preferences;
        this.removeName = removeName;
        this.showWebsiteTitle = showWebsiteTitle;
        this.view = view;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(showWebsiteTitle){
            if(viewType==0){
                return new ViewHolder(layoutInflater.inflate(R.layout.item_title_date, parent, false));
            }if(viewType==1){
                return new ViewHolder(layoutInflater.inflate(R.layout.item_title_website, parent, false));
            }
        }
        if(removeName.equals("Website")||removeName.equals("Keyword")){
            return new ViewHolder(layoutInflater.inflate(R.layout.item_delete, parent, false));
        }

        return new ViewHolder(layoutInflater.inflate(R.layout.item_found, parent, false));

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(removeName.equals("Website")||removeName.equals("Keyword")) {
            holder.imageButton.setOnClickListener(v -> new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setTitle("Delete " + removeName)
                    .setMessage("Are you sure you want to delete " + Items.get(position).getItemName() + "?")
                    .setOnCancelListener(dialog -> {
                    })
                    .setNegativeButton("Cancel", (result, context) -> {
                    })
                    .setPositiveButton("Continue", (dialog, which) -> {

                        new Connect(context, (result, context) -> {
                        },null, "remove" + removeName, Items.get(position).getItemName()).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
                        if(removeName.equals("Website")){
                            WebsiteFragment.update(context, userPreferences, view);
                        }else {
                           KeywordFragment.update(context,userPreferences, view);
                        }
                        notifyDataSetChanged();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show());
        }

        if (holder.getItemViewType() == 0) {
            holder.textView.setText(Items.get(position).getItemName());
        }else if( holder.getItemViewType() == 1){
            holder.textView.setText(Items.get(position).getWebsiteName());
        }else{
            holder.textView.setText(Items.get(position).getItemName());

            holder.imageButton.setOnClickListener(v -> {

                if(showWebsiteTitle) {
                    goToUrl(Items.get(position).getWebsiteName());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(Items.get(position).getWebsiteName()==null){
            return 0;
        }
        if(Items.get(position).getItemName()==null){
            return 1;
        }
        return 2;
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
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        context.startActivity(launchBrowser);
    }
}
