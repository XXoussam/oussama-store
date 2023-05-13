package com.fsb.oussama_store.rep;


import com.fsb.oussama_store.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepo extends JpaRepository<Sales, Long> {
    public List<Sales> findByNameProductLikeIgnoreCase(String name);
}
