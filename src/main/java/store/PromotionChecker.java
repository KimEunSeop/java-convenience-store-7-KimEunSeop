package store;

import store.model.Product;
import store.model.Promotion;
import store.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;

public class PromotionChecker {

    private ShoppingCart shoppingCart;

    private Map<String, Integer> missedItems = new HashMap<>();
    private Map<String, Integer> exceedItems = new HashMap<>();

    public PromotionChecker(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Map<String, Integer> findMissedItems() {
        for (Product product : shoppingCart.getPromotionProducts()) {
            changeAmount(product);
            checkMissed(product);
        }

        return missedItems;
    }

    private void changeAmount(Product product) {
        int amount = 0;
        if (product.getQuantity() % (product.getPromotion().getBuyCount() + product.getPromotion().getGetCount())
                < product.getPromotion().getBuyCount()) {
            amount = product.getQuantity() %
                    (product.getPromotion().getBuyCount() + product.getPromotion().getGetCount());
        }
        shoppingCart.substractPromotionProduct(product.getName(), amount);
    }

    public Map<String, Integer> checkPromotionQuantity(ProductRepository productRepository) {
        for (Product product : shoppingCart.getPromotionProducts()) {
            checkQuantity(product, productRepository);
        }

        return exceedItems;
    }

    private void checkQuantity(Product checkProduct, ProductRepository productRepository) {
        int quantityLimit = 0;
        for (Product product : productRepository.findByName(checkProduct.getName())) {
            if (product.getPromotion() != null) {
                quantityLimit = product.getQuantity();
            }
        }

        if (checkProduct.getQuantity() > quantityLimit) {
            int exceedQuantity = calculateMaxQuantity(checkProduct, quantityLimit);
            exceedItems.put(checkProduct.getName(), exceedQuantity);
        }
    }

    private int calculateMaxQuantity(Product product, int quantityLimit) {
        int buyCount = product.getPromotion().getBuyCount();
        int getCount = product.getPromotion().getGetCount();
        return (product.getQuantity() - ((quantityLimit / (buyCount + getCount)) * (buyCount + getCount)));
    }

    private void checkMissed(Product product) {
        int missedItemCount = calculateMissedItemCount(product.getQuantity(), product.getPromotion());
        if (missedItemCount > 0) {
            missedItems.put(product.getName(), missedItemCount);
        }
    }

    private int calculateMissedItemCount(int buyCount, Promotion promotion) {
        if (((buyCount % (promotion.getBuyCount() + promotion.getGetCount())) - promotion.getBuyCount()) >= 0) {
            int missedItemCount = promotion.getGetCount()
                    - ((buyCount % (promotion.getBuyCount() + promotion.getGetCount()))
                    - promotion.getBuyCount());
            return missedItemCount;
        }
        return 0;
    }

    public void checkMissedItemsResponse(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            for (String name : missedItems.keySet()) {
                shoppingCart.addProduct(name, missedItems.get(name));
            }
            return;
        }
        if ("N".equalsIgnoreCase(input)) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.");
    }

    public void checkExceedItemsResponse(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            for (String name : exceedItems.keySet()) {
                shoppingCart.changeProduct(name, exceedItems.get(name));
            }
            return;
        }
        if ("N".equalsIgnoreCase(input)) {
            for (String name : exceedItems.keySet()) {
                shoppingCart.substractPromotionProduct(name, exceedItems.get(name));
            }
//            System.out.println(shoppingCart.getPromotionProducts().get(0).getName() + shoppingCart.getPromotionProducts().get(0).getQuantity());
            return;
        }
        throw new IllegalArgumentException("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.");
    }
}
