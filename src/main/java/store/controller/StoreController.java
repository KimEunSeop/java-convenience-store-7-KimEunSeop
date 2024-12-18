package store.controller;

import store.*;
import store.Service.FileDataReader;
import store.Service.PriceCalculator;
import store.Service.PromotionService;
import store.Service.ShoppingCart;
import store.model.Product;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static store.Application.NO;
import static store.Application.YES;

public class StoreController {
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    public static final int EMPTY = 0;
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private ShoppingCart shoppingCart;
    private PromotionService promotionService;
    private PriceCalculator priceCalculator;

    public void start() {
        set();
        while (true) {
            pickItems();
            checkItems();
            calculate();
            buy();
            if (!checkPurchaseAdditional()) {
                break;
            }
        }
    }

    private void set() {
        FileDataReader fileDataReader = new FileDataReader();
        try {
            fileDataReader.loadPromotions(PROMOTIONS_FILE_PATH, promotionRepository);
            fileDataReader.loadProducts(PRODUCTS_FILE_PATH, productRepository, promotionRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pickItems() {
        outputView.printWelcomeGuide();
        outputView.printItemList(productRepository.getProductsAsString());
        process(this::inputShoppingCart);
    }

    private void checkItems() {
        promotionService = new PromotionService(shoppingCart);
        if (promotionService.findMissedItems().size() != EMPTY) {
            process(this::inputMissedItem);
        }
        if (promotionService.checkExclude(productRepository).size() != EMPTY) {
            process(this::inputExclude);
        }
    }

    private void calculate() {
        priceCalculator = new PriceCalculator(shoppingCart);
        priceCalculator.calculateTotal();
        priceCalculator.calculatePromotions();
        process(this::inputMembership);
    }

    private void buy() {
        shoppingCart.buy();
        Map<String, Integer> freeItems = promotionService.getFreeItems();
        List<Product> allProducts = shoppingCart.getAllProducts();
        int totalPrice = priceCalculator.getTotalPrice();
        int promotionDiscount = priceCalculator.getPromotionDiscount();
        int membershipDiscount = priceCalculator.getMembershipDiscount();
        int finalAmount = priceCalculator.getFinalprice();

        outputView.printReceipt(freeItems, allProducts, totalPrice, promotionDiscount, membershipDiscount, finalAmount);
    }

    private boolean checkPurchaseAdditional() {
        while (true) {
            try {
                outputView.printFinishGuide();
                outputView.printYOrN();
                return checkInput();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean checkInput() {
        String answer = inputView.getResponse();
        if (YES.equalsIgnoreCase(answer)) {
            return true;
        }
        if (NO.equalsIgnoreCase(answer)) {
            return false;
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_Y_OR_N_INPUT.getMessage());
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
            Map<String, Integer> missedItems = promotionService.findMissedItems();
            for (String key : missedItems.keySet()) {
                outputView.printGetMissedItemGuide(key, missedItems.get(key));
                outputView.printYOrN();
            }
            promotionService.checkMissedItemsResponse(inputView.getResponse());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputMissedItem);
        }
    }

    private void inputExclude() {
        try {
            Map<String, Integer> excludeItems = promotionService.checkExclude(productRepository);
            for (String key : excludeItems.keySet()) {
                outputView.printExcludeGuide(key, excludeItems.get(key));
                outputView.printYOrN();
            }
            promotionService.checkExcludeItemsResponse(inputView.getResponse());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputExclude);
        }
    }

    private void inputMembership() {
        try {
            outputView.printMembershipDiscountGuide();
            outputView.printYOrN();
            priceCalculator.calculatemembershipDiscount(inputView.getResponse());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputMembership);
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
