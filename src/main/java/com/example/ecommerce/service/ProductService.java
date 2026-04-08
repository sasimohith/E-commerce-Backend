package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<ProductDTO> getAllProducts() {
        try {
            List<Product> products = productRepository.findByIsActiveTrue();
            return products.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Return empty list if error occurs
        }
    }

    public ProductDTO getProductById(Long id) {
        try {
            System.out.println("=== DEBUG: Searching for product with ID: " + id + " ===");
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                Product p = product.get();
                System.out.println("Found product - ID: " + p.getId() + ", Name: " + p.getName() + ", isActive: " + p.getIsActive());
                return convertToDTO(p);
            } else {
                System.out.println("No product found with ID: " + id);
                return null;
            }
        } catch (Exception e) {
            System.out.println("ERROR in getProductById: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            updateProductFromDTO(product, productDTO);
            Product savedProduct = productRepository.save(product);
            return convertToDTO(savedProduct);
        }
        return null;
    }

    public boolean deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product p = product.get();
            p.setIsActive(false); // Soft delete
            productRepository.save(p);
            return true;
        }
        return false;
    }

    public boolean updateStock(Long productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product p = product.get();
            if (p.getStockQuantity() >= quantity) {
                p.setStockQuantity(p.getStockQuantity() - quantity);
                productRepository.save(p);
                return true;
            }
        }
        return false;
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setSku(product.getSku());
        dto.setImageUrl(product.getImageUrl());
        dto.setIsActive(product.getIsActive());
        
        try {
            if (product.getCategory() != null) {
                dto.setCategoryId(product.getCategory().getId());
                dto.setCategoryName(product.getCategory().getName());
            }
        } catch (Exception e) {
            // Handle lazy loading exception gracefully
            dto.setCategoryId(null);
            dto.setCategoryName("Category Not Available");
        }
        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        updateProductFromDTO(product, dto);
        return product;
    }

    private void updateProductFromDTO(Product product, ProductDTO dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setSku(dto.getSku());
        product.setImageUrl(dto.getImageUrl());
        product.setIsActive(dto.getIsActive());
        
        if (dto.getCategoryId() != null) {
            Optional<Category> category = categoryRepository.findById(dto.getCategoryId());
            category.ifPresent(product::setCategory);
        }
    }
}
