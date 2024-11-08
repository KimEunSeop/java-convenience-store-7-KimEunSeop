package store.view;

import java.util.List;

public class OutputView {
    public void printWelcomeGuide() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printItemList(List<String> items) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
        for(String item : items){
            System.out.print("- ");
            System.out.println(item);
        }
    }
}
