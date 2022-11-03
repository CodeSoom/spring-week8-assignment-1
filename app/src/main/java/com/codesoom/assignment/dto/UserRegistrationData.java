package com.codesoom.assignment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
public class UserRegistrationData {
    @NotBlank
    @Size(min = 3)
    @Mapping("email")
    private String email;

    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Size(min = 4, max = 1024)
    @Mapping("password")
    private String password;


    public UserRegistrationData(@JsonProperty("email") String email, @JsonProperty("name") String name, @JsonProperty("password") String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
