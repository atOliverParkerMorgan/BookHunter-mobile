package oliver.bookhunter;

public class Database {
        private String title;
        private int imageUrl;

        public Database(String title,int imageUrl){

            this.title = title;
            this.imageUrl = imageUrl;
        }
        public String getTitle(){
            return this.title;
        }
        public int getImageUrl(){
            return this.imageUrl;
        }
        // getters & setters
    }

