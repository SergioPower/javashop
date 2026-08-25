package com.javashop.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.javashop.entity.Producto;
import com.javashop.exception.ProductoNotFoundException;
import com.javashop.service.ProductoService;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @DisplayName("GET /api/productos/{id} cuando existe → 200 OK")
    @Test
    void testFindByIdProductoExiste() throws Exception {

        // Arrange
        Long id = 1L;

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Laptop Lenovo");
        producto.setDescripcion("Laptop para desarrollo");
        producto.setPrecio(new BigDecimal("15000.00"));
        producto.setStock(10);

        when(productoService.findById(id))
                .thenReturn(producto);

        // Act
        mockMvc.perform(get("/api/productos/{id}", id))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Lenovo"))
                .andExpect(jsonPath("$.descripcion").value("Laptop para desarrollo"))
                .andExpect(jsonPath("$.precio").value(15000.00))
                .andExpect(jsonPath("$.stock").value(10));

        // Verify
        verify(productoService, times(1)).findById(id);
    }

    @Test
    void testFindByIdProductoNoExiste() throws Exception {
        // Arrange
        Long id = 999L;

        when(productoService.findById(id)).thenThrow(new ProductoNotFoundException(id));

        //Act
        mockMvc.perform(get("/api/productos/{id}", id))

                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 999 no encontrado"));

        // Verify
        verify(productoService, times(1)).findById(id);
    }

    
}
