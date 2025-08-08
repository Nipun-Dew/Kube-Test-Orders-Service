package org.lugx.orders.repos;

import org.lugx.orders.entities.OrderEB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEB, Long>, JpaSpecificationExecutor<OrderEB> {
}