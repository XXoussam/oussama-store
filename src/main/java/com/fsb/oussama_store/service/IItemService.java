package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Item;

import java.util.List;
import java.util.Optional;

public interface IItemService {
    public Item addItem(Item item);

    public Item editItem(Item item);

    public Optional<Item> findItemById(Long itemId);

    public void deleteItem(Long itemId);

    public List<Item> findItemByName(String itemName);

    public boolean checkIfExist(Long id);

    public List<Item> getAllItems();
}
