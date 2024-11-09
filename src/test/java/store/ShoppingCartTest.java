package store;

import store.model.Product;
import store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productRepository.add(new Product("콜라", 1000, 10, null));
        productRepository.add(new Product("사이다", 1000, 8, null));
    }

    @Test
    void 장바구니에_상품을_정상적으로_추가할_때() {
        String input = "[콜라-5]";
        ShoppingCart shoppingCart = new ShoppingCart(input, productRepository);

        assertEquals(1, shoppingCart.getProducts().size());
        assertEquals("콜라", shoppingCart.getProducts().get(0).getName());
        assertEquals(5, shoppingCart.getProducts().get(0).getQuantity());
    }

    @Test
    void 존재하지_않는_상품을_구매할_때_예외가_발생한다() {
        String input = "[존재하지_않는_상품-1]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ShoppingCart(input, productRepository);
        });

        assertEquals("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 재고를_초과하는_수량으로_구매할_때_예외가_발생한다() {
        String input = "[콜라-15]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ShoppingCart(input, productRepository);
        });

        assertEquals("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 잘못된_형식으로_입력했을_때_예외가_발생한다() {
        String input = "[콜라10]";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new ShoppingCart(input, productRepository);
        });

        assertEquals("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.", exception.getMessage());
    }

    @Test
    void 여러_상품을_장바구니에_정상적으로_추가할_때() {
        String input = "[콜라-5],[사이다-3]";
        ShoppingCart shoppingCart = new ShoppingCart(input, productRepository);

        assertEquals(2, shoppingCart.getProducts().size());
        assertEquals("콜라", shoppingCart.getProducts().get(0).getName());
        assertEquals(5, shoppingCart.getProducts().get(0).getQuantity());
        assertEquals("사이다", shoppingCart.getProducts().get(1).getName());
        assertEquals(3, shoppingCart.getProducts().get(1).getQuantity());
    }
}
