package com.chokhaniyash.streambridge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "User id is required")
    private String id;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email format")
    private String email;

    private String firstName;
    private String lastName;
}
