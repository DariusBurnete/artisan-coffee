package com.example.artisan_coffee.controller;

import com.example.artisan_coffee.dto.ProductDTO;
import com.example.artisan_coffee.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStockOnly,
            Model model
    ) {
        List<ProductDTO> products = productService.getFilteredProducts(name, category, minPrice, maxPrice, inStockOnly);
        List<String> categories = productService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("inStockOnly", inStockOnly);

        return "home";
    }

    @GetMapping("/navbar")
    public String showNavbar() {
        return "navbar";
    }

    @GetMapping("/api/products/{id}")
    @ResponseBody
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }
}

