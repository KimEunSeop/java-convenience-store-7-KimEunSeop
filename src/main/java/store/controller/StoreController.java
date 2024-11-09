package store.controller;

import store.FileDataReader;
import store.PromotionChecker;
import store.ShoppingCart;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class StoreController {
    public static final int EMPTY = 0;
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private ShoppingCart shoppingCart;
    PromotionChecker promotionChecker;

    public void start() {
        set();
        outputView.printWelcomeGuide();
        outputView.printItemList(productRepository.getProductsAsString());
        process(this::inputShoppingCart);
        promotionChecker = new PromotionChecker(shoppingCart);
        if(promotionChecker.findMissedItems().size() != EMPTY) {
            process(this::inputMissedItem);
        }
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

    private void inputShoppingCart() {
        try {
            outputView.printPurchaseGuide();
            shoppingCart = new ShoppingCart(inputView.getResponse(), productRepository);
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputShoppingCart);
        }
    }

    private void inputMissedItem() {
        try {
            Map<String, Integer> missedItems = promotionChecker.findMissedItems();
            for (String key : missedItems.keySet()) {
                outputView.printGetMissedItemGuide(key, missedItems.get(key));
                outputView.printYOrN();
            }
            promotionChecker.checkResponse(inputView.getResponse());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputShoppingCart);
        }
    }

    private void process(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(action);
        }
    }
}
