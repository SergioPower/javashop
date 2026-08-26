package com.javashop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.javashop.dto.ProductoRequest;
import com.javashop.dto.ProductoResponse;
import com.javashop.entity.Producto;
import com.javashop.exception.ProductoNotFoundException;
import com.javashop.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository repository) {
        this.productoRepository = repository;
    }

    public List<ProductoResponse> findAll() {
        return productoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ProductoNotFoundException(id));
    }

    public Producto save(ProductoRequest request) {
        Producto newProducto = toEntity(request);
        return productoRepository.save(newProducto);
    }

    public void deleteById(Long id) {
        Producto producto = findById(id);
        productoRepository.delete(producto);
    }

    public Producto update(Long id, ProductoRequest request) {
        Producto productoToUpdate = findById(id);
        productoToUpdate.setNombre(request.nombre());
        productoToUpdate.setDescripcion(request.descripcion());
        productoToUpdate.setPrecio(request.precio());
        productoToUpdate.setStock(request.stock());
        return productoRepository.save(productoToUpdate);
    }

    public Producto toEntity(ProductoRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        return producto;
    }

    public ProductoResponse toResponse(Producto producto){
        ProductoResponse response = new ProductoResponse(
            producto.getId(), 
            producto.getNombre(), 
            producto.getDescripcion(), 
            producto.getPrecio(), 
            producto.getStock());

        return response;
    }

}
