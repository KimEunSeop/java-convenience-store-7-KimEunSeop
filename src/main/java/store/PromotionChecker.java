package store;

import store.model.Product;
import store.model.Promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionChecker {

    private ShoppingCart shoppingCart;

    private Map<String, Integer> missedItems = new HashMap<>();

    public PromotionChecker(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public Map<String, Integer> findMissedItems() {
        for (Product product : shoppingCart.getProducts()) {
            checkMissed(product);
        }

        return missedItems;
    }

    private void checkMissed(Product product) {
        if (product.getPromotion() != null) {
            int missedItemCount = calculateMissedItemCount(product.getQuantity(), product.getPromotion());
            if (missedItemCount > 0) {
                missedItems.put(product.getName(), missedItemCount);
            }
        }
    }

    private int calculateMissedItemCount(int buyCount, Promotion promotion) {
        int missedItemCount = promotion.getGetCount()
                - ((buyCount % (promotion.getBuyCount() + promotion.getGetCount()))
                - promotion.getBuyCount());

        return missedItemCount;
    }

    public void checkResponse(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            for (String key : missedItems.keySet()) {
                shoppingCart.addProduct(key, missedItems.get(key));
            }
            return;
        }
        if ("N".equalsIgnoreCase(input)) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.");
    }
}
