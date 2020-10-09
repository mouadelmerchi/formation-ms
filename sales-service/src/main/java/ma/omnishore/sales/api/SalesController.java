package ma.omnishore.sales.api;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ma.omnishore.sales.api.feign.SuppliersProduct;
import ma.omnishore.sales.domain.Sale;
import ma.omnishore.sales.service.ISalesService;

/**
 * Sale controller.
 **/
@RestController
@RequestMapping("/api/sales")
public class SalesController {

    @Autowired
    private ISalesService salesService;

    @Autowired
    private SuppliersProduct suppliersProduct;

    private static final Logger log = LoggerFactory.getLogger(SalesController.class);

    // -------------------get All Sales-------------------------------------------
    @GetMapping
    @ApiOperation("Get sales list")
    public ResponseEntity<List<Sale>> findAll() {
        log.info("Returning sale list from database.");
        final List<Sale> sales = salesService.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    // -------------------Retrieve Single Sale------------------------------------
    @GetMapping(value = "/{id}")
    @ApiOperation("Get a sale by id")
    public ResponseEntity<Sale> getSale(@PathVariable("id") long id) {
        final Sale sale = salesService.getSale(id);
        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    // -------------------Retrieve Sales By ClientId------------------------------------
    @GetMapping(value = "/client/{id}")
    @ApiOperation("Get sales by client id")
    public ResponseEntity<List<Sale>> getSalesByClient(@PathVariable("id") long id) {
        log.info("Returning sale list for client by id {} from database.", id);
        final List<Sale> sales = salesService.getSalesByClient((id));
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    // -------------------Create a Sale-------------------------------------------
    @PostMapping
    @ApiOperation("Create a new sale")
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        sale = salesService.createSale(sale);
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }

    // ------------------- Delete a Sale-----------------------------------------
    @DeleteMapping(value = "/{id}")
    @ApiOperation("Delete a sale by id")
    public ResponseEntity<Sale> deleteSale(@PathVariable("id") long id) {
        salesService.deleteSale(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ------------------- Get Client Suppliers-------------------------------------------
    //@CircuitBreaker(name = "product-service", fallbackMethod = "suppliersFailed")
    @GetMapping(value = "/client/{id}/suppliers")
    @ApiOperation("Get suppliers by client id")
    public ResponseEntity<List<String>> getSuppliersByClient(@PathVariable("id") long id) {
        log.info("Returning client suppliers list from database.");

        final List<String> productCodes = salesService.getProductCodesByClient((id));

        final List<String> suppliers = suppliersProduct.getProductsSuppliers(productCodes);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    public ResponseEntity<List<String>> suppliersFailed(long id, Throwable throwable) {
        log.info("Default outcome for suppliers of client {}.", id);
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
}
