package store;

import store.controller.StoreController;

import java.time.LocalDate;

public class Application {

    public static final String YES = "Y";
    public static final String NO = "N";

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        StoreController storeController = new StoreController();
        storeController.start();
    }
}
