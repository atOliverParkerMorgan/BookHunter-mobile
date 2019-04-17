package oliver.bookhunter.Website;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import oliver.bookhunter.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ItemData> itemsData;
    public static Context context;



    public MyAdapter(List<ItemData> itemsData,Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.webistetextview, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(itemsData.get(position).getTitle());
        viewHolder.imgViewIcon.setImageResource(itemsData.get(position).getImageUrl());
        viewHolder.imgViewIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // ListsDatabaseList theRemovedItem = itemsData.get(position);
                // remove your item from data base
                Log.d("Index",Integer.toString(position));
                delete(itemsData.get(position).getTitle());
                itemsData.remove(position);  // remove the item from list
                notifyItemRemoved(position); // notify the adapter about the removed item


            }
        });


    }



    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageButton imgViewIcon;
        public ViewHolder vimgViewIcon;


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
    public void delete(String delete){
        String file_name = "bookhunter_file";
        this.notifyDataSetChanged();

        try {
            String Message;
            final FileInputStream fileinput = context.openFileInput(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(fileinput);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while (((Message = bufferedReader.readLine()) != null)) {
                stringBuffer.append(Message + "\n");

            }
            final BufferedReader bufReader = new BufferedReader(new StringReader(stringBuffer.toString()));
            String line = null;
            FileOutputStream fileoutput = context.openFileOutput(file_name, Context.MODE_PRIVATE);
            while ((line = bufReader.readLine()) != null) {
                Log.d("text", line);
                Log.d("dele",delete);


                if (line.equals(delete)) {
                    Log.d("True", "true");
                } else {
                    line += '\n';
                    fileoutput.write(line.getBytes());
                }


                fileoutput.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }








    }

}
