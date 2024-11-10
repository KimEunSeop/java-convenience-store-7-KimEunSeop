package store.view;

public enum OutputViewMessage {
    WELCOME("안녕하세요. W편의점입니다."),
    ITEM_LIST_HEADER("현재 보유하고 있는 상품입니다."),
    PURCHASE_GUIDE("구매하실 상품명과 수량을 입력해 주세요. (예 : [사이다-2],[감자칩-1])"),
    MISSED_ITEM_GUIDE("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까?"),
    YES_OR_NO(" (Y/N)"),
    EXCEED_PROMOTION_GUIDE("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까?"),
    MEMBERSHIP_DISCOUNT_GUIDE("멤버십 할인을 받으시겠습니까?"),
    RECEIPT_HEADER("==============W 편의점================"),
    FREE_ITEMS_HEADER("==============증   정================"),
    FINAL_AMOUNT_HEADER("===================================="),
    TOTAL_AMOUNT("총구매액"),
    PROMOTION_DISCOUNT("행사할인"),
    MEMBERSHIP_DISCOUNT("멤버십할인"),
    FINAL_PRICE("내실돈"),
    FINISH_GUIDE("감사합니다. 구매하고 싶은 다른 상품이 있나요?");

    private final String message;

    OutputViewMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
