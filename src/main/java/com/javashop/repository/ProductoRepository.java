package com.javashop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javashop.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    

}
