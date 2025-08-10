package com.example.artisan_coffee.product;

import com.example.artisan_coffee.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withFilters(String name, String category, Double minPrice, Double maxPrice, Boolean inStockOnly) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (category != null && !category.isEmpty()) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (Boolean.TRUE.equals(inStockOnly)) {
                predicates.add(cb.greaterThan(root.get("quantity"), 0));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

