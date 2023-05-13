package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Sales;
import com.fsb.oussama_store.rep.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements ISalesService {
    @Autowired
    private SalesRepo salesRepo;

    @Override
    public Sales addSales(Sales sales) {
        return salesRepo.save(sales);
    }

    @Override
    public List<Sales> searchByName(String nameSales) {
        return salesRepo.findByNameProductLikeIgnoreCase((new StringBuilder())
                .append("%").append(nameSales).append("%").toString());
    }

    @Override
    public void deleteSales(Long salesId) {
        salesRepo.deleteById(salesId);
    }

    @Override
    public List<Sales> allSales() {
        return salesRepo.findAll();
    }
}
