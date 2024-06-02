package org.byovsiannikov.tasktheraven.service;

import org.byovsiannikov.tasktheraven.dto.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer (Customer customer);

    List<Customer> readAllCustomers ();

    Customer readCustomerByID (Long id);

    Customer updateCustomerByID (Long id, Customer customer);

    String deleteCustomerByID (Long id);
}
