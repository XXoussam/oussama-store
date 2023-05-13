package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Owner;

import java.util.List;

public interface IOwnerService {
    public Owner addOwner(Owner owner);

    public Owner editOwner(Owner owner);

    public Owner findOwnerById(Long ownerId);

    public void deleteOwner(Long ownerId);

    public boolean checkIfExist(Long id);

    public List<Owner> getAllOwners();
}
