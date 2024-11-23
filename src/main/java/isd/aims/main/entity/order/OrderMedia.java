package isd.aims.main.entity.order;

import isd.aims.main.entity.media.Media;

public class OrderMedia {

    private Media media;
    private int price;
    private int quantity;
    private boolean isRushOrder;
    private double weight;

    // Constructor
    public OrderMedia(Media media, int quantity, int price, double weight, boolean isRushOrder) {
        this.media = media;
        this.quantity = quantity;
        this.price = price;
        this.weight = weight;
        this.isRushOrder = isRushOrder; 
    }

    @Override
    public String toString() {
        return "{" +
                "  media='" + media + "'" +
                ", quantity='" + quantity + "'" +
                ", price='" + price + "'" +
                ", weight='" + weight + "'" +
                ", isRushOrder='" + isRushOrder + "'" +
                "}";
    }

    // Getters and setters
    public Media getMedia() {
        return this.media;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setRushOrder(boolean isRushOrder) {
        this.isRushOrder = isRushOrder;
    }

    public boolean getRushOrder() {
        return this.isRushOrder;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
