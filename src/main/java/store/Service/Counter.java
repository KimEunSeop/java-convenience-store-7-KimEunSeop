package store.Service;

import store.model.Product;
import store.repository.ProductRepository;

import java.util.List;

public class Counter {
    private final ProductRepository productRepository;

    public Counter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public int getRemainPromotionProductStock(String name, List<Product> promotionProducts) {
        int promotionProductStock = productRepository.findPromotionProductStockByName(name);

        for (Product product : promotionProducts) {
            if (product.getName().equals(name)) {
                promotionProductStock -= product.getQuantity();
            }
        }
        return promotionProductStock;
    }

    public int getRemainProductStock(String name, List<Product> products) {
        int productStock = productRepository.findProductStockByName(name);

        for (Product product : products) {
            if (product.getName().equals(name)) {
                productStock -= product.getQuantity();
            }
        }
        return productStock;
    }

    public void buyPromotionProducts(List<Product> promotionProducts) {
        for (Product promotionProduct : promotionProducts) {
            Product repositoryProduct = productRepository.findPromotionProductByName(promotionProduct.getName());
            repositoryProduct.setQuantity(getRemainPromotionProductStock(promotionProduct.getName(), promotionProducts));
        }
    }

    public void buyProducts(List<Product> products) {
        for (Product product : products) {
            Product repositoryProduct = productRepository.findProductByName(product.getName());
            repositoryProduct.setQuantity(getRemainProductStock(product.getName(), products));
        }
    }
}
