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
            String productString = makeString(product);
            productStrings.add(productString);
        }
        return productStrings;
    }

    private static String makeString(Product product) {
        String promotionName = setPromotionName(product);
        String quantityString = checkEmpty(product);
        String productString = String.format("%s %,d원 %s %s",
                product.getName(), product.getPrice(), quantityString, promotionName);
        return productString;
    }

    private static String setPromotionName(Product product) {
        String promotionName = "";
        if (product.getPromotion() != null) {
            promotionName = product.getPromotion().getName();
        }
        return promotionName;
    }

    private static String checkEmpty(Product product) {
        String quantityString = "";
        if (product.getQuantity() == 0) {
            quantityString = "재고 없음";
        }
        if (product.getQuantity() != 0) {
            quantityString = String.format("%d개", product.getQuantity());
        }
        return quantityString;
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
