package store.Service;

import store.model.Product;

public class PriceCalculator {

    public static final double MEMBERSHIP_DISCOUNT_PERSENTAGE = 0.3;
    private final ShoppingCart shoppingCart;
    private int totalPrice = 0;
    private int promotionDiscount = 0;
    private int membershipDiscount = 0;

    public PriceCalculator(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void calculateTotal() {
        for (Product product : shoppingCart.getProducts()) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        for (Product product : shoppingCart.getPromotionProducts()) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
    }

    public void calculatePromotions() {
        for (Product promotionProduct : shoppingCart.getPromotionProducts()) {
            int buyCount = promotionProduct.getPromotion().getBuyCount();
            int getCount = promotionProduct.getPromotion().getGetCount();
            int freeCount = ((promotionProduct.getQuantity() / (buyCount + getCount)) * getCount);

            if (promotionProduct.getQuantity() % (buyCount + getCount) > buyCount) {
                freeCount += ((promotionProduct.getQuantity() % (buyCount + getCount)) - buyCount);
            }
            promotionDiscount = freeCount * promotionProduct.getPrice();
        }
    }

    public void calculatemembershipDiscount(String input) {
        if ("Y".equalsIgnoreCase(input)) {
            membershipDiscount = (int) ((totalPrice - promotionDiscount) * MEMBERSHIP_DISCOUNT_PERSENTAGE);
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalprice() {
        return totalPrice - promotionDiscount - membershipDiscount;
    }
}
