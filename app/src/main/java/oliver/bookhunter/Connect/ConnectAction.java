package oliver.bookhunter.Connect;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public interface ConnectAction {

    void login(JSONObject result,Context context);
}
