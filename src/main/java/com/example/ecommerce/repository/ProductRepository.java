package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.isActive = true")
    List<Product> findByIsActiveTrue();
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id AND p.isActive = true")
    Optional<Product> findByIdAndIsActiveTrue(@Param("id") Long id);
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.id = :categoryId AND p.isActive = true")
    List<Product> findByCategoryIdAndIsActiveTrue(@Param("categoryId") Long categoryId);
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);
    
    Product findBySku(String sku);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
