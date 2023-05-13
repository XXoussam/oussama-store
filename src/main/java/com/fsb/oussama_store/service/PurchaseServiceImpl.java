package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.PurchaseProcess;
import com.fsb.oussama_store.rep.PurchaseProcessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseServiceImpl implements IPurchaseProcessService {
    @Autowired
    private PurchaseProcessRepo purchaseProcessRepo;
    @Autowired
    private IClientService clientService;


    @Override
    public PurchaseProcess addPurchase(PurchaseProcess purchase) {
        return purchaseProcessRepo.save(purchase);
    }

    @Override
    public PurchaseProcess editPurchase(Long purchaseId, Integer newQuantity) {
        PurchaseProcess p = findPurchase(purchaseId);
        p.setQuantity(newQuantity);
        return purchaseProcessRepo.save(p);
    }

    @Override
    public PurchaseProcess findPurchase(Long purchaseId) {
        return purchaseProcessRepo.findById(purchaseId).get();
    }

    @Override
    public void deletePurchase(Long purchaseId) {
        purchaseProcessRepo.deleteById(purchaseId);
    }

    @Override
    public List<PurchaseProcess> getAllPurchase() {
        return purchaseProcessRepo.findAll();
    }

    @Override
    public List<PurchaseProcess> getPurchaseByClient(Long idClient) throws Exception {
        List<PurchaseProcess> purchaseProcesses = new ArrayList<>();
        if (clientService.checkIfExist(idClient)) {
            for (PurchaseProcess process : getAllPurchase()) {
                if (process.getClientId().equals(idClient)) {
                    purchaseProcesses.add(process);
                }
            }
        } else throw new Exception("the client with id : " + idClient + " does not exist");
        return purchaseProcesses;
    }

    @Override
    public boolean checkIfExist(Long purchaseId) {
        if (purchaseProcessRepo.existsById(purchaseId)) {
            return true;
        }
        return false;
    }
}
