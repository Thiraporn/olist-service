package com.ana.olist.repository;

import com.ana.olist.entities.FactOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactOrderRepository extends JpaRepository<FactOrder, String> {
}
