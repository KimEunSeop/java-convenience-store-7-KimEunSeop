package store.Service;

import store.ErrorMessage;
import store.model.Product;
import store.model.Promotion;
import store.repository.ProductRepository;

import java.util.HashMap;
import java.util.Map;

public class PromotionService {

    private ShoppingCart shoppingCart;
    private Map<String, Integer> missedItems = new HashMap<>();
    private Map<String, Integer> excludeItems = new HashMap<>();
    private Map<String, Integer> freeItems = new HashMap<>();

    public PromotionService(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Map<String, Integer> findMissedItems() {
        for (Product product : shoppingCart.getPromotionProducts()) {
            checkMissed(product);
        }

        return missedItems;
    }

    private void checkMissed(Product product) {
        int missedItemCount = calculateMissedItemCount(product.getQuantity(), product.getPromotion());
        int stock = shoppingCart.getRemainPromotionProductStock(product.getName());
        if (missedItemCount > 0 && stock != 0) {
            if (missedItemCount <= stock) {
                missedItems.put(product.getName(), missedItemCount);
                return;
            }
            missedItems.put(product.getName(), missedItemCount - stock);
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
        throw new IllegalArgumentException(ErrorMessage.INVALID_Y_OR_N_INPUT.getMessage());
    }

    public Map<String, Integer> checkExclude(ProductRepository productRepository) {
        for (Product product : shoppingCart.getPromotionProducts()) {
            checkQuantity(product, productRepository);
        }
        return excludeItems;
    }

    private void checkQuantity(Product checkProduct, ProductRepository productRepository) {
        int quantityLimit = 0;
        for (Product product : productRepository.findByName(checkProduct.getName())) {
            if (product.getPromotion() != null) {
                quantityLimit = product.getQuantity();
            }
        }
        if (checkProduct.getQuantity() == quantityLimit) {
            int excludeQuantity = calculateExcludeQuantity(checkProduct);
            excludeItems.put(checkProduct.getName(), excludeQuantity);
        }
    }

    private int calculateExcludeQuantity(Product checkProduct) {
        int excludePromotionProductQuality = checkProduct.getQuantity() % (checkProduct.getPromotion().getGetCount() + checkProduct.getPromotion().getBuyCount());
        for (Product product : shoppingCart.getProducts()) {
            if (checkProduct.getName().equals(product.getName())) {
                excludePromotionProductQuality += product.getQuantity();
            }
        }
        return excludePromotionProductQuality;
    }

    public void checkExcludeItemsResponse(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            return;
        }
        if ("N".equalsIgnoreCase(input)) {
            for (String name : excludeItems.keySet()) {
                shoppingCart.subtractProduct(name, excludeItems.get(name));
            }
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_Y_OR_N_INPUT.getMessage());
    }

    public Map<String, Integer> getFreeItems() {
        for (Product product : shoppingCart.getPromotionProducts()) {
            int buyCount = product.getPromotion().getBuyCount();
            int getCount = product.getPromotion().getGetCount();
            int freeCount = ((product.getQuantity() / (buyCount + getCount)) * getCount);
            if (product.getQuantity() % (buyCount + getCount) > buyCount) {
                freeCount += ((product.getQuantity() % (buyCount + getCount)) - buyCount);
            }
            freeItems.put(product.getName(), freeCount);
        }
        return freeItems;
    }
}
