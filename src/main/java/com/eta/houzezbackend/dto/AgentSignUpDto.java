package com.eta.houzezbackend.dto;

import lombok.*;

import javax.validation.constraints.*;



@Getter
@Setter
@AllArgsConstructor
@Builder
public class AgentSignUpDto {

    @NotBlank(message = "Email must not be blank.")
    @Email(message = "Email should be a valid email.")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Pattern(regexp = "^(?=\\S*[a-zA-Z])(?=\\S*[0-9#!\"$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]).{8,}$",
            message = "Your password must be at least 8 character long and contains at least one non-letter character.")

    private String password;
    private String photo;
    private String company;
    private String companyLogo;
    private String phone;
    private String activeLink;

}
