package store.repository;

import store.model.Promotion;

import java.util.HashMap;
import java.util.Map;

public class PromotionRepository {
    private Map<String, Promotion> promotions = new HashMap<>();

    public void add(Promotion promotion) {
        promotions.put(promotion.getName(), promotion);
    }

    public Promotion findByName(String name) {
        return promotions.get(name);
    }
}
