package oliver.bookhunter.FindsFragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import oliver.bookhunter.KeywordFragment.ItemData2;
import oliver.bookhunter.R;

public class MyAdapter4 extends RecyclerView.Adapter<MyAdapter4.ViewHolder> {
    private List<ItemData2> itemsData;
    public static Context context;



    public MyAdapter4(List<ItemData2> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)


    @Override
    public MyAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent,
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
                Toast.makeText(context,"removed",Toast.LENGTH_SHORT).show();
                delete(itemsData.get(position).getTitle());

                //get user and database instance database
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                // Delete the from database
                db.collection("users").document(user.getUid()).update("keywords", FieldValue.arrayRemove(itemsData.get(position).getTitle()));

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
        String file_name = "bookhunter_file2";
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
