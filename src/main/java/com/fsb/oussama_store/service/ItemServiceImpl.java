package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Item;
import com.fsb.oussama_store.rep.ItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements IItemService {
    @Autowired
    private ItemRepo itemRepo;

    @Override
    public Item addItem(Item item) {
        return itemRepo.save(item);
    }

    @Override
    public Item editItem(Item item) {
        return itemRepo.save(item);
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return itemRepo.findById(itemId);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepo.deleteById(itemId);
    }

    @Override
    public List<Item> findItemByName(String itemName) {
        return itemRepo.findByNameLikeIgnoreCase((new StringBuilder())
                .append("%").append(itemName).append("%").toString());
    }

    @Override
    public boolean checkIfExist(Long id) {
        return itemRepo.existsById(id);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
}
