package com.javashop.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javashop.dto.ProductoRequest;
import com.javashop.entity.Producto;
import com.javashop.exception.ProductoNotFoundException;
import com.javashop.repository.ProductoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    ProductoService productoService;

    @Mock
    ProductoRepository productoRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        productoService = new ProductoService(productoRepository);
    }

    @DisplayName("findById() cuando el producto existe.")
    @Test
    void findById_deberiaRetornarProductoCuandoExiste() {

        // Arrange
        Long id = 1L;

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Laptop Lenovo");
        producto.setDescripcion("Laptop para desarrollo");
        producto.setPrecio(new BigDecimal("15000.00"));
        producto.setStock(10);

        when(productoRepository.findById(id))
                .thenReturn(Optional.of(producto));

        // Act
        Producto resultado = productoService.findById(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Laptop Lenovo", resultado.getNombre());
        assertEquals("Laptop para desarrollo", resultado.getDescripcion());
        assertEquals(new BigDecimal("15000.00"), resultado.getPrecio());
        assertEquals(10, resultado.getStock());

        // Verify
        verify(productoRepository, times(1)).findById(id);
    }

    @DisplayName("findById() cuando no existe → ProductoNotFoundException.")
    @Test
    void testFindById_ProductoNoExiste() {
        // Arrange
        Long id = 999L;

        when(productoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class , () -> productoService.findById(id));
        
        // Assert        
        String mensajeEsperado = "Producto con id 999 no encontrado";
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verify
        verify(productoRepository, times(1)).findById(id);
    }

    @DisplayName("save() guarda correctamente.")
    @Test
    void testSave_GuardaCorrectamente() {
        // Arrange
        Producto producto = new Producto();
        producto.setNombre("nombre test");
        producto.setDescripcion("descipción test");
        producto.setPrecio(new BigDecimal("100000.00"));
        producto.setStock(100);

        when(productoRepository.save(producto)).thenReturn(producto);

        // Act
        Producto productoGuardado = productoService.save(producto);

        // Assert
        assertNotNull(productoGuardado);
        assertEquals(producto, productoGuardado);

        // Verify
        verify(productoRepository).save(producto);

    }

    @DisplayName("update() actualiza correctamente.")
    @Test
    void testUpdate_ActualizaCorrectamente() {
        // Arrange
        Long id = 1L; 
        ProductoRequest request = new ProductoRequest("nombre actualizado", "description actualizada", new BigDecimal("10.00"), 1);
        
        Producto productoExistente = new Producto();
        productoExistente.setId(id);
        productoExistente.setNombre("nombre anterior");
        productoExistente.setDescripcion("description anterior");
        productoExistente.setPrecio(new BigDecimal("20.00"));
        productoExistente.setStock(10);
    
        when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));

        when(productoRepository.save(productoExistente)).thenReturn(productoExistente);

        // Act
        Producto productoActualizado = productoService.update(id, request);

        // Assert
        assertNotNull(productoActualizado);

        assertEquals(id, productoActualizado.getId());
        assertEquals(request.nombre(), productoActualizado.getNombre());
        assertEquals(request.descripcion(), productoActualizado.getDescripcion());
        assertEquals(request.precio(), productoActualizado.getPrecio());
        assertEquals(request.stock(), productoActualizado.getStock());

        // Verify
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(productoExistente);
    }

    @DisplayName("update() cuando no existe → ProductoNotFoundException.")
    @Test
    void testUpdate_NoExisteProducto() {
        // Arrange
        Long id = 999L; 
        ProductoRequest request = new ProductoRequest("nombre actualizado", "description actualizada", new BigDecimal("10.00"), 1);
    
        when(productoRepository.findById(id)).thenReturn(Optional.empty());


        // Act
        ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class, () -> productoService.update(id, request));

        // Assert
        String mensajeEsperado = "Producto con id 999 no encontrado";
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verify
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).save(any(Producto.class));
    }

    @DisplayName("deleteById() elimina correctamente.")
    @Test
    void testDeleteByIdEliminaCorrectamente() {
        // Arrange
        Long id = 1L;

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Laptop Lenovo");
        producto.setDescripcion("Laptop para desarrollo");
        producto.setPrecio(new BigDecimal("15000.00"));
        producto.setStock(10);

        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        // Act
        productoService.deleteById(id);

        // Assert
        
        // Verify
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).delete(producto);
    }


    @DisplayName("deleteById() cuando no existe → ProductoNotFoundException.")
    @Test
    void testDeleteByIdProductoNoExiste() {
        // Arrange
        Long id = 999L;

        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class, () -> productoService.deleteById(id));

        // Assert
        String mensajeEsperado = "Producto con id 999 no encontrado";
        assertEquals(mensajeEsperado, exception.getMessage());

        // Verify
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, never()).delete(any(Producto.class));

    }
}
