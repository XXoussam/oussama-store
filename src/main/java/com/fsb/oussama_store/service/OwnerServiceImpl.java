package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Owner;
import com.fsb.oussama_store.rep.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements IOwnerService {
    @Autowired
    private OwnerRepo ownerRepo;

    @Override
    public Owner addOwner(Owner owner) {
        return ownerRepo.save(owner);
    }

    @Override
    public Owner editOwner(Owner owner) {
        return ownerRepo.save(owner);
    }

    @Override
    public Owner findOwnerById(Long ownerId) {
        return ownerRepo.findById(ownerId).get();
    }

    @Override
    public void deleteOwner(Long ownerId) {
        ownerRepo.deleteById(ownerId);
    }

    @Override
    public boolean checkIfExist(Long id) {
        return ownerRepo.existsById(id);
    }

    @Override
    public List<Owner> getAllOwners() {
        return ownerRepo.findAll();
    }
}
