package com.fsb.oussama_store.controller;

import com.fsb.oussama_store.models.Item;
import com.fsb.oussama_store.models.Sales;
import com.fsb.oussama_store.service.ISalesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('Owner')")
@RequestMapping("/rest/sales/api")
public class SalesController {
    @Autowired
    private ISalesService salesService;
/*
    addSales is in buy method
*/

    @GetMapping("/findsalesByItemName")
    @ApiOperation(value = "find sales by nameItem", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : sales not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Sales>> findSalesByItemName(@RequestParam String name) {
        List<Sales> sales = salesService.searchByName(name);
        if (!sales.isEmpty()) {
            return new ResponseEntity<List<Sales>>(sales, HttpStatus.OK);
        }
        return new ResponseEntity<List<Sales>>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletesales/{salesId}")
    @ApiOperation(value = "Delete a sales, if the item does not exist, nothing is done", response = String.class)
    @ApiResponse(code = 204, message = "No Content: sales sucessfully deleted")
    public ResponseEntity<String> deleteSales(@PathVariable Long salesId) {
        salesService.deleteSales(salesId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allsales")
    @ApiOperation(value = "find all sales", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no sales found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Sales>> getAllSales() {
        List<Sales> allSales = salesService.allSales();
        if (!allSales.isEmpty()) {
            return new ResponseEntity<List<Sales>>(allSales, HttpStatus.OK);
        }
        return new ResponseEntity<List<Sales>>(HttpStatus.NO_CONTENT);
    }
}
