package org.byovsiannikov.tasktheraven.service;


import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.byovsiannikov.tasktheraven.converter.CustomerConverter;
import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.entity.CustomerEntity;
import org.byovsiannikov.tasktheraven.exception.TryingToChangeEmailException;
import org.byovsiannikov.tasktheraven.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerConverter customerConverter;

    @Override
    public Customer createCustomer (Customer customer) {
        CustomerEntity customerEntityForSave = customerConverter.fromDto(customer);
        customerEntityForSave.setCreated(Instant.now().toEpochMilli());
        customerEntityForSave.setUpdated(Instant.now().toEpochMilli());
        customerEntityForSave.setActive(true);
        if (repository.existsByEmail(customer.getEmail())) {
            throw new EntityExistsException("Customer with email " + customer.getEmail() + " already exists");
        }
        CustomerEntity saved = repository.save(customerEntityForSave);
        log.info("Customer with id {} successfully saved", saved.getId());
        return customerConverter.toDto(saved);
    }

    @Override
    public List<Customer> readAllCustomers () {
        List<CustomerEntity> customerEntityList = repository.findAllByIsActiveTrue();
        log.info("Customers {} successfully retrieved", customerEntityList.size());
        return customerConverter.toDto(customerEntityList);
    }

    @Override
    public Customer readCustomerByID (Long id) {
        CustomerEntity customerWithActiveProfile = repository.findByIdAndIsActiveTrue(id);
        if (customerWithActiveProfile == null) {
            throw new EntityNotFoundException("Customer with id " + id + " not found");
        }
        log.info("Customer with id {} successfully retrieved", customerWithActiveProfile.getId());
        return customerConverter.toDto(customerWithActiveProfile);
    }

    @Override
    public Customer updateCustomerByID (Long id, Customer customer) {
        CustomerEntity customerWithActiveProfile = repository.findByIdAndIsActiveTrue(id);
        if (customerWithActiveProfile == null) {
            throw new EntityNotFoundException("Customer with id " + id + " not found");
        } else if (!customerWithActiveProfile.getEmail().equals(customer.getEmail())) {
            throw new TryingToChangeEmailException("Email can`t be changed ");
        }
        customerWithActiveProfile.setUpdated(Instant.now().toEpochMilli());
        customerWithActiveProfile.setPhone(customer.getPhone());
        customerWithActiveProfile.setFullName(customer.getFullName());
        CustomerEntity saved = repository.save(customerWithActiveProfile);
        log.info("Customer with id {} successfully updated", saved.getId());
        return customerConverter.toDto(saved);
    }

    @Override
    public String deleteCustomerByID (Long id) {
        CustomerEntity entityForDeactivation = repository.findByIdAndIsActiveTrue(id);
        if (entityForDeactivation == null) {
            throw new EntityNotFoundException("Customer with id " + id + " not found");
        }
        entityForDeactivation.setActive(false);
        repository.save(entityForDeactivation);
        log.info("Customer with id {} successfully disabled", id);
        return "Marked as deleted";
    }

}
