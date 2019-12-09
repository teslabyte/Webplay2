package webplay.ResponseClasses.Gashas;

public class Course {
    private int no;
    private String title;
    private String description;
    private int currency_id;
    private int price;
    private int items_count;
    private int drawable_count;
    private int treasure_item_count;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(int currency_id) {
        this.currency_id = currency_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getItems_count() {
        return items_count;
    }

    public void setItems_count(int items_count) {
        this.items_count = items_count;
    }

    public int getDrawable_count() {
        return drawable_count;
    }

    public void setDrawable_count(int drawable_count) {
        this.drawable_count = drawable_count;
    }

    public int getTreasure_item_count() {
        return treasure_item_count;
    }

    public void setTreasure_item_count(int treasure_item_count) {
        this.treasure_item_count = treasure_item_count;
    }
}
