package store.repository;

import store.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductRepository {
    private final List<Product> products = new ArrayList<>();

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

    public Product findPromotionProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && product.getPromotion() != null)
                .findFirst()
                .orElse(null);
    }

    public Product findProductByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && product.getPromotion() == null)
                .findFirst()
                .orElse(null);
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

    public int findPromotionProductStockByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && product.getPromotion() != null)
                .mapToInt(Product::getQuantity)
                .findFirst()
                .orElse(0);
    }

    public int findProductStockByName(String name) {
        return products.stream()
                .filter(product -> product.getName().equals(name) && product.getPromotion() == null)
                .mapToInt(Product::getQuantity)
                .findFirst()
                .orElse(0);
    }
}
