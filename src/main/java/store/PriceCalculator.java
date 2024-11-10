package store;

import store.model.Product;

public class PriceCalculator {

    public static final double MEMBERSHIP_DISCOUNT_PERSENTAGE = 0.3;
    private final ShoppingCart shoppingCart;
    private int totalAmount = 0;
    private int promotionDiscount = 0;
    private int membershipDiscount = 0;

    public PriceCalculator(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void calculateTotal() {
        for (Product product : shoppingCart.getProducts()) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
        for (Product product : shoppingCart.getPromotionProducts()) {
            totalAmount += product.getPrice() * product.getQuantity();
        }
    }

    public void calculatePromotions() {
        for (Product promotionProduct : shoppingCart.getPromotionProducts()) {
            int buyCount = promotionProduct.getPromotion().getBuyCount();
            int getCount = promotionProduct.getPromotion().getGetCount();
            promotionDiscount = ((promotionProduct.getQuantity() / (buyCount + getCount)) * getCount)
                    - ((promotionProduct.getQuantity() % (buyCount + getCount)) - buyCount);

        }
    }

    public void calculatemembershipDiscount(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            membershipDiscount = (int) ((totalAmount - promotionDiscount) * MEMBERSHIP_DISCOUNT_PERSENTAGE);
            if (membershipDiscount > 8000) {
                membershipDiscount = 8000;
            }
            return;
        }
        if ("N".equalsIgnoreCase(input)) {
            return;
        }
        throw new IllegalArgumentException("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.");

    }

    public int getFinalAmount() {
        return totalAmount - promotionDiscount - membershipDiscount;
    }
}
