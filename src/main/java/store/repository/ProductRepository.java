package store.repository;

import store.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    public void add (Product product) {
        products.add(product);
    }
    public List<Product> findAll() {
        return products;
    }
    public Product findByName(String name) {
        return products.stream().filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }
}
