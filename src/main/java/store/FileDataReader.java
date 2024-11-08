package store;

import store.model.Product;
import store.model.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDataReader {

    public static void loadPromotions(String filename, PromotionRepository promotionRepository) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Promotion promotion = parsePromotion(line);
                promotionRepository.add(promotion);
            }
        }
    }

    private static Promotion parsePromotion(String line) {
        String[] fields = line.split(",");
        return new Promotion(
                fields[0].trim(),
                Integer.parseInt(fields[1].trim()),
                Integer.parseInt(fields[2].trim()),
                fields[3].trim(),
                fields[4].trim()
        );
    }

    public static void loadProducts(
            String filename, ProductRepository productRepository, PromotionRepository promotionRepository
    ) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                Product product = parseProduct(line, promotionRepository);
                productRepository.add(product);
            }
        }
    }

    private static Product parseProduct(String line, PromotionRepository promotionRepository) {
        String[] fields = line.split(",");
        String name = fields[0].trim();
        int price = Integer.parseInt(fields[1].trim());
        int quantity = Integer.parseInt(fields[2].trim());
        Promotion promotion = findPromotion(fields[3].trim(), promotionRepository);

        return new Product(name, price, quantity, promotion);
    }


    private static Promotion findPromotion(String promotionName, PromotionRepository promotionRepository) {
        if (promotionName != null && !promotionName.isEmpty()) {
            return promotionRepository.findByName(promotionName);
        } else {
            return null;
        }
    }
}