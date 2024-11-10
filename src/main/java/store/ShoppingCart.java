package store;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Product product = findProduct(promotionProducts, name);
        if (product != null) {
            addPromotionProductQuantity(product, quantity);
            return true;
        }
        product = findProduct(products, name);
        if (product != null) {
            product.setQuantity(product.getQuantity() + quantity);
            return true;
        }
        return false;
    }

    private Product findProduct(List<Product> productList, String name) {
        for (Product product : productList) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
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

    public void substractPromotionProduct(String name, Integer exceedQuantity) {
        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                product.setQuantity(product.getQuantity() - exceedQuantity);
            }
        }
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Product> getPromotionProducts() {
        return promotionProducts;
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
            update(promotionProduct, totalProducts);
        }
    }

    private void update(Product promotionProduct, List<Product> totalProducts) {
        for (Product product : totalProducts) {
            if (product.getName().equals(promotionProduct.getName())) {
                product.setQuantity(product.getQuantity() + promotionProduct.getQuantity());
                return;
            }
        }
        totalProducts.add(new Product(promotionProduct.getName(), promotionProduct.getPrice(), promotionProduct.getQuantity(), null));
    }

    public void buy() {
        List<Product> allProducts = productRepository.getProducts();

        Map<String, Product> productMap = new HashMap<>();
        for (Product repoProduct : allProducts) {
            productMap.put(repoProduct.getName(), repoProduct);
        }

        deductProductQuantity(productMap, products);
        deductProductQuantity(productMap, promotionProducts);
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    private void deductProductQuantity(Map<String, Product> productMap, List<Product> products) {
        for (Product product : products) {
            Product repositoryProduct = productMap.get(product.getName());

            if (repositoryProduct != null) {
                int newQuantity = repositoryProduct.getQuantity() - product.getQuantity();
                repositoryProduct.setQuantity(newQuantity);
                if (newQuantity == 0) {
                    repositoryProduct.setSoldOut();
                }
            }
        }
    }

    public int getRemainStock(String name) {
        int promotionStock = productRepository.findPromotionStockByName(name);

        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                promotionStock -= product.getQuantity();
            }
        }
        return promotionStock;
    }
}
