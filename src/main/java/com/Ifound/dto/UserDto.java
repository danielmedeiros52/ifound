package com.Ifound.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private long id;
    @NotNull
    private String email;
    @NotNull
    private String numberOfPrincipalDocument;
    @NotNull
    private String cellphone;
    private String city;
    private String homeNumber;
    private String name;
    private String street;
    private String type;
    @JsonProperty("zipCode")
    private String zipCode;
    private Boolean isActive;


}
