package store;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private List<Product> products = new ArrayList<>();
    private List<Product> promotionProducts = new ArrayList<>();
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
        if (hasProduct(name, quantity)) return;

        List<Product> inputProducts = productRepository.findByName(name);

        for (Product product : inputProducts) {
            if (product.getPromotion() != null) {
                promotionProducts.add(new Product(name, product.getPrice(), quantity, product.getPromotion()));
                break;
            }
            products.add(new Product(name, product.getPrice(), quantity, product.getPromotion()));
        }
    }

    public void changeProduct(String name, Integer exceedQuantity) {
        int newPrice = 0;
        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() - exceedQuantity);
                newPrice = product.getPrice();
                break;
            }
        }
        promotionProducts = removeEmptyProducts(promotionProducts);
        boolean isExist = false;
        for (Product product : products) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() + exceedQuantity);
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            products.add(new Product(name, newPrice, exceedQuantity, null));
        }
    }

    public List<Product> removeEmptyProducts(List<Product> products) {
        List<Product> nonEmptyProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() > 0) {
                nonEmptyProducts.add(product);
            }
        }
        return nonEmptyProducts;
    }


    private boolean hasProduct(String name, int quantity) {
        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() + quantity);
                return true;
            }
        }
        for (Product product : products) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() + quantity);
                return true;
            }
        }
        return false;
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
        int totalquantity = 0;
        for (Product product : productRepository.findByName(name)) {
            totalquantity += product.getQuantity();
        }

        if (quantity > totalquantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getPromotionProducts() {
        return promotionProducts;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    public void substractPromotionProduct(String name, Integer exceedQuantity) {
        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() - exceedQuantity);
            }
        }
    }
}
