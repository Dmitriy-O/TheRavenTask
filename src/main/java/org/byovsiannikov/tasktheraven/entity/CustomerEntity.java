package org.byovsiannikov.tasktheraven.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMER_SEQ")
    @SequenceGenerator(name = "CUSTOMER_SEQ", sequenceName = "CUSTOMER_SEQ_ID", allocationSize = 1)
    private Long id;

    private Long created;

    private Long updated;

    @NotEmpty(message = "Value should not be empty")
    @Size(min = 2, max = 50, message = "Full name must be within the bound of 2 to 50")
    private String fullName;

    @Size(min = 2, max = 100, message = "Email must be within the bound of 2 to 100")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "(^\\+\\d{6,14})?", message = "Phone pattern not valid")
    private String phone;

    private boolean isActive;


}
