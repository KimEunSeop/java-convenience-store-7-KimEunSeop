package store;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products = new ArrayList<>();
    private final ProductRepository productRepository;

    public ShoppingCart(String input, ProductRepository productRepository) {
        this.productRepository = productRepository;
        set(input);
    }

    private void set(String input) {
        String[] items = input.split(",");
        for (String item : items) {
            validateInputFormat(item);
            String[] parsedItem = parse(item);
            validateExistence(parsedItem[0]);
            validateQuantity(parsedItem[0], Integer.parseInt(parsedItem[1]));
            addProduct(parsedItem[0], Integer.parseInt(parsedItem[1]));
        }
    }

    private String[] parse(String item) {
        String[] split = item.replaceAll("[\\[\\]]", "").split("-");
        split[0] = split[0].trim();
        split[1] = split[1].trim();
        return split;
    }

    public void addProduct(String name, int quantity) {
        Product product = productRepository.findByName(name);
        products.add(new Product(name, product.getPrice(), quantity, product.getPromotion()));
    }

    private void validateInputFormat(String item) {
        if (!item.matches("\\[.+-\\d+\\]")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private void validateExistence(String name) {
        if (productRepository.findByName(name) == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
    }

    private void validateQuantity(String name, int quantity) {
        Product product = productRepository.findByName(name);
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public List<Product> getProducts() {
        return products;
    }
}
