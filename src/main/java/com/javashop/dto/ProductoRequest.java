package com.javashop.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequest(
    @NotBlank String nombre, 
    String descripcion, 
    @Positive BigDecimal precio, 
    @PositiveOrZero Integer stock) {

}
