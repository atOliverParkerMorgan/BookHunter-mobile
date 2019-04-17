package oliver.bookhunter.Website;

public class ItemData {


    private String title;
    private int imageUrl;

    public ItemData(String title,int imageUrl){

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
