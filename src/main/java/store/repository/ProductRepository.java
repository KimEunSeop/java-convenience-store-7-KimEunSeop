package store.repository;

import store.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product findByName(String name) {
        return products.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public List<String> getProductsAsString() {
        List<String> productStrings = new ArrayList<>();
        for (Product product : products) {
            String promotionName;
            promotionName = product.getPromotion().getName();
            if (product.getPromotion() == null) {
                promotionName = "재고없음";
            }
            String productString = String.format("%s %,d원 %d개 %s",
                    product.getName(), product.getPrice(), product.getQuantity(), promotionName);
            productStrings.add(productString);
        }
        return productStrings;
    }
}
