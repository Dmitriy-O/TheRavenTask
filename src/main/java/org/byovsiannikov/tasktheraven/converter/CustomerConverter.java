package org.byovsiannikov.tasktheraven.converter;

import org.byovsiannikov.tasktheraven.dto.Customer;
import org.byovsiannikov.tasktheraven.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerConverter {

    Customer toDto (CustomerEntity customerEntity);

    List<Customer> toDto (List<CustomerEntity> customerEntity);

    CustomerEntity fromDto (Customer customer);

    List<CustomerEntity> fromDto (List<Customer> customer);
}
