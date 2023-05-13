package com.fsb.oussama_store.rep;

import com.fsb.oussama_store.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {
    public List<Item> findByNameLikeIgnoreCase(String name);
}
