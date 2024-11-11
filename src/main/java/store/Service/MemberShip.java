package store.Service;

public enum MemberShip {
    STANDARD(0.3, 8000);

    private final double discountPercentage;
    private final int maxDiscount;

    MemberShip(double discountPercentage, int maxDiscount) {
        this.discountPercentage = discountPercentage;
        this.maxDiscount = maxDiscount;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public int getMaxDiscount() {
        return maxDiscount;
    }
}
