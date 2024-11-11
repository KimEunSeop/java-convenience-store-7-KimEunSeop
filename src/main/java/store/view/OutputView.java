package store.view;

import store.model.Product;

import java.util.List;
import java.util.Map;

public class OutputView {
    public void printWelcomeGuide() {
        System.out.println(OutputViewMessage.WELCOME.getMessage());
    }

    public void printItemList(List<String> items) {
        System.out.println(OutputViewMessage.ITEM_LIST_HEADER.getMessage());
        System.out.println();
        for (String item : items) {
            System.out.println(String.format("- %s", item));
        }
        System.out.println();
    }

    public void printPurchaseGuide() {
        System.out.println(OutputViewMessage.PURCHASE_GUIDE.getMessage());
    }

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printGetMissedItemGuide(String productName, int quantity) {
        System.out.println(String.format(OutputViewMessage.MISSED_ITEM_GUIDE.getMessage(), productName, quantity));
    }

    public void printYOrN() {
        System.out.println(OutputViewMessage.YES_OR_NO.getMessage());
    }

    public void printExcludeGuide(String productName, Integer exceedQuantity) {
        System.out.println(String.format(
                OutputViewMessage.EXCEED_PROMOTION_GUIDE.getMessage(), productName, exceedQuantity));
    }

    public void printMembershipDiscountGuide() {
        System.out.println(OutputViewMessage.MEMBERSHIP_DISCOUNT_GUIDE.getMessage());
    }

    public void printReceipt(Map<String, Integer> freeItems, List<Product> products,
                             int totalPrice, int promotionDiscount, int membershipDiscount, int finalPrice) {
        System.out.println(OutputViewMessage.RECEIPT_HEADER.getMessage());
        printProductList(products);

        if (!freeItems.isEmpty()) {
            System.out.println("\n" + OutputViewMessage.FREE_ITEMS_HEADER.getMessage());
            printFreeItems(freeItems);
        }

        int totalQuantity = calculateTotalQuantity(products);
        printFinalAmount(totalPrice, promotionDiscount, membershipDiscount, finalPrice, totalQuantity);
    }

    private void printProductList(List<Product> products) {
        System.out.println(String.format("%-10s\t%-6s\t%-10s", "상품명", "수량", "금액"));
        for (Product product : products) {
            System.out.println(String.format("%-10s\t%-6d\t%-10s",
                    product.getName(),
                    product.getQuantity(),
                    String.format("%,d", product.getPrice() * product.getQuantity())));
        }
    }

    private void printFreeItems(Map<String, Integer> freeItems) {
        for (Map.Entry<String, Integer> entry : freeItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(String.format("%-10s\t%-6d", productName, quantity));
        }
    }

    private void printFinalAmount(
            int totalPrice, int promotionDiscount, int membershipDiscount, int finalPrice, int totalQuantity) {
        System.out.println(OutputViewMessage.FINAL_AMOUNT_HEADER.getMessage());
        System.out.println(String.format("%-12s\t%-6d\t%5s",
                OutputViewMessage.TOTAL_AMOUNT.getMessage(), totalQuantity, String.format("%,d", totalPrice)));
        System.out.println(String.format("%-12s\t%14s",
                OutputViewMessage.PROMOTION_DISCOUNT.getMessage(), String.format("%,d", -promotionDiscount)));
        System.out.println(String.format("%-12s\t%14s",
                OutputViewMessage.MEMBERSHIP_DISCOUNT.getMessage(), String.format("%,d", -membershipDiscount)));
        System.out.println(String.format("%-12s\t%14s",
                OutputViewMessage.FINAL_PRICE.getMessage(), String.format("%,d", finalPrice)));
    }

    public void printFinishGuide() {
        System.out.println(OutputViewMessage.FINISH_GUIDE.getMessage());
    }

    private int calculateTotalQuantity(List<Product> products) {
        return products.stream().mapToInt(Product::getQuantity).sum();
    }
}
