package store.repository;

import store.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void add(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> findByName(String name) {
        return products.stream()
                .filter(p -> p.getName().equals(name))
                .collect(Collectors.toList());
    }

    public List<String> getProductsAsString() {
        List<String> productStrings = new ArrayList<>();
        for (Product product : products) {
            String promotionName = "";
            if (product.getPromotion() != null) {
                promotionName = product.getPromotion().getName();
            }
            String productString = String.format("%s %,d원 %d개 %s",
                    product.getName(), product.getPrice(), product.getQuantity(), promotionName);
            productStrings.add(productString);
        }
        return productStrings;
    }
}
