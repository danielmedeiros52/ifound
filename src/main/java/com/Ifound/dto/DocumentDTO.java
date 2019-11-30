package com.Ifound.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DocumentDTO {
    @JsonProperty("documentNumber")
    private String documentNumber;
    @JsonProperty("userFound")
    private UserDto userFound;
    @JsonProperty("ownerName")
    private String ownerName;

}
