package oliver.bookhunter;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    private List<String> Keywords;
    private List<String> Websites;
    private List<String> Finds;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(List<String> Keywords,List<String> Websites, List<String> Finds) {
        this.Websites = Websites;
        this.Keywords = Keywords;
        this.Finds = Finds;


    }

    public List<String> getKeywords() {
        return Keywords;
    }

    public List<String> getWebsites() {
        return Websites;
    }

    public List<String> getFinds() {
        return Finds;
    }
}
