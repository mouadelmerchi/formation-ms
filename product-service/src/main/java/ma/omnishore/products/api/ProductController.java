package ma.omnishore.products.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ma.omnishore.products.domain.Product;
import ma.omnishore.products.service.IProductService;

/**
 * Product controller.
 **/
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IProductService productService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    // -------------------get All Products-------------------------------------------
    @GetMapping
    @ApiOperation("Get all prodcuts")
    public ResponseEntity<List<Product>> findAll() {
        log.info("Returning product list from database.");
        final List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // -------------------Retrieve Single Product------------------------------------
    @GetMapping(value = "/{id}")
    @ApiOperation("Get a single product by id")
    public ResponseEntity<Product> getProduct(@PathVariable("id") long id) {
        final Product product = productService.getProduct(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // -------------------Retrieve Suppliers By Product Codes------------------------------------
    @PostMapping(value = "/suppliers", consumes = "application/json")
    @ApiOperation("Get product list by product codes")
    public ResponseEntity<List<String>> getProductsByCodes(@RequestBody List<String> codes) {
        log.info("Returning suppliers list by product code list {} from database.", codes);
        final List<String> suppliers = productService.getSuppliersByProductCodes(codes);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    // -------------------Create a Product-------------------------------------------
    @PostMapping
    @ApiOperation("Create a new product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        product = productService.createProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    // -------------------Update a Product-------------------------------------------
    @PutMapping
    @ApiOperation("Edit an existing product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        product = productService.updateProduct(product);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    // ------------------- Delete a Product-----------------------------------------
    @DeleteMapping(value = "/{id}")
    @ApiOperation("Delete a product by id")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
