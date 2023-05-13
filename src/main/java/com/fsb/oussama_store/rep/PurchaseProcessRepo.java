package com.fsb.oussama_store.rep;

import com.fsb.oussama_store.models.PurchaseProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProcessRepo extends JpaRepository<PurchaseProcess, Long> {
}
