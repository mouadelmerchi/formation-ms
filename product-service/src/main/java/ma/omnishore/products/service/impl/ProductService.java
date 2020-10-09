package ma.omnishore.products.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.omnishore.products.domain.Product;
import ma.omnishore.products.repository.ProductRepository;
import ma.omnishore.products.service.IProductService;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService implements IProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Product a product.
     * @param product  the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Product createProduct(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Product a product.
     * @param product  the entity to update.
     * @return the persisted entity.
     */
    @Override
    public Product updateProduct(Product product) {
        if(!productRepository.existsById(product.getId())) {
            return null;
        }
        log.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Get all the products.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        log.debug("Request to get all Products");
        return productRepository.findAll();
    }

    /**
     * Get one product by id.
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Get suppliers by product codes.
     *
     * @param codes  list of code.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> getSuppliersByProductCodes(List<String> codes){
        log.debug("Request to get Products By Product Codes : {}", codes);
        return productRepository.findProductsByCodeList(codes);
    }

    /**
     * Delete the product by id.
     * @param id the id of the entity.
     */
    @Override
    public void deleteProduct(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
