package com.javashop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.javashop.dto.ProductoRequest;
import com.javashop.entity.Producto;
import com.javashop.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository repository) {
        this.productoRepository = repository;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Producto save(ProductoRequest request) {
        Producto newProducto = toEntity(request);
        return productoRepository.save(newProducto);
    }

    public Producto toEntity(ProductoRequest request){
        Producto producto = new Producto();
        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        return producto;
    }

}
