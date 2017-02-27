package hitam.epics.anbfhadmin;

/**
 * Created by sanjit on 27/2/17.
 */

public class Item {
    private String name;
    private String pictureurl;
    private float price;

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", pictureurl='" + pictureurl + '\'' +
                ", price=" + price +
                '}';
    }

    public Item() {
    }

    public Item(String name, float price) {
        this.name = name;
        this.price = price;
    }

    public void setPictureurl(String pictureurl) {
        this.pictureurl = pictureurl;
    }

    public Item(String name, String pictureurl, float price) {
        this.name = name;
        this.pictureurl = pictureurl;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPictureurl() {
        return pictureurl;
    }

    public float getPrice() {
        return price;
    }
}
