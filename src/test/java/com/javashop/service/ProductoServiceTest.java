package com.javashop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.javashop.dto.ProductoRequest;
import com.javashop.dto.ProductoResponse;
import com.javashop.entity.Producto;
import com.javashop.exception.ProductoNotFoundException;
import com.javashop.repository.ProductoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

        ProductoService productoService;

        @Mock
        ProductoRepository productoRepository;

        @BeforeEach
        void setUp() {
                productoService = new ProductoService(productoRepository);
        }

        @DisplayName("findAll() devuelve la lista de productos como ProductoResponse.")
        @Test
        void testFindAll() {

                // Arrange
                Producto producto1 = new Producto();
                producto1.setId(1L);
                producto1.setNombre("Laptop Lenovo");
                producto1.setDescripcion("Laptop para desarrollo");
                producto1.setPrecio(new BigDecimal("15000.00"));
                producto1.setStock(10);

                Producto producto2 = new Producto();
                producto2.setId(2L);
                producto2.setNombre("Mouse Logitech");
                producto2.setDescripcion("Mouse inalámbrico");
                producto2.setPrecio(new BigDecimal("800.00"));
                producto2.setStock(20);

                when(productoRepository.findAll())
                                .thenReturn(List.of(producto1, producto2));

                // Act
                List<ProductoResponse> resultado = productoService.findAll();

                // Assert
                assertNotNull(resultado);
                assertEquals(2, resultado.size());

                assertEquals(1L, resultado.get(0).id());
                assertEquals("Laptop Lenovo", resultado.get(0).nombre());

                assertEquals(2L, resultado.get(1).id());
                assertEquals("Mouse Logitech", resultado.get(1).nombre());

                // Verify
                verify(productoRepository, times(1)).findAll();
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
                ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class,
                                () -> productoService.findById(id));

                // Assert
                String mensajeEsperado = "Producto con id 999 no encontrado";
                assertEquals(mensajeEsperado, exception.getMessage());

                // Verify
                verify(productoRepository, times(1)).findById(anyLong());
        }

        @Test
        void testFindResponseById() {
                // Arrange
                Long id = 1L;
                Producto producto = new Producto();
                producto.setId(id);
                producto.setNombre("nombre test");
                producto.setDescripcion("descripcion test");
                producto.setPrecio(new BigDecimal("100000.00"));
                producto.setStock(100);

                ProductoResponse response = new ProductoResponse(
                                1L,
                                "nombre test",
                                "descripcion test",
                                new BigDecimal("100000.00"),
                                100);

                when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

                // Act
                ProductoResponse resultado = productoService.findResponseById(id);

                // Assert
                assertNotNull(resultado);
                assertEquals(response, resultado);

                // Verify
                verify(productoRepository, times(1)).findById(id);
        }

        @DisplayName("save() guarda correctamente.")
        @Test
        void testSaveGuardaCorrectamente() {

                // Arrange
                ProductoRequest request = new ProductoRequest(
                                "nombre test",
                                "description test",
                                new BigDecimal("100000.00"),
                                100);

                Producto productoGuardado = new Producto();
                productoGuardado.setId(1L);
                productoGuardado.setNombre(request.nombre());
                productoGuardado.setDescripcion(request.descripcion());
                productoGuardado.setPrecio(request.precio());
                productoGuardado.setStock(request.stock());

                when(productoRepository.save(any(Producto.class)))
                                .thenReturn(productoGuardado);

                // Act
                ProductoResponse resultado = productoService.save(request);

                // Assert
                assertNotNull(resultado);
                assertEquals(1L, resultado.id());
                assertEquals(request.nombre(), resultado.nombre());
                assertEquals(request.descripcion(), resultado.descripcion());
                assertEquals(request.precio(), resultado.precio());
                assertEquals(request.stock(), resultado.stock());

                // Verify
                verify(productoRepository, times(1))
                                .save(any(Producto.class));
        }

        @DisplayName("update() actualiza correctamente.")
        @Test
        void testUpdateActualizaCorrectamente() {
                // Arrange
                Long id = 1L;
                ProductoRequest request = new ProductoRequest("nombre actualizado", "description actualizada",
                                new BigDecimal("10.00"), 1);

                Producto productoExistente = new Producto();
                productoExistente.setId(id);
                productoExistente.setNombre("nombre anterior");
                productoExistente.setDescripcion("description anterior");
                productoExistente.setPrecio(new BigDecimal("20.00"));
                productoExistente.setStock(10);

                when(productoRepository.findById(id)).thenReturn(Optional.of(productoExistente));

                when(productoRepository.save(productoExistente)).thenReturn(productoExistente);

                // Act
                ProductoResponse productoActualizado = productoService.update(id, request);

                // Assert
                assertNotNull(productoActualizado);

                assertEquals(id, productoActualizado.id());
                assertEquals(request.nombre(), productoActualizado.nombre());
                assertEquals(request.descripcion(), productoActualizado.descripcion());
                assertEquals(request.precio(), productoActualizado.precio());
                assertEquals(request.stock(), productoActualizado.stock());

                // Verify
                verify(productoRepository, times(1)).findById(id);
                verify(productoRepository, times(1)).save(productoExistente);
        }

        @DisplayName("update() cuando no existe → ProductoNotFoundException.")
        @Test
        void testUpdateNoExisteProducto() {
                // Arrange
                Long id = 999L;
                ProductoRequest request = new ProductoRequest("nombre actualizado", "description actualizada",
                                new BigDecimal("10.00"), 1);

                when(productoRepository.findById(id)).thenReturn(Optional.empty());

                // Act
                ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class,
                                () -> productoService.update(id, request));

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
                ProductoNotFoundException exception = assertThrows(ProductoNotFoundException.class,
                                () -> productoService.deleteById(id));

                // Assert
                String mensajeEsperado = "Producto con id 999 no encontrado";
                assertEquals(mensajeEsperado, exception.getMessage());

                // Verify
                verify(productoRepository, times(1)).findById(id);
                verify(productoRepository, never()).delete(any(Producto.class));

        }
}
