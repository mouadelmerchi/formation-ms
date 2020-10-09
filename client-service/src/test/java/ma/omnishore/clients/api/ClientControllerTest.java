package ma.omnishore.clients.api;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
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

import ma.omnishore.clients.api.feign.SalesClient;
import ma.omnishore.clients.config.WithMockOAuth2Conext;
import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.dto.SaleDto;
import ma.omnishore.clients.service.impl.ClientService;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientController controller;

    @MockBean
    private SalesClient salesClient;

    @MockBean
    private ClientService service;

    private final Gson gson = new Gson();

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @WithMockOAuth2Conext(authorities = "user")
    public void testGetClients() throws Exception {
        final Client client1 = new Client("Test1", "Test1", "test1@test.ma","Address 1");
        final Client client2 = new Client("Test2", "Test2", "test2@test.ma","Address 2");
        when(service.getAllClients()).thenReturn(Stream.of(client1, client2).collect(Collectors.toList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client").accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2))).andReturn();
    }

    @Test
    public void testGetClient() throws Exception {
        final Client client1 = new Client("Test1", "Test1", "test1@test.ma","Address 1");
        client1.setId(1L);
        when(service.getClient(1L)).thenReturn(client1);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/client/1").accept(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        JSONAssert.assertEquals(gson.toJson(client1), result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testCreateClient() throws Exception {
        final Client client1 = new Client("Test1", "Test1", "test1@test.ma","Address 1");
        when(service.createClient(any())).thenReturn(client1);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/client").accept(MediaType.APPLICATION_JSON).content(gson.toJson(client1)).contentType(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isCreated()).andReturn();

        JSONAssert.assertEquals(gson.toJson(client1), result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testUpdateClient() throws Exception {
        final Client client1 = new Client("Test1", "Test1", "test1@test.ma","Address 1");
        client1.setId(1L);
        final Client client2 = new Client("Test2", "Test2", "test2@test.ma","Address 2");;
        client2.setId(1L);
        when(service.updateClient(any())).thenReturn(client2);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/client").accept(MediaType.APPLICATION_JSON).content(gson.toJson(client2)).contentType(MediaType.APPLICATION_JSON);
        final MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

        JSONAssert.assertEquals(gson.toJson(client2), result.getResponse().getContentAsString(), false);

    }

    @Test
    public void testDeleteClient() throws Exception {
        doNothing().when(service).deleteClient(1L);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/client/1").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void testGetClientSales() throws Exception {

        final SaleDto saleDto1 = new SaleDto("Code1", 1L, new Date(), 3L, 15.0d);
        final SaleDto saleDto2 = new SaleDto("Code2", 2L, new Date(), 2L, 35.0d);

        when(salesClient.getClientSales(1L)).thenReturn(Stream.of(saleDto1).collect(Collectors.toList()));
        when(salesClient.getClientSales(2L)).thenReturn(Stream.of(saleDto2).collect(Collectors.toList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client/1/sales").accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(1))).andReturn();
    }

    @Test
    public void testGetClientSuppliers() throws Exception {

        when(salesClient.getClientSuppliers(1L)).thenReturn(Stream.of("Supplier 1", "Supplier 3").collect(Collectors.toList()));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/client/1/suppliers").accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$", hasSize(2))).andReturn();
    }
}
