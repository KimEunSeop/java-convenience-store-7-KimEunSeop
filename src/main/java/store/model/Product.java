package store.model;

public class Product {
    private String name;
    private int price;
    private int quantity;
    private Promotion promotion;

    public Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public Promotion getPromotion() {
        return promotion;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSoldOut() {
        this.promotion = new Promotion("재고 없음", 0, 0, null, null);
    }
}