package com.crking7.datn.repositories;

import com.crking7.datn.models.Color;
import com.crking7.datn.models.Sale;
import com.crking7.datn.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    Sale findByName(String name);

    List<Sale> findByIsActive(Integer isActive);
}
