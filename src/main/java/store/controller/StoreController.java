package store.controller;

import store.FileDataReader;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;

public class StoreController {
    final OutputView outputView = new OutputView();
    final PromotionRepository promotionRepository = new PromotionRepository();
    final ProductRepository productRepository = new ProductRepository();

    public void start() {
        set();
        outputView.printWelcomeGuide();
        outputView.printItemList(productRepository.getProductsAsString());
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
