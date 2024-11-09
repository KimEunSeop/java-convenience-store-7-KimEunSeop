package store;

import store.controller.StoreController;

import java.time.LocalDate;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        StoreController storeController = new StoreController();
        storeController.start();
    }
}
