package com.javashop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.javashop.dto.ProductoRequest;
import com.javashop.entity.Producto;
import com.javashop.exception.ProductoNotFoundException;
import com.javashop.service.ProductoService;

@WebMvcTest(ProductoController.class)
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

<<<<<<< HEAD

=======
>>>>>>> 6e7f0296efe0f396342fb52d6e71ba54a5dc71e9
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

        // Act
        mockMvc.perform(get("/api/productos/{id}", id))

                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 999 no encontrado"));

        // Verify
        verify(productoService, times(1)).findById(id);
    }

    @Test
    void testListObtieneListaProductos() throws Exception {
        // Arrange
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop Lenovo Legion 5");
        producto.setDescripcion("Laptop para desarrollo y gaming");
        producto.setPrecio(new BigDecimal("18500"));
        producto.setStock(7);

        List<Producto> lista = Arrays.asList(producto);

        when(productoService.findAll()).thenReturn(lista);

        // Act
        mockMvc.perform(get("/api/productos"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Laptop Lenovo Legion 5"))
                .andExpect(jsonPath("$[0].descripcion").value("Laptop para desarrollo y gaming"))
                .andExpect(jsonPath("$[0].precio").value(18500))
                .andExpect(jsonPath("$[0].stock").value(7));

        // Veriry
        verify(productoService, times(1)).findAll();

    }

    @Test
    void testCreateAgregarProducto() throws Exception {

        // Arrange
        ProductoRequest request = new ProductoRequest(
                "Laptop Lenovo Legion 5",
                "Laptop para desarrollo y gaming",
                new BigDecimal("18500"),
                7);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());

        when(productoService.save(request)).thenReturn(producto);

        // Act
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Laptop Lenovo Legion 5",
                            "descripcion": "Laptop para desarrollo y gaming",
                            "precio": 18500,
                            "stock": 7
                        }
                        """))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Lenovo Legion 5"))
                .andExpect(jsonPath("$.descripcion").value("Laptop para desarrollo y gaming"))
                .andExpect(jsonPath("$.precio").value(18500))
                .andExpect(jsonPath("$.stock").value(7));

        // Veriry
        verify(productoService, times(1)).save(request);
    }

    @Test
    void testCreateProductoInvalido()  throws Exception{
        // Act
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nombre": "",
                        "descripcion": "inválido",
                        "precio": null,
                        "stock": null
                        }
                        """))
            // Assert
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").exists());
            
        // Veriry
        verify(productoService, never()).save(any(ProductoRequest.class));
    }

    @Test
    void testUpdateActualizacionCorrecta() throws Exception {
        // Arrange
        Long id = 1L;
        ProductoRequest request = new ProductoRequest(
                "nombre test",
                "descripcion test",
                new BigDecimal("10000"),
                10);

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("nombre test");
        producto.setDescripcion("descripcion test");
        producto.setPrecio(new BigDecimal("10000"));
        producto.setStock(10);

        when(productoService.update(id, request)).thenReturn(producto);

        // Act
        mockMvc.perform(put("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "nombre test",
                            "descripcion": "descripcion test",
                            "precio": 10000,
                            "stock": 10
                        }
                        """))
                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value(request.nombre()))
                .andExpect(jsonPath("$.descripcion").value(request.descripcion()))
                .andExpect(jsonPath("$.precio").value(request.precio()))
                .andExpect(jsonPath("$.stock").value(request.stock()));

        // Verify
        verify(productoService, times(1)).update(id, request);

    }

    @Test
    void testUpdateProductoNoExiste() throws Exception {
        // Arrange
        Long id = 999L;
        ProductoRequest request = new ProductoRequest(
                "nombre test",
                "descripcion test",
                new BigDecimal("10000"),
                10);

        when(productoService.update(id, request)).thenThrow(new ProductoNotFoundException(id));

        // Act
        mockMvc.perform(put("/api/productos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "nombre test",
                            "descripcion": "descripcion test",
                            "precio": 10000,
                            "stock": 10
                        }
                        """))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 999 no encontrado"));

        // Verifi
        verify(productoService, times(1)).update(id, request);

    }

    @Test
    void testDeleteEliminacionCorrecta() throws Exception {
        // Arrange
        Long id = 1L;

        // Act
        mockMvc.perform(delete("/api/productos/{id}", id))
                // Assert
                .andExpect(status().isNoContent());

        // Verify
        verify(productoService, times(1)).deleteById(id);

    }

    @Test
    void testDeleteProductoNoExiste() throws Exception {
        // Arrange
        Long id = 999L;

        doThrow(new ProductoNotFoundException(id)).when(productoService).deleteById(id);

        // Act
        mockMvc.perform(delete("/api/productos/{id}", id))
                // Assert
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto con id 999 no encontrado"));

        // Verify
        verify(productoService, times(1)).deleteById(id);
    }

}
