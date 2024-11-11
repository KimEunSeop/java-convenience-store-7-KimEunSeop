package store.Service;

import store.ErrorMessage;
import store.model.Product;

public class PriceCalculator {

    public static final String YES = "Y";
    public static final String NO = "N";
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
        if (YES.equalsIgnoreCase(input)) {
            calculatemembershipDiscount();
            return;
        }
        if (NO.equalsIgnoreCase(input)) {
            return;
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_Y_OR_N_INPUT.getMessage());
    }

    private void calculatemembershipDiscount() {
        int promotionPrice = 0;
        for (Product promotionProduct : shoppingCart.getPromotionProducts()) {
            int buyCount = promotionProduct.getPromotion().getBuyCount();
            int getCount = promotionProduct.getPromotion().getGetCount();
            promotionPrice += ((promotionProduct.getQuantity() / (buyCount + getCount)) * (buyCount + getCount)) * promotionProduct.getPrice();
        }
        membershipDiscount = (int) ((totalPrice - promotionPrice) * MemberShip.STANDARD.getDiscountPercentage());
        if (membershipDiscount > MemberShip.STANDARD.getMaxDiscount()) {
            membershipDiscount = MemberShip.STANDARD.getMaxDiscount();
        }
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
