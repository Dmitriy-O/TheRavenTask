package org.byovsiannikov.tasktheraven.converter;

import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.entity.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerConverterTest {

    private final CustomerConverter converter = Mappers.getMapper(CustomerConverter.class);

    @Test
    void shouldConvertFromDto () {
        Customer source = new Customer();
        source.setId(1L);
        source.setFullName("John Doe");
        source.setEmail("john.doe@example.com");
        source.setPhone("+123456789");

        CustomerEntity target = converter.fromDto(source);

        assertEquals(1L, target.getId());
        assertEquals("John Doe", target.getFullName());
        assertEquals("john.doe@example.com", target.getEmail());
        assertEquals("+123456789", target.getPhone());
        assertFalse(target.isActive());
        assertNull(target.getCreated());
        assertNull(target.getUpdated());
    }

    @Test
    void shouldConvertToDto () {
        CustomerEntity source = new CustomerEntity();
        source.setId(1L);
        source.setCreated(1623945600L);
        source.setUpdated(1623945600L);
        source.setFullName("John Doe");
        source.setEmail("john.doe@example.com");
        source.setPhone("+123456789");
        source.setActive(true);

        Customer target = converter.toDto(source);

        assertEquals(1L, target.getId());
        assertEquals("John Doe", target.getFullName());
        assertEquals("john.doe@example.com", target.getEmail());
        assertEquals("+123456789", target.getPhone());
    }

    @Test
    void shouldConvertFromDtoList () {
        Customer source1 = new Customer();
        source1.setId(1L);
        source1.setFullName("John Doe");
        source1.setEmail("john.doe@example.com");
        source1.setPhone("+123456789");

        Customer source2 = new Customer();
        source2.setId(2L);
        source2.setFullName("Jane Doe");
        source2.setEmail("jane.doe@example.com");
        source2.setPhone("+987654321");

        List<Customer> sourceList = Arrays.asList(source1, source2);
        List<CustomerEntity> targetList = converter.fromDto(sourceList);

        assertEquals(2, targetList.size());
        assertEquals(1L, targetList.get(0).getId());
        assertEquals("John Doe", targetList.get(0).getFullName());
        assertEquals(2L, targetList.get(1).getId());
        assertEquals("Jane Doe", targetList.get(1).getFullName());
    }

    @Test
    void shouldConvertToDtoList () {
        CustomerEntity source1 = new CustomerEntity();
        source1.setId(1L);
        source1.setCreated(1623945600L);
        source1.setUpdated(1623945600L);
        source1.setFullName("John Doe");
        source1.setEmail("john.doe@example.com");
        source1.setPhone("+123456789");
        source1.setActive(true);

        CustomerEntity source2 = new CustomerEntity();
        source2.setId(2L);
        source2.setCreated(1623945700L);
        source2.setUpdated(1623945700L);
        source2.setFullName("Jane Doe");
        source2.setEmail("jane.doe@example.com");
        source2.setPhone("+987654321");
        source2.setActive(false);

        List<CustomerEntity> sourceList = Arrays.asList(source1, source2);
        List<Customer> targetList = converter.toDto(sourceList);

        assertEquals(2, targetList.size());
        assertEquals(1L, targetList.get(0).getId());
        assertEquals("John Doe", targetList.get(0).getFullName());
        assertEquals(2L, targetList.get(1).getId());
        assertEquals("Jane Doe", targetList.get(1).getFullName());
    }
}