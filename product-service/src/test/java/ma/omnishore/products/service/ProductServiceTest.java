package ma.omnishore.products.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ma.omnishore.products.ProductServiceApplication;
import ma.omnishore.products.domain.Product;
import ma.omnishore.products.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ProductServiceApplication.class })
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IProductService productService;

    private Long randomId;

    @Before
    public void createProducts() {
        final Product randomProduct1 = new Product("Code1", "Product Description 1", "Supplier 1");
        final Product randomProduct2 = new Product("Code2", "Product Description 2", "Supplier 2");
        final Product randomProduct3 = new Product("Code3", "Product Description 3", "Supplier 3");
        final Product randomProduct4 = new Product("Code4", "Product Description 3", "Supplier 1");
        randomId = productRepository.save(randomProduct2).getId();
        productRepository.saveAll(Arrays.asList(randomProduct1, randomProduct3, randomProduct4));
    }

    @After
    public void deleteProducts() {
        productRepository.deleteAll();
    }

    @Test
    public void verifyGetAllProducts() {
        final List<Product> products = productService.getAllProducts();
        assertThat(products, hasSize(4));
    }

    @Test
    public void testGetProduct() throws Exception {
        final Product product = productService.getProduct(randomId);
        assertNotNull(product.getId());
    }

    @Test
    public void testGetProductsByCodeList() throws Exception {
        final List<String> codes = new ArrayList<>();
        codes.add("Code1");
        codes.add("Code3");
        codes.add("Code4");
        final List<String> products = productService.getSuppliersByProductCodes(codes);
        assertThat(products, hasSize(2));
    }

    @Test
    public void testCreateProduct() throws Exception {
        final Product randomProduct4 = new Product("Code4", "Product Description 4", "Supplier 4");
        final Product product = productService.createProduct(randomProduct4);
        assertNotNull(product.getId());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        final Product randomProduct5 = new Product("Code5", "Product Description 5", "Supplier 5");
        randomProduct5.setId(randomId);
        final Product product = productService.updateProduct(randomProduct5);
        assertEquals(product.getCode(), randomProduct5.getCode());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        productService.deleteProduct(randomId);
    }

}
