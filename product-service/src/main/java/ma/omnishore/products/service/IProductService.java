package ma.omnishore.products.service;

import java.util.List;

import ma.omnishore.products.domain.Product;

public interface IProductService {

    /**
     * Save a product.
     *
     * @param product
     *            the entity to save.
     * @return the persisted entity.
     */
    Product createProduct(Product product);

    /**
     * Update a product.
     *
     * @param product
     *            the entity to update.
     * @return the persisted entity.
     */
    Product updateProduct(Product product);

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    List<Product> getAllProducts();

    /**
     * Get one product by id.
     *
     * @param id
     *            the id of the entity.
     * @return the entity.
     */
    Product getProduct(Long id);

    /**
     * Get products by codes.
     *
     * @param codes
     *            the list of product codes.
     * @return the list of entities.
     */
    List<String> getSuppliersByProductCodes(List<String> codes);

    /**
     * Delete the product by id.
     *
     * @param id
     *            the id of the entity.
     */
    void deleteProduct(Long id);
}
