package oliver.bookhunter.Connect;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public interface ConnectAction {

    void action(JSONObject result, Context context);

}
