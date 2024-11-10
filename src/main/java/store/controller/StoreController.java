package store.controller;

import store.FileDataReader;
import store.PriceCalculator;
import store.PromotionChecker;
import store.ShoppingCart;
import store.model.Product;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.view.InputView;
import store.view.OutputView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StoreController {
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    public static final int EMPTY = 0;
    private final OutputView outputView = new OutputView();
    private final InputView inputView = new InputView();
    private final PromotionRepository promotionRepository = new PromotionRepository();
    private final ProductRepository productRepository = new ProductRepository();
    private ShoppingCart shoppingCart;
    private PromotionChecker promotionChecker;
    private PriceCalculator priceCalculator;

    public void start() {
        set();
        while(true){
            outputView.printWelcomeGuide();
            outputView.printItemList(productRepository.getProductsAsString());
            process(this::inputShoppingCart);
            promotionChecker = new PromotionChecker(shoppingCart);
            if (promotionChecker.findMissedItems().size() != EMPTY) {
                process(this::inputMissedItem);
            }
            if (promotionChecker.checkPromotionQuantity(productRepository).size() != EMPTY) {
                process(this::inputExclude);
            }
            priceCalculator = new PriceCalculator(shoppingCart);
            priceCalculator.calculateTotal();
            priceCalculator.calculatePromotions();
            process(this::inputMembership);
            buy();
            if (!checkPurchaseAdditional()) {
                break;
            }
        }
    }

    private boolean checkPurchaseAdditional() {
        while(true){
            try {
                outputView.printFinsihGuide();
                outputView.printYOrN();
                return checkInput();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean checkInput() {
        String answer = inputView.getResponse();
        if ("Y".equalsIgnoreCase(answer)) {
            return true;
        }
        if ("N".equalsIgnoreCase(answer)) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] 문자 Y나 N를 입력해야 합니다. 다시 입력해 주세요.");
    }

    private void buy() {
        shoppingCart.buy();
        Map<String, Integer> freeItems = promotionChecker.getFreeItems();
        List<Product> allProducts = shoppingCart.getAllProducts();
        int totalPrice = priceCalculator.getTotalPrice();
        int promotionDiscount = priceCalculator.getPromotionDiscount();
        int membershipDiscount = priceCalculator.getMembershipDiscount();
        int finalAmount = priceCalculator.getFinalprice();

        outputView.printReceipt(freeItems, allProducts, totalPrice, promotionDiscount, membershipDiscount, finalAmount);
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
            promotionChecker.checkMissedItemsResponse(inputView.getResponse());
        } catch (IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());
            process(this::inputMissedItem);
        }
    }

    private void inputExclude() {
        try {
            Map<String, Integer> exceedItems = promotionChecker.checkPromotionQuantity(productRepository);
            for (String key : exceedItems.keySet()) {
                outputView.printExcludeGuide(key, exceedItems.get(key));
                outputView.printYOrN();
            }
            promotionChecker.checkExceedItemsResponse(inputView.getResponse());
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
