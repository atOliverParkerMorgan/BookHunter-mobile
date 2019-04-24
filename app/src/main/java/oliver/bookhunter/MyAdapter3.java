package oliver.bookhunter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter3 extends RecyclerView.Adapter<oliver.bookhunter.MyAdapter3.ViewHolder> {
    private List<Database> itemsData;
    public static Context context;



    public MyAdapter3(List<Database> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)


    @Override
    public oliver.bookhunter.MyAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                      int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.webistetextview, null);

        // create ViewHolder

        oliver.bookhunter.MyAdapter3.ViewHolder viewHolder = new oliver.bookhunter.MyAdapter3.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final oliver.bookhunter.MyAdapter3.ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(itemsData.get(position).getTitle());
        viewHolder.imgViewIcon.setImageResource(itemsData.get(position).getImageUrl());
        viewHolder.imgViewIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ListsDatabaseList theRemovedItem = itemsData.get(position);
                // remove your item from data base

                find(itemsData.get(position).getTitle());


            }
        });


    }



    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageButton imgViewIcon;
        public oliver.bookhunter.KeywordFragment.MyAdapter2.ViewHolder vimgViewIcon;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);


            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            imgViewIcon = (ImageButton) itemLayoutView.findViewById(R.id.item_icon);



        }
    }



    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public void find(String delete){
        String link = delete.substring(delete.indexOf("Website:"), delete.indexOf("Profile:"));
        link = link.substring(9);
        Log.d("Link",link);
        Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);











    }

}

