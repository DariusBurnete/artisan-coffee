package com.example.artisan_coffee.controller;

import com.example.artisan_coffee.dto.ProductDTO;
import com.example.artisan_coffee.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String adminPanel(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin-panel";
    }

    @GetMapping("/products/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "product-form";
    }

    @PostMapping("/products/add")
    public String addProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {
        productService.addProduct(productDTO, imageFile);
        return "redirect:/admin";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        ProductDTO productDTO = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found"));

        model.addAttribute("product", productDTO);
        return "product-form";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {
        productService.updateProduct(id, productDTO, imageFile);
        return "redirect:/admin";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) throws IOException {
        productService.deleteProduct(id);
        return "redirect:/admin";
    }
}

