package oliver.bookhunter.Connect;

import android.content.Context;

import org.json.JSONObject;

public interface ConnectActionWebsite {
    void action(JSONObject result, Context context, String website);
}
