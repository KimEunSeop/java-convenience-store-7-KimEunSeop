package store;

import store.model.Product;
import store.model.Promotion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.repository.ProductRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PromotionCheckerTest {

    private ProductRepository productRepository;
    private ShoppingCart shoppingCart;
    private PromotionChecker promotionChecker;

    @Test
    void 프로모션_1_1_적용_시_추가_상품_찾기() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("1+1",1, 1,"2024-01-01","2024-12-31"));
                }
                return null;
            }
        };
        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        var missedItems = promotionChecker.findMissedItems();
        assertEquals(1, missedItems.size());
    }

    @Test
    void 프로모션_2_1_적용_시_추가_상품_찾기() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("2+1",2, 1,"2024-01-01","2024-12-31"));
                }
                return null;
            }
        };
        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        var missedItems = promotionChecker.findMissedItems();
        assertEquals(1, missedItems.size());
    }

    @Test
    void 프로모션_3_1_적용_시_추가_상품_찾기() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("3+1",3, 1,"2024-01-01","2024-12-31"));
                }
                return null;
            }
        };

        String input = "[콜라-3]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        var missedItems = promotionChecker.findMissedItems();
        assertEquals(1, missedItems.size());
    }

    @Test
    void 프로모션_4_2_적용_시_추가_상품_찾기() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("4+2",4, 2,"2024-01-01","2024-12-31"));
                }
                return null;
            }
        };

        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        var missedItems = promotionChecker.findMissedItems();
        assertEquals(1, missedItems.size());
    }

    @Test
    void Y_입력시_무료_상품_장바구니에_추가() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31"));
                }
                return null;
            }
        };

        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        promotionChecker.checkResponse("Y");

        for (Product product : shoppingCart.getProducts()) {
            if (product.getName().equals("콜라")) {
                assertEquals(3, product.getQuantity());
            }
        }
    }



    @Test
    void N_입력시_아무것도_추가되지않음() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("2+1", 2, 1, "2024-01-01", "2024-12-31"));
                }
                return null;
            }
        };
        String input = "[콜라-2]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        promotionChecker.checkResponse("N");

        for (Product product : shoppingCart.getProducts()) {
            if (product.getName().equals("콜라")) {
                assertEquals(2, product.getQuantity());
            }
        }
    }

    @Test
    void 잘못된_입력시_예외발생() {
        productRepository = new ProductRepository() {
            public Product findByName(String name) {
                if ("콜라".equals(name)) {
                    return new Product("콜라", 1000, 10, new Promotion("2+1",2, 1,"2024-01-01","2024-12-31"));
                }
                return null;
            }
        };
        String input = "[콜라-5]";
        shoppingCart = new ShoppingCart(input, productRepository);
        promotionChecker = new PromotionChecker(shoppingCart);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            promotionChecker.checkResponse("Z");
        });

        assertEquals("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.", exception.getMessage());
    }
}
