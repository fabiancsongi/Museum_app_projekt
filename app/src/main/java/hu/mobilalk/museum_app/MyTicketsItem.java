package hu.mobilalk.museum_app;

public class MyTicketsItem {

    private String title;
    private String description;
    private String price;
    private String id;

    public MyTicketsItem() {
    }

    public MyTicketsItem(String title, String description, String price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }


     public String _getId(){
        return id;
     }

    public void setId(String id) {
        this.id = id;
    }
}
