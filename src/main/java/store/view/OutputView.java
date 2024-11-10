package store.view;

import store.model.Product;

import java.util.List;
import java.util.Map;

public class OutputView {
    public void printWelcomeGuide() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printItemList(List<String> items) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        for (String item : items) {
            System.out.print("- ");
            System.out.println(item);
        }
        System.out.println();
    }

    public void printPurchaseGuide() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예 : [사이다-2],[감자칩-1])");
    }

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printGetMissedItemGuide(String productName, int quantity) {
        System.out.println("현재 " + productName + "은(는) " + quantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까?");
    }

    public void printYOrN() {
        System.out.println(" (Y/N)");
    }

    public void printExcludeGuide(String productName, Integer exceedQuantity) {
        System.out.println("현재 " + productName + " " + exceedQuantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?");
    }

    public void printMembershipDiscountGuide() {
        System.out.println("멤버십 할인을 받으시겠습니까?");
    }

    public void printReceipt(Map<String, Integer> freeItems, List<Product> products, int totalPrice, int promotionDiscount, int membershipDiscount, int finalPrice) {
        System.out.println("==============W 편의점================");
        printProductList(products);

        if (!freeItems.isEmpty()) {
            System.out.println("\n==============증   정================");
            printFreeItems(freeItems);
        }

        printFinalAmount(totalPrice, promotionDiscount, membershipDiscount, finalPrice);
    }

    private void printProductList(List<Product> products) {
        System.out.printf("%-10s\t%-6s\t%-10s\n", "상품명", "수량", "금액");
        for (Product product : products) {
            System.out.printf("%-10s\t%-6d\t%-10d\n", product.getName(), product.getQuantity(), product.getPrice() * product.getQuantity());
        }
    }

    private void printFreeItems(Map<String, Integer> freeItems) {
        for (Map.Entry<String, Integer> entry : freeItems.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            System.out.printf("%-10s\t%-6d\t\n", productName, quantity);
        }
    }

    private void printFinalAmount(int totalAmount, int promotionDiscount, int membershipDiscount, int finalAmount) {
        System.out.println("====================================");
        System.out.printf("%-10s\t%-6d\t%-10d\n", "총 구매액", totalAmount, totalAmount);
        System.out.printf("%-10s\t%-6d\t%-10d\n", "행사 할인", -promotionDiscount, -promotionDiscount);
        System.out.printf("%-10s\t%-6d\t%-10d\n", "멤버십 할인", -membershipDiscount, -membershipDiscount);
        System.out.printf("%-10s\t%-6d\t%-10d\n", "내실 돈", finalAmount, finalAmount);
    }

    public void printFinsihGuide() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요?");
    }
}
