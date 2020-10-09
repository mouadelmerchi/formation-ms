package ma.omnishore.sales.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface SuppliersProduct {

    @PostMapping("/api/product/suppliers")
    List<String> getProductsSuppliers(@RequestBody List<String> codes);
}
