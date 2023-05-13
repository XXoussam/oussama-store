package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.PurchaseProcess;

import java.util.List;

public interface IPurchaseProcessService {
    PurchaseProcess addPurchase(PurchaseProcess purchase);

    PurchaseProcess editPurchase(Long purchaseId, Integer newQuantity);

    PurchaseProcess findPurchase(Long purchaseId);

    void deletePurchase(Long purchaseId);

    List<PurchaseProcess> getAllPurchase();

    List<PurchaseProcess> getPurchaseByClient(Long idClient) throws Exception;

    boolean checkIfExist(Long purchaseId);
}
