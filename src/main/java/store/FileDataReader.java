package store;

import camp.nextstep.edu.missionutils.DateTimes;
import store.model.Product;
import store.model.Promotion;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileDataReader {

    public static void loadPromotions(String filename, PromotionRepository promotionRepository) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            bufferedReader.readLine();
            analyzeLine(promotionRepository, bufferedReader);
        }
    }

    private static void analyzeLine(PromotionRepository promotionRepository, BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            Promotion promotion = parsePromotion(line);
            if (isPromotionValid(promotion)) {
                promotionRepository.add(promotion);
            }
        }
    }

    private static boolean isPromotionValid(Promotion promotion) {
        if (promotion == null) {
            return false;
        }
        LocalDateTime today = DateTimes.now();
        LocalDate startDate = LocalDate.parse(promotion.getStartDate(), DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(promotion.getEndDate(), DateTimeFormatter.ISO_DATE);

        return !(today.toLocalDate().isBefore(startDate) || today.toLocalDate().isAfter(endDate));
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

    public static void loadProducts(String filename, ProductRepository productRepository, PromotionRepository promotionRepository) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                parseAndAddProduct(line, productRepository, promotionRepository);
            }
        }
    }

    private static void parseAndAddProduct(
            String line, ProductRepository productRepository, PromotionRepository promotionRepository) {
        String[] fields = line.split(",");
        String name = fields[0].trim();
        int price = Integer.parseInt(fields[1].trim());
        int quantity = Integer.parseInt(fields[2].trim());
        Promotion promotion = findPromotion(fields[3].trim(), promotionRepository);
        if (productRepository.findProductByName(name) != null) {
            updateProductQuantity(productRepository.findProductByName(name), quantity);
            return;
        }
        addNewProduct(name, price, quantity, promotion, productRepository);
    }

    private static void updateProductQuantity(Product existingProduct, int quantity) {
        existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
    }

    private static void addNewProduct(
            String name, int price, int quantity, Promotion promotion, ProductRepository productRepository) {
        productRepository.add(new Product(name, price, quantity, promotion));
        if (promotion != null && quantity > 0) {
            addRegularProduct(name, price, productRepository);
        }
    }

    private static void addRegularProduct(String name, int price, ProductRepository productRepository) {
        Product regularProduct = new Product(name, price, 0, null);
        productRepository.add(regularProduct);
    }

    private static Promotion findPromotion(String promotionName, PromotionRepository promotionRepository) {
        if (promotionName != null && !promotionName.isEmpty()) {
            return promotionRepository.findByName(promotionName);
        }
        return null;
    }
}
