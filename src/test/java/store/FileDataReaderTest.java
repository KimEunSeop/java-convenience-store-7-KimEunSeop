package store;

import org.junit.jupiter.api.AfterEach;
import store.Service.FileDataReader;
import store.model.Product;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {

    private PromotionRepository promotionRepository;
    private ProductRepository productRepository;
    private FileDataReader fileDataReader;

    private File promotionFile;
    private File productFile;

    @BeforeEach
    void setUp() throws IOException {
        promotionRepository = new PromotionRepository();
        productRepository = new ProductRepository();
        fileDataReader = new FileDataReader();

        promotionFile = createTempFile("promotions.csv",
                "name,buy,get,start_date,end_date\n" +
                        "탄산2+1,2,1,2024-01-01,2024-12-31\n" +
                        "MD추천상품,1,1,2024-01-01,2024-12-31\n"
        );

        productFile = createTempFile("products.csv",
                "name,price,quantity,promotion\n" +
                        "콜라,1000,10,탄산2+1\n" +
                        "사이다,1000,8,MD추천상품\n"
        );
    }

    private File createTempFile(String filename, String content) throws IOException {
        File tempFile = new File(filename);
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    @AfterEach
    void tearDown() {
        if (promotionFile.exists()) {
            promotionFile.delete();
        }
        if (productFile.exists()) {
            productFile.delete();
        }
    }

    @Test
    void 파일에서_읽어올_때_프로모션이_저장된다() throws IOException {
        fileDataReader.loadPromotions(promotionFile.getAbsolutePath(), promotionRepository);
        assertNotNull(promotionRepository.findByName("탄산2+1"));
        assertNotNull(promotionRepository.findByName("MD추천상품"));
    }

    @Test
    void 파일에서_읽어올_때_제품이_저장된다() throws IOException {
        fileDataReader.loadProducts(productFile.getAbsolutePath(), productRepository, promotionRepository);
        Product product1 = productRepository.findByName("콜라").getFirst();
        Product product2 = productRepository.findByName("사이다").getFirst();
        assertNotNull(product1);
        assertNotNull(product2);
        assertEquals("콜라", product1.getName());
        assertEquals("사이다", product2.getName());
    }
}
