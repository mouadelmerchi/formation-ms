package ma.omnishore.clients.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.ApiOperation;
import ma.omnishore.clients.api.feign.SalesClient;
import ma.omnishore.clients.domain.Client;
import ma.omnishore.clients.dto.SaleDto;
import ma.omnishore.clients.service.IClientService;

/**
 * Client controller.
 **/
@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @Autowired
    private SalesClient salesClient;

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    // -------------------get All Clients-------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('user')")
    @ApiOperation("Get clients list")
    public ResponseEntity<List<Client>> findAll() {
        log.info("Returning client list from database.");
        final List<Client> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    // -------------------Retrieve Single Client------------------------------------
    @GetMapping(value = "/{id}")
    @ApiOperation("Get a client by id")
    public ResponseEntity<Client> getClient(@PathVariable("id") long id) {
        final Client client = clientService.getClient(id);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // -------------------Create a Client-------------------------------------------
    @PostMapping
    @ApiOperation("Create a new client")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        client = clientService.createClient(client);
        return new ResponseEntity<>(client, HttpStatus.CREATED);
    }

    // ------------------- Update a Client ------------------------------------------------
    @PutMapping
    @ApiOperation("Edit an existing client")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        clientService.updateClient(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    // ------------------- Delete a Client-----------------------------------------
    @DeleteMapping(value = "/{id}")
    @ApiOperation("Delete a client")
    public ResponseEntity<Client> deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ------------------- Get Client Sales-------------------------------------------
    @CircuitBreaker(name = "sales-service", fallbackMethod = "salesFailed")
    @GetMapping(value = "/{id}/sales")
    @ApiOperation("Get a client's sales list")
    public ResponseEntity<List<SaleDto>> getClientSales(@PathVariable("id") long id) {
        log.info("Returning client sales list from database.");
        final List<SaleDto> sales = salesClient.getClientSales(id);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    public ResponseEntity<List<SaleDto>> salesFailed(long id, Throwable throwable) {
        log.info("Default outcome for sales of client {}.", id);
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    // ------------------- Get Client Suppliers-------------------------------------------
    @GetMapping(value = "/{id}/suppliers")
    @ApiOperation("Get a client's suppliers list")
    public ResponseEntity<List<String>> getClientSuppliers(@PathVariable("id") long id) {
        log.info("Returning client sales list from database.");
        final List<String> suppliers = salesClient.getClientSuppliers(id);
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }
}
