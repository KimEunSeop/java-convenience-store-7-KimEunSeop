package store.view;

import java.util.List;

public class OutputView {
    public void printWelcomeGuide() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    public void printItemList(List<String> items) {
        for(String item : items){
            System.out.println("- ");
            System.out.println(item);
        }
    }
}
