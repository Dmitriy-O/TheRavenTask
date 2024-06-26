package org.byovsiannikov.tasktheraven.repository;

import org.byovsiannikov.tasktheraven.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    List<CustomerEntity> findAllByIsActiveTrue ();

    CustomerEntity findByIdAndIsActiveTrue (Long id);

    boolean existsByEmail (String email);


}
