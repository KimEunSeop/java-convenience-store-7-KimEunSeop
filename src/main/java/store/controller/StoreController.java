package store.controller;

import store.FileDataReader;
import store.ShoppingCart;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;

public class StoreController {
    final OutputView outputView = new OutputView();
    final InputView inputView = new InputView();
    final PromotionRepository promotionRepository = new PromotionRepository();
    final ProductRepository productRepository = new ProductRepository();

    public void start() {
        set();
        outputView.printWelcomeGuide();
        outputView.printItemList(productRepository.getProductsAsString());
        outputView.printPurchaseGuide();
        ShoppingCart shoppingCart = new ShoppingCart(inputView.getResponse(), productRepository);
    }

    private void set() {
        FileDataReader fileDataReader = new FileDataReader();
        try {
            fileDataReader.loadPromotions("src/main/resources/promotions.md", promotionRepository);
            fileDataReader.loadProducts("src/main/resources/products.md", productRepository, promotionRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
