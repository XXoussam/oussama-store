package com.fsb.oussama_store.controller;

import com.fsb.oussama_store.authentication.Service.UserService;
import com.fsb.oussama_store.models.*;
import com.fsb.oussama_store.rep.PurchaseProcessRepo;
import com.fsb.oussama_store.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/purchase/api/")
public class PurchaseProcessController {
    @Autowired
    private IPurchaseProcessService purchaseProcessService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IItemService itemService;
    @Autowired
    private PurchaseProcessRepo processRepo;
    @Autowired
    private IOwnerService ownerService;
    @Autowired
    private ISalesService salesService;
    @Autowired
    private UserService userService;


    @PostMapping("/addpurchase")
    @PreAuthorize("hasRole('Client')")
    @ApiOperation(value = "Add a new purchase", response = PurchaseProcess.class)
    @ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict: the purchase already exist"),
            @ApiResponse(code = 201, message = "Created: the purchase is successfully inserted"),
            @ApiResponse(code = 304, message = "Not Modified: the purchase is unsuccessfully inserted")})
    public ResponseEntity<PurchaseProcess> createNewPurchase(@RequestBody PurchaseProcess purchaseProcess) {
        purchaseProcess.setClientId(
                clientService.findClientByEmail(userService.getCurrentUsername()).getId()
        );

        if (purchaseIsValidate(purchaseProcess)) {
            itemService.findItemById(purchaseProcess.getItemId()).get().setTotalExemplaries(
                    itemService.findItemById(purchaseProcess.getItemId()).get().getTotalExemplaries()
                            - purchaseProcess.getQuantity()
            );
            itemService.editItem(itemService.findItemById(purchaseProcess.getItemId()).get());

            if (checkPurchaseIfExist(purchaseProcess) != null) {
                PurchaseProcess process = purchaseProcessService
                        .findPurchase(checkPurchaseIfExist(purchaseProcess));
                System.out.println("////////////////////////////" + checkPurchaseIfExist(purchaseProcess));
                process.setQuantity(process.getQuantity() + purchaseProcess.getQuantity());
                PurchaseProcess pur = processRepo.save(process);
                return new ResponseEntity<PurchaseProcess>(pur, HttpStatus.CREATED);
            } else {
                PurchaseProcess purchaseProcess1 = purchaseProcessService.addPurchase(purchaseProcess);
                if (purchaseProcess1 != null && purchaseProcess1.getId() != null) {
                    Client c = clientService.findClient(purchaseProcess1.getClientId());
                    c.getPurchaseProcesses().add(purchaseProcess1);
                    clientService.editClient(c);
                    return new ResponseEntity<PurchaseProcess>(purchaseProcess1, HttpStatus.CREATED);
                }
            }
        }
        return new ResponseEntity<PurchaseProcess>(HttpStatus.NOT_MODIFIED);
    }


    boolean purchaseIsValidate(PurchaseProcess purchaseProcess) {
        if ((clientService.checkIfExist(purchaseProcess.getClientId())) &&
                (itemService.checkIfExist(purchaseProcess.getItemId()))) {
            if (purchaseProcess.getQuantity() <=
                    itemService.findItemById(purchaseProcess.getItemId()).get().getTotalExemplaries()) {
                return true;
            }
        }
        return false;
    }


    Long checkPurchaseIfExist(PurchaseProcess purchaseProcess) {
        List<PurchaseProcess> purchaseProcessList = purchaseProcessService.getAllPurchase();
        List<Long> idItems = purchaseProcessList.stream().map(p -> p.getItemId()).collect(Collectors.toList());
        List<Long> idClients = purchaseProcessList.stream().map(p -> p.getClientId()).collect(Collectors.toList());
        List<Long> ids = purchaseProcessList.stream().map(p -> p.getId()).collect(Collectors.toList());
        int i = 0;
        while (i < idClients.size()) {
            if ((purchaseProcess.getClientId().equals(idClients.get(i))) &&
                    (purchaseProcess.getItemId().equals(idItems.get(i)))) {
                return ids.get(i);
            }
            i++;
        }
        return null;
    }


    @GetMapping("/allpurchases")
    @PreAuthorize("hasRole('Owner')")
    @ApiOperation(value = "find all purchase", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no purchase found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<PurchaseProcessDTO>> getAllPurchases() {
        List<PurchaseProcess> allPurchase = purchaseProcessService.getAllPurchase();
        if (!allPurchase.isEmpty()) {
            List<PurchaseProcessDTO> processDTOList = allPurchase.stream()
                    .map(this::mapToPurchaseDTO).collect(Collectors.toList());
            return new ResponseEntity<List<PurchaseProcessDTO>>(processDTOList, HttpStatus.OK);
        }
        return new ResponseEntity<List<PurchaseProcessDTO>>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("/updatepurchase")
    @PreAuthorize("hasRole('Client')")
    @ApiOperation(value = "update a purchase", response = PurchaseProcess.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Not Found : the purchase does not exist"),
            @ApiResponse(code = 200, message = "OK: the purchase successfully updated"),
            @ApiResponse(code = 304, message = "Not Modified : the purchase unsuccessfully updated")})
    public ResponseEntity<PurchaseProcess> updateItem(@RequestBody PurchaseProcess purchase) {
        System.out.println(purchase);
        if (!purchaseProcessService.checkIfExist(purchase.getId())) {
            return new ResponseEntity<PurchaseProcess>(HttpStatus.NOT_FOUND);
        }
        Item item = itemService.findItemById
                (purchaseProcessService.findPurchase(purchase.getId()).getItemId()).get();
        PurchaseProcess pp = purchaseProcessService.findPurchase(purchase.getId());
        if (item.getTotalExemplaries() + pp.getQuantity() >= purchase.getQuantity()) {
            item.setTotalExemplaries(item.getTotalExemplaries() + pp.getQuantity() - purchase.getQuantity());
            itemService.editItem(item);
            PurchaseProcess p = purchaseProcessService.editPurchase(purchase.getId(), purchase.getQuantity());
            if (p != null) {
                System.out.println("modified");
                return new ResponseEntity<PurchaseProcess>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<PurchaseProcess>(HttpStatus.NOT_MODIFIED);
    }


    @GetMapping("/findpurchase")
    @PreAuthorize("hasRole('Owner')")
    @ApiOperation(value = "find purchase by id", response = PurchaseProcess.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : purchase not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<PurchaseProcess> findPurchaseProcessById(@RequestParam("id") Long id) {
        PurchaseProcess p = purchaseProcessService.findPurchase(id);
        if (p != null) {
            return new ResponseEntity<PurchaseProcess>(p, HttpStatus.OK);
        }
        return new ResponseEntity<PurchaseProcess>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/deletepurchase/{purchaseId}")
    @PreAuthorize("hasRole('Client')")
    @ApiOperation(value = "Delete a purchase, if the purchase does not exist, nothing is done", response = String.class)
    @ApiResponse(code = 204, message = "No Content: purchase sucessfully deleted")
    public ResponseEntity<String> deletePurchase(@PathVariable Long purchaseId) {
        System.out.println("///////////////////////////delete" + purchaseId);
        PurchaseProcess p = purchaseProcessService.findPurchase(purchaseId);
        Item item = itemService.findItemById(p.getItemId()).get();
        item.setTotalExemplaries(item.getTotalExemplaries() + p.getQuantity());
        itemService.editItem(item);
        Client c = clientService.findClient(purchaseProcessService.findPurchase(purchaseId).getClientId());
        c.getPurchaseProcesses().remove(purchaseProcessService.findPurchase(purchaseId));
        clientService.editClient(c);
        purchaseProcessService.deletePurchase(purchaseId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/findpurchasebyclient")
    @PreAuthorize("hasRole('Client')")
    @ApiOperation(value = "find purchase by id", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : purchase not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<PurchaseProcess>> findPurchaseProcessByIdClient() throws Exception {
        Client c = clientService.findClientByEmail(userService.getCurrentUsername());
        List<PurchaseProcess> processList = purchaseProcessService.getPurchaseByClient(c.getId());
        if (!processList.isEmpty()) {
            return new ResponseEntity<List<PurchaseProcess>>(processList, HttpStatus.OK);
        }
        return new ResponseEntity<List<PurchaseProcess>>(HttpStatus.NO_CONTENT);
    }


    PurchaseProcessDTO mapToPurchaseDTO(PurchaseProcess process) {
        ClientDTO clientDTO = new ClientDTO();
        ItemDTO itemDTO = new ItemDTO();
        Client c = clientService.findClient(process.getClientId());
        Item i = itemService.findItemById(process.getItemId()).get();
        clientDTO.setId(c.getId());
        clientDTO.setName(c.getName());
        clientDTO.setEmail(c.getEmail());
        clientDTO.setWallet(c.getWallet());
        itemDTO.setId(i.getId());
        itemDTO.setName(i.getName());
        itemDTO.setImgUrl(i.getImgUrl());
        itemDTO.setDescription(i.getDescription());
        itemDTO.setPrice(i.getPrice());
        PurchaseProcessDTO processDTO = new PurchaseProcessDTO();
        processDTO.setClient(clientDTO);
        processDTO.setItem(itemDTO);
        processDTO.setQuantity(process.getQuantity());
        processDTO.setId(process.getId());
        return processDTO;
    }


    @PutMapping("/buy")
    @PreAuthorize("hasRole('Client')")
    @ApiOperation(value = "Buy operation done", response = Boolean.class)
    @ApiResponse(code = 200, message = "OK : successfull")
    boolean buy() throws Exception {
        float total = purchaseCalculator();
        Client c = clientService.findClientByEmail(userService.getCurrentUsername());
        c.getPurchaseProcesses().clear();
        clientService.editClient(c);
        if (total <= c.getWallet()) {
            c.setWallet(c.getWallet() - total);
            Owner o = ownerService.findOwnerById(1L);
            o.setWallet(o.getWallet() + total);
            ownerService.editOwner(o);
            clientService.editClient(c);
            List<PurchaseProcess> processList = purchaseProcessService.getPurchaseByClient(c.getId());
            for (PurchaseProcess process :
                    processList) {
                Item item = itemService.findItemById(process.getItemId()).get();
                Client client = clientService.findClient(process.getClientId());
                Sales sales = new Sales(null,
                        item.getId(),
                        client.getName(),
                        client.getEmail(),
                        item.getName(),
                        item.getImgUrl(),
                        item.getPrice(),
                        item.getDescription(),
                        process.getQuantity()
                );
                salesService.addSales(sales);
                purchaseProcessService.deletePurchase(process.getId());
            }
            return true;
        }
        return false;
    }

    float purchaseCalculator() throws Exception {
        List<PurchaseProcess> processList = findPurchaseProcessByIdClient().getBody();
        float i = 0;
        for (PurchaseProcess process : processList) {
            Item item = itemService.findItemById(process.getItemId()).get();
            i = i + (item.getPrice() * process.getQuantity());
        }
        return i;
    }


}
