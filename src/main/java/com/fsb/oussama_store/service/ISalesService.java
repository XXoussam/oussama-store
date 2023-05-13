package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Sales;

import java.util.List;

public interface ISalesService {
    Sales addSales(Sales sales);

    List<Sales> searchByName(String nameSales);

    void deleteSales(Long salesId);

    List<Sales> allSales();
}
