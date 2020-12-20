package oliver.bookhunter.Adapter;


public class Item {
        private final String itemName;
        private final String websiteName;


        public Item(String itemName, String websiteName) {
            this.websiteName = websiteName;
            this.itemName = itemName;

        }

        public String getWebsiteName() {
            return websiteName;
        }

        public String getItemName() {

            return itemName;
        }

}