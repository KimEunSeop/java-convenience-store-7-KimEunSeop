package store.view;

import java.util.List;

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

    public void printReceipt(int totalAmount, int promotionDiscount, int membershipDiscount, int finalAmount) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t\t금액");


        System.out.println("==============증   정================");


        System.out.println("====================================");
        System.out.println("총 구매액\t\t" + totalAmount + "\t" + totalAmount);
        System.out.println("행사 할인\t\t-" + promotionDiscount);
        System.out.println("멤버십 할인\t\t-" + membershipDiscount);
        System.out.println("내실 돈\t\t\t" + finalAmount);
    }
}
