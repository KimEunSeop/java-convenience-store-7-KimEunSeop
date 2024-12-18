# java-convenience-store-precourse

# 4주차 과제 목표

---
- 기능을 구현하고 바로 그 기능에 맞는 테스트를 만들어 점검한다.
- 함수형 인터페이스를 사용하여 예외 발생시 대응한다.
- 연관성이 있는 상수는 enum을 사용한다.
- 객체는 객체답게 사용한다.
- 후회하지 않게 열심히 해서 아름답고 우렁차게 프리코스를 마무리한다.

---
# 구현할 기능 목록

-[x] 파일 입출력을 통해 상품 목록, 행사 목록을 불러올 수 있다.

-[x] 환영인사, 상품명, 가격, 프로모션 이름, 재고 안내를 출력한다.

-[x] 재고가 없을 시 "재고 없음"을 출력한다.

-[x] 구매 안내 멘트를 출력한 후 상품명, 수량을 입력받는다.

-[x] 입력 형식을 잘 지켰는지 확인 후 예외처리한다.

-[x] 존재하지 않는 상품인지 확인 후 예외처리한다.

-[x] 구매수량이 재고 수량을 초과하는지 확인 후 예외처리한다.

-[x] 예외가 발생한다면 다시 구매할 상품명, 수량을 입력받는다.

-[x] 구매할 상품이 프로모션 상품인지 체크한다.

-[x] 프로모션 제품의 정확한 수량을 안가져왔을 경우를 확인 후 더 가져올지 물어본다.

    -[x] Y 입력 시 증정 받을 수 있는 상품을 장바구니에 추가한다.
  
    -[x] N 입력 시 그대로 진행한다.
  
    -[x] Y, N 말고 다른 입력을 받을 시 예외처리 후 다시 입력을 받는다.
  
-[x] 프로모션 재고가 부족한지 확인 후 일부 수량에 대해 정가로 결제할지 물어본다.

    -[x] Y 입력 시 일부 수량에 대해서는 정가로 결제한다.
  
    -[x] N 입력 시 정가로 결제해야 하는 수량만큼 제외한 후 결제한다.
  
    -[x] Y, N 말고 다른 입력을 받을 시 예외처리 후 다시 입력을 받는다.
  
-[x] 멤버쉽 할인을 받을 지 물어본다.

    -[x] Y 입력 시 프로모션 미적용 금액의 30프로의 할인을 적용한다.
  
        -[x] 최대 할인 금액은 8000원이다.
      
    -[x] N 입력 시 적용하지 않는다.
  
    -[x] Y, N 말고 다른 입력을 받을 시 예외처리 후 다시 입력을 받는다.
  
-[x] 구매시 재고에서 제외한다.

-[x] 영수증을 이쁘게 출력한다!

    - 구매 상품 내역: 구매한 상품명, 수량, 가격
    - 증정 상품 내역: 프로모션에 따라 무료로 제공된 증정 상품의 목록
    - 금액 정보
        - 총구매액: 구매한 상품의 총 수량과 총 금액
        - 행사할인: 프로모션에 의해 할인된 금액
        - 멤버십할인: 멤버십에 의해 추가로 할인된 금액
        - 내실돈: 최종 결제 금액
      
-[x] 더 구매할 상품이 있는지 물어본다.

    -[x] Y 입력 시 다시 재고 안내부터 시작한다.
  
    -[x] N 입력 시 종료한다.
  
    -[x] Y, N 말고 다른 입력을 받을 시 예외처리 후 다시 입력을 받는다.

---
### 11.05 ~ 11.06 코드 상호 리뷰 진행
### 11.07 ~ 11.08 문제 이해 및 기획
### 11.08. 19:50 ~ 20:27 README.md 작성
### 11.08. 20:27 ~ 01:32 1차 코딩 완료
- 파일 입출력을 위한 로직에 굉장히 오랜 시간을 힘을 씀 
### 11.09. 18:30 ~ 03:17 2차 코딩 완료
- 중간에 1시간 밥 먹음
- 프로모션 관련 로직에서 많은 시간을 씀
### 11.10. 16:18 ~ 03:49 3차 코딩 완료, 최소 기능 동작 구현 완료
- 중간에 1시간 밥 먹음
- 생각하지 못한 프로모션 관련 로직에서 많은 시간을 씀
- 요구사항의 내용을 잘못 숙지하여 전체적인 수정에 시간을 많이 씀
### 11.11 03:49 ~ 06:30, 최소 기능 동작 구현 완료 (버그 해결)
- 아직 버그가 많아 해결하지 못한 버그 수정
- 버그를 수정하면 다른 버그가 나타나 시간이 오래 걸림
### 11.11 06:30 ~ 19:35, 각종 버그 해결 및 코드 리팩토링
- 약 9~10시간 진행
- 예상하지 못한 버그가 많이 발생

