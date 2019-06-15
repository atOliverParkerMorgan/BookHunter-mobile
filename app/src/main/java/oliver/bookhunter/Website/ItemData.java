package oliver.bookhunter.Website;

public class ItemData {


    private String title;
    private int imageUrl;

    ItemData(String title, int imageUrl){

        this.title = title;
        this.imageUrl = imageUrl;
    }
    public String getTitle(){
        return this.title;
    }
    int getImageUrl(){
        return this.imageUrl;
    }
    // getters & setters
}
