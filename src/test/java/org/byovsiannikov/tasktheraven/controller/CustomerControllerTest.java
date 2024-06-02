package org.byovsiannikov.tasktheraven.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreateCustomer () throws Exception {
        Customer customer = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("+123456789").build();

        Mockito.when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.fullName", is("John Doe"))).andExpect(jsonPath("$.email", is("john.doe@example.com"))).andExpect(jsonPath("$.phone", is("+123456789")));
    }

    @Test
    public void shouldGetAllCustomers () throws Exception {
        Customer customer1 = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("+123456789").build();

        Customer customer2 = Customer.builder().id(2L).fullName("Jane Doe").email("jane.doe@example.com").phone("+987654321").build();

        Mockito.when(customerService.readAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].fullName", is("John Doe"))).andExpect(jsonPath("$[0].email", is("john.doe@example.com"))).andExpect(jsonPath("$[0].phone", is("+123456789"))).andExpect(jsonPath("$[1].id", is(2))).andExpect(jsonPath("$[1].fullName", is("Jane Doe"))).andExpect(jsonPath("$[1].email", is("jane.doe@example.com"))).andExpect(jsonPath("$[1].phone", is("+987654321")));
    }

    @Test
    public void shouldGetCustomerById () throws Exception {
        Customer customer = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("+123456789").build();

        Mockito.when(customerService.readCustomerByID(anyLong())).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.fullName", is("John Doe"))).andExpect(jsonPath("$.email", is("john.doe@example.com"))).andExpect(jsonPath("$.phone", is("+123456789")));
    }

    @Test
    public void shouldUpdateCustomerById () throws Exception {
        Customer customer = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("+123456789").build();

        Mockito.when(customerService.updateCustomerByID(anyLong(), any(Customer.class))).thenReturn(customer);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1))).andExpect(jsonPath("$.fullName", is("John Doe"))).andExpect(jsonPath("$.email", is("john.doe@example.com"))).andExpect(jsonPath("$.phone", is("+123456789")));
    }

    @Test
    public void shouldDeleteCustomerById () throws Exception {
        Mockito.when(customerService.deleteCustomerByID(anyLong())).thenReturn("Customer deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$", is("Customer deleted successfully")));
    }

    @Test
    public void shouldReturnNotFoundWhenCustomerDoesNotExist () throws Exception {
        Mockito.when(customerService.readCustomerByID(anyLong())).thenThrow(new EntityNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpect(jsonPath("$", is("Customer not found")));
    }

    @Test
    public void shouldReturnConflictWhenCustomerAlreadyExists () throws Exception {
        Customer customer = Customer.builder().id(1L).fullName("John Doe").email("john.doe@example.com").phone("+123456789").build();

        Mockito.when(customerService.createCustomer(any(Customer.class))).thenThrow(new EntityExistsException());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))).andExpect(status().isConflict()).andExpect(jsonPath("$", is("Customer already exists")));
    }

    @Test
    public void shouldReturnBadRequestWhenCreatingInvalidCustomer () throws Exception {
        Customer invalidCustomer = Customer.builder().id(1L).fullName("J").email("invalid-email").phone("123").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidCustomer))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.fullName", is("Full name must be within the bound of 2 to 50"))).andExpect(jsonPath("$.email", is("Email should be valid"))).andExpect(jsonPath("$.phone", is("Phone pattern not valid")));
    }

    @Test
    public void shouldReturnBadRequestWhenUpdatingInvalidCustomer () throws Exception {
        Customer invalidCustomer = Customer.builder().id(1L).fullName("J").email("invalid-email").phone("123").build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invalidCustomer))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.fullName", is("Full name must be within the bound of 2 to 50"))).andExpect(jsonPath("$.email", is("Email should be valid"))).andExpect(jsonPath("$.phone", is("Phone pattern not valid")));
    }

}