package org.byovsiannikov.tasktheraven.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;
import org.byovsiannikov.tasktheraven.converter.CustomerConverter;
import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.entity.CustomerEntity;
import org.byovsiannikov.tasktheraven.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private CustomerConverter customerConverter;

    @Mock
    private Validator validator;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp () {
        customer = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("1234567890").build();
        customerEntity = CustomerEntity.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("1234567890").created(Instant.now().toEpochMilli()).updated(Instant.now().toEpochMilli()).isActive(true).build();
    }

    @Test
    void createCustomer () {
        when(customerConverter.fromDto(any(Customer.class))).thenReturn(customerEntity);
        when(repository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        when(customerConverter.toDto(any(CustomerEntity.class))).thenReturn(customer);

        Customer result = customerService.createCustomer(customer);
        assertEquals(customer.getId(), result.getId());
        assertEquals(customer.getFullName(), result.getFullName());

        verify(repository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void createCustomer_EmailAlreadyExists () {
        when(customerConverter.fromDto(any(Customer.class))).thenReturn(customerEntity);
        when(repository.existsByEmail(any(String.class))).thenReturn(true);
        assertThrows(EntityExistsException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    void readAllCustomers () {
        when(repository.findAllByIsActiveTrue()).thenReturn(Collections.singletonList(customerEntity));
        when(customerConverter.toDto(anyList())).thenReturn(Collections.singletonList(customer));

        List<Customer> result = customerService.readAllCustomers();
        assertEquals(1, result.size());
        assertEquals(customer.getFullName(), result.get(0).getFullName());

        verify(repository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void readCustomerByID () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(customerEntity);
        when(customerConverter.toDto(any(CustomerEntity.class))).thenReturn(customer);

        Customer result = customerService.readCustomerByID(1L);
        assertEquals(customer.getId(), result.getId());

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
    }

    @Test
    void readCustomerByID_NotFound () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> customerService.readCustomerByID(1L));

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
    }

    @Test
    void updateCustomerByID () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(customerEntity);
        when(customerConverter.fromDto(any(Customer.class))).thenReturn(customerEntity);
        when(repository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        when(customerConverter.toDto(any(CustomerEntity.class))).thenReturn(customer);

        Customer result = customerService.updateCustomerByID(1L, customer);
        assertEquals(customer.getId(), result.getId());

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
        verify(repository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void updateCustomerByID_NotFound () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(null);
        when(customerConverter.fromDto(any(Customer.class))).thenReturn(customerEntity);
        assertThrows(EntityNotFoundException.class, () -> customerService.updateCustomerByID(1L, customer));

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
    }

    @Test
    void deleteCustomerByID () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(customerEntity);

        String result = customerService.deleteCustomerByID(1L);
        assertEquals("Marked as deleted", result);

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
        verify(repository, times(1)).save(any(CustomerEntity.class));
    }

    @Test
    void deleteCustomerByID_NotFound () {
        when(repository.findByIdAndIsActiveTrue(anyLong())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> customerService.deleteCustomerByID(1L));

        verify(repository, times(1)).findByIdAndIsActiveTrue(anyLong());
    }
}
