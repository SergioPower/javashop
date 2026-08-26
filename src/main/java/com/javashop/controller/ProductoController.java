package com.javashop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javashop.dto.ProductoRequest;
import com.javashop.dto.ProductoResponse;
import com.javashop.service.ProductoService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> create(@RequestBody @Valid ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.save(request));
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> list() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findResponseById(id));

        /*
         * Optional<Producto> optionalProducto = productoService.findById(id);
         * if (optionalProducto.isPresent()) {
         * return ResponseEntity.ok(optionalProducto.get());
         * } else {
         * return ResponseEntity.notFound().build();
         * }
         */

        /*
         * return productoService.findById(id)
         * .map(ResponseEntity::ok)
         * .orElseGet(() -> ResponseEntity.notFound().build());
         */

        
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> update(@PathVariable Long id, @RequestBody @Valid ProductoRequest request) {
        return ResponseEntity.ok(productoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
