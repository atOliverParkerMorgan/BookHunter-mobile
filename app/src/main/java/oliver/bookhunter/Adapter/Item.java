package oliver.bookhunter.Adapter;

import android.util.Log;

public class Item {
        private String itemName;
        private String websiteName;
        private boolean isInBag;

        public Item(String itemName, String websiteName) {
            this.websiteName = websiteName;
            this.itemName = itemName;
        }

        public String getWebsiteName() {
            return "   "+websiteName;
        }

        public String getItemName() {
            return "   "+itemName;
        }


    }