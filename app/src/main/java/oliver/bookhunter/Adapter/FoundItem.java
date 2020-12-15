package oliver.bookhunter.Adapter;

import android.util.Log;

public class FoundItem {
    private String itemName;
    private String showItem;
    private String website;

    public FoundItem(String itemName, String website) {
        this.website = website;
        this.itemName = itemName;
        this.showItem = itemName.replace("https://","");
        this.showItem = showItem.replace("http://","");
        this.showItem = showItem.length()>25?showItem.substring(0,25)+"...":showItem;

    }

    public String getShowItem() {
        return showItem;
    }

    public String getWebsite() {
        return website;
    }

    public String getItemName() {

        return "   "+showItem;
    }

    public String getValue(){
        return itemName;
    }
}
