package org.byovsiannikov.tasktheraven.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Long id;

    @NotEmpty(message = "Value should not be empty")
    @Size(min = 2, max = 50, message = "Full name must be within the bound of 2 to 50")
    private String fullName;

    @Size(min = 2, max = 100, message = "Email must be within the bound of 2 to 100")
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "(^\\+\\d{6,14})?", message = "Phone pattern not valid")
    private String phone;
}
