package store.Service;

import store.ErrorMessage;
import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    public static final int EMPTY = 0;
    private List<Product> products = new ArrayList<>();
    private List<Product> promotionProducts = new ArrayList<>();
    private final ProductRepository productRepository;
    private final Counter counter;

    public ShoppingCart(String input, ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.counter = new Counter(productRepository);
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
            if (product.getPromotion() == null) {
                continue;
            }
            if (product.getQuantity() >= quantity) {
                promotionProducts.add(new Product(name, product.getPrice(), quantity, product.getPromotion()));
                return;
            }
            promotionProducts.add(new Product(name, product.getPrice(), product.getQuantity(), product.getPromotion()));
            products.add(new Product(name, product.getPrice(), quantity - product.getQuantity(), null));
            return;
        }
        products.add(new Product(name, inputProducts.get(0).getPrice(), quantity, null));
    }

    private boolean hasProduct(String name, int quantity) {
        Product product = counter.findProduct(promotionProducts, name);
        if (product != null) {
            addPromotionProductQuantity(product, quantity);
            return true;
        }
        product = counter.findProduct(products, name);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return true;
        }
        return false;
    }

    private void addPromotionProductQuantity(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            int remainingQuantity = quantity - product.getQuantity();
            product.setQuantity(product.getQuantity() + remainingQuantity);
            addProductQuantity(product.getName(), remainingQuantity);
        }
        product.setQuantity(product.getQuantity() + quantity);
    }

    private void addProductQuantity(String name, int quantity) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() + quantity);
                return;
            }
        }
        Product product = productRepository.findByName(name).get(0);
        products.add(new Product(name, product.getPrice(), quantity, null));
    }

    private void validateInputFormat(String item) {
        if (!item.matches("\\[.+-\\d+\\]")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_INPUT_FORMAT.getMessage());
        }
    }

    private void validateExistence(String name) {
        if (productRepository.findByName(name).size() == EMPTY) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXIST.getMessage());
        }
    }

    private void validateQuantity(String name, int quantity) {
        int totalquantity = 0;
        for (Product product : productRepository.findByName(name)) {
            totalquantity += product.getQuantity();
        }

        if (quantity > totalquantity) {
            throw new IllegalArgumentException(ErrorMessage.OUT_OF_STOCK.getMessage());
        }
    }

    public void subtractProduct(String name, Integer excludeQuantity) {
        int remainingQuantity = subtractProducts(name, excludeQuantity);
        if (remainingQuantity > 0) {
            subtractPromotionProducts(name, remainingQuantity);
        }
    }

    public int subtractProducts(String name, Integer excludeQuantity) {
        for (Product product : products) {
            if (!product.getName().equals(name)) continue;

            int deductedQuantity = Math.min(product.getQuantity(), excludeQuantity);
            product.setQuantity(product.getQuantity() - deductedQuantity);
            excludeQuantity -= deductedQuantity;

            if (excludeQuantity == 0) return 0;
        }
        return excludeQuantity;
    }

    public void subtractPromotionProducts(String name, Integer excludeQuantity) {
        for (Product product : promotionProducts) {
            if (!product.getName().equals(name)) continue;

            int deductedQuantity = Math.min(product.getQuantity(), excludeQuantity);
            product.setQuantity(product.getQuantity() - deductedQuantity);
            excludeQuantity -= deductedQuantity;

            if (excludeQuantity == 0) return;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> totalProducts = new ArrayList<>();

        combineProducts(products, totalProducts);
        combinePromotionProducts(promotionProducts, totalProducts);

        return totalProducts;
    }

    private void combineProducts(List<Product> products, List<Product> totalProducts) {
        for (Product product : products) {
            totalProducts.add(new Product(product.getName(), product.getPrice(), product.getQuantity(), null));
        }
    }

    private void combinePromotionProducts(List<Product> promotionProducts, List<Product> totalProducts) {
        for (Product promotionProduct : promotionProducts) {
            updatePromotionProducts(promotionProduct, totalProducts);
        }
    }

    private void updatePromotionProducts(Product promotionProduct, List<Product> totalProducts) {
        for (Product product : totalProducts) {
            if (product.getName().equals(promotionProduct.getName())) {
                product.setQuantity(product.getQuantity() + promotionProduct.getQuantity());
                return;
            }
        }
        totalProducts.add(new Product(promotionProduct.getName(), promotionProduct.getPrice(), promotionProduct.getQuantity(), null));
    }

    public int getRemainPromotionProductStock(String name) {
        return counter.getRemainPromotionProductStock(name, promotionProducts);
    }

    public int getRemainProductStock(String name) {
        return counter.getRemainProductStock(name, products);
    }

    public void buy() {
        counter.buyPromotionProducts(promotionProducts);
        counter.buyProducts(products);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getPromotionProducts() {
        return promotionProducts;
    }
}
