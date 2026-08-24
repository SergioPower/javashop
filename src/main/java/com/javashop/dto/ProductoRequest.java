package com.javashop.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(

        @JsonProperty("nombre")
        @NotBlank String nombre,

        @JsonProperty("descripcion")
        String descripcion,

        @JsonProperty("precio")
        @NotNull
        @Positive
        BigDecimal precio,

        @JsonProperty("stock")
        @NotNull
        @PositiveOrZero
        Integer stock

) {
    @JsonCreator
    public ProductoRequest {
    }
}