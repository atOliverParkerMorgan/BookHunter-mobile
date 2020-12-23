package oliver.bookhunter.Connect;

import android.content.Context;
import org.json.JSONObject;

public interface ConnectAction {

    void action(JSONObject result, Context context);

}
