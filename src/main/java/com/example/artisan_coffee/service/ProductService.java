package com.example.artisan_coffee.service;

import com.example.artisan_coffee.dto.ProductDTO;
import com.example.artisan_coffee.entity.Product;
import com.example.artisan_coffee.product.ProductSpecification;
import com.example.artisan_coffee.repository.ProductRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public final ImageService imageService;

    public ProductService(ProductRepository productRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    public List<ProductDTO> getFilteredProducts(String name, String category, Double minPrice, Double maxPrice, Boolean inStockOnly) {
        Specification<Product> spec = ProductSpecification.withFilters(name, category, minPrice, maxPrice, inStockOnly);
        List<Product> products = productRepository.findAll(spec);
        return products.stream().map(ProductDTO::fromEntity).toList();
    }

    public List<String> getAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductDTO::fromEntity)
                .toList();
    }

    public ProductDTO addProduct(ProductDTO dto, MultipartFile imageFile) throws IOException {
        Product product = dto.toEntity();

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            product.setImageUrl(fileName);
        }

        Product saved = productRepository.save(product);
        return ProductDTO.fromEntity(saved);
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto, MultipartFile imageFile) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (product.getImageUrl() != null) {
                imageService.renameImageToDeleted(product.getImageUrl());
            }
            String fileName = saveImage(imageFile);
            product.setImageUrl(fileName);
        }

        Product updated = productRepository.save(product);
        return ProductDTO.fromEntity(updated);
    }


    public void deleteProduct(Long id) throws IOException {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        imageService.renameImageToDeleted(product.getImageUrl());
        productRepository.deleteById(id);
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "C:/Users/EBURNED41/OneDrive - NTT DATA EMEAL/Escritorio/coffee/uploads/";
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        File uploadPath = new File(uploadDir);

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::fromEntity)
                .orElse(null);
    }

}

