package oliver.bookhunter;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    private List<String> Keywords;
    private List<String> Websites;
    private String Uid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(List<String> Keywords,List<String> Websites, String Uid) {
        this.Websites = Websites;
        this.Keywords = Keywords;
        this.Uid = Uid;

    }

    public List<String> getKeywords() {
        return Keywords;
    }

    public List<String> getWebsites() {
        return Websites;
    }

    public String getUid() {
        return Uid;
    }
}
