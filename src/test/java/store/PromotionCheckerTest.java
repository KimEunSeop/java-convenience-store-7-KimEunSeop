package store;

import store.model.Product;
import store.model.Promotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

class PromotionCheckerTest {

    private ProductRepository productRepository;
    private ShoppingCart shoppingCart;
    private PromotionChecker promotionChecker;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void 프로모션_1_1_적용_시_추가_상품_찾기() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("1+1", 1, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);


        assertEquals(1, promotionChecker.findMissedItems().get("콜라"));
    }

    @Test
    void 프로모션_2_1_적용_시_추가_상품_찾기() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);


        assertEquals(1, promotionChecker.findMissedItems().get("콜라"));
    }

    @Test
    void 프로모션_3_1_적용_시_추가_상품_찾기() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("3+1", 3, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-3]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        assertEquals(1, promotionChecker.findMissedItems().get("콜라"));
    }

    @Test
    void 프로모션_4_2_적용_시_추가_상품_찾기() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("4+2", 4, 2, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        assertEquals(1, promotionChecker.findMissedItems().get("콜라"));
    }

    @Test
    void Y_입력시_무료_상품_장바구니에_추가() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        promotionChecker.checkMissedItemsResponse("Y");


        for (Product product : shoppingCart.getPromotionProducts()) {
            if (product.getName().equals("콜라")) {
                assertEquals(3, product.getQuantity());
            }
        }
    }

    @Test
    void N_입력시_아무것도_추가되지않음() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        promotionChecker.checkMissedItemsResponse("N");

        for (Product product : shoppingCart.getPromotionProducts()) {
            if (product.getName().equals("콜라")) {
                assertEquals(2, product.getQuantity());
            }
        }
    }

    @Test
    void 잘못된_입력시_예외발생() {
        productRepository.add(new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31")));
        productRepository.add(new Product("콜라", 1000, 10, null));

        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            promotionChecker.checkMissedItemsResponse("Z");
        });

        assertEquals("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.", exception.getMessage());
    }
}
