package ma.omnishore.products.api;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import ma.omnishore.products.domain.Product;
import ma.omnishore.products.service.impl.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductController controller;

    @MockBean
    private ProductService service;

    private final Gson gson = new Gson();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testGetproducts() throws Exception {
        final Product product1 = new Product("Code1", "Product Description 1", "Supplier 1");
        final Product product2 = new Product("Code2", "Product Description 2", "Supplier 2");
        when(service.getAllProducts()).thenReturn(Stream.of(product1, product2).collect(Collectors.toList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product").accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2))).andReturn();
    }

    @Test
    public void testGetProduct() throws Exception {
        final Product product1 = new Product("Code1", "Product Description 1", "Supplier 1");
        product1.setId(1L);
        when(service.getProduct(1L)).thenReturn(product1);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/product/1").accept(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        JSONAssert.assertEquals(gson.toJson(product1), result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetProductsByCodes() throws Exception {
        final Product product1 = new Product("Code1", "Product Description 1", "Supplier 1");
        final Product product3 = new Product("Code3", "Product Description 3", "Supplier 3");

        final List<String> suppliers = Stream.of(product1, product3).map(p -> p.getSupplier()).collect(Collectors.toList());
        when(service.getSuppliersByProductCodes(any())).thenReturn(suppliers);

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product/list/Code1,Code3").accept(MediaType.APPLICATION_JSON)).andReturn();

        final String content = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(gson.toJson(suppliers), content, false);
    }

    @Test
    public void testCreateProduct() throws Exception {
        final Product product1 = new Product("Code1", "Product Description 1", "Supplier 1");
        when(service.createProduct(any())).thenReturn(product1);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/product").accept(MediaType.APPLICATION_JSON).content(gson.toJson(product1)).contentType(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        JSONAssert.assertEquals(gson.toJson(product1), result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testUpdateClient() throws Exception {
        final Product product2 = new Product("Code5", "Product Description 5", "Supplier 5");
        product2.setId(1L);
        when(service.updateProduct(any())).thenReturn(product2);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/product").accept(MediaType.APPLICATION_JSON).content(gson.toJson(product2)).contentType(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        JSONAssert.assertEquals(gson.toJson(product2), result.getResponse().getContentAsString(), false);

    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(service).deleteProduct(1L);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/product/1").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

}
