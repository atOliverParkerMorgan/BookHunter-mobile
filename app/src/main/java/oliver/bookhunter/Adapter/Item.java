package oliver.bookhunter.Adapter;

import android.util.Log;

public class Item {
    private String itemName;
    private String showItem;

    public Item(String itemName) {
        Log.d("SHOW: ",itemName);
        this.itemName = itemName;
        this.showItem = itemName.replace("https://","");
        this.showItem = showItem.replace("http://","");
        this.showItem = showItem.length()>25?showItem.substring(0,25)+"...":showItem;
        Log.d("SHOW2: ",showItem);
    }


    public String getItemName() {

        return "   "+showItem;
    }

    public String getValue(){
        return itemName;
    }


}
