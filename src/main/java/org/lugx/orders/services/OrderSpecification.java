package org.lugx.orders.services;

import jakarta.persistence.criteria.Predicate;
import org.lugx.orders.entities.OrderEB;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {
    private OrderSpecification() {
        // Stop instantiation
    }

    public static Specification<OrderEB> filterBy(String cartId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (cartId != null) {
                predicates.add(criteriaBuilder.equal(root.get("cartId"), cartId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
