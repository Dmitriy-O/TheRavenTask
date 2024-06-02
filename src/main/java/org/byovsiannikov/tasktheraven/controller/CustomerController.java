package org.byovsiannikov.tasktheraven.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customers")
    public ResponseEntity<Customer> createCustomer (@RequestBody @Valid Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers () {
        return ResponseEntity.ok(customerService.readAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<Customer> getCustomerByID (@PathVariable(name = "id") Long findId) {
        return ResponseEntity.ok(customerService.readCustomerByID(findId));
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<Customer> updateCustomerByID (@PathVariable(name = "id") Long id,
            @Valid @RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomerByID(id, customer));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<String> deleteCustomerByID (@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(customerService.deleteCustomerByID(id));
    }
}
