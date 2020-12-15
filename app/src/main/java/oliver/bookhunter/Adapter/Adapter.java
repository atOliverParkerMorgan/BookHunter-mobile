package oliver.bookhunter.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import oliver.bookhunter.Connect.Connect;
import oliver.bookhunter.R;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private List<Item> Items;
    private Activity mainActivity;

    // RecyclerView recyclerView;
    public Adapter(List<Item> listdata) {
       // this.mainActivity = mainActivity;

        this.Items = listdata;
        List<Integer> indexToAdd = new ArrayList<>();
        List<String> subjectsToAdd = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType==0) return new ViewHolder(layoutInflater.inflate(R.layout.item_edit, parent, false));
        return new ViewHolder(layoutInflater.inflate(R.layout.item_default, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (holder.getItemViewType() == 0) {
            holder.textView.setText(Items.get(position).getItemName());
        }else {

            holder.textView.setText(Items.get(position).getItemName());
            holder.imageButton.setOnClickListener(v -> {
                // logic
                Items.remove(Items.get(position));
                //new Connect(getContext(), runnable,"getFindWebsites",query).execute(userPreferences.getString("user", ""), userPreferences.getString("password", ""));
                notifyItemRangeChanged(position-1, Items.size());


            });
        }
    }


    @Override
    public int getItemCount() {
        return Items.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        TextView textView;
        RelativeLayout relativeLayout;
        ViewHolder(View itemView) {
            super(itemView);

            this.imageButton = itemView.findViewById(R.id.deleteImageButton);
            this.textView = itemView.findViewById(R.id.name);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
