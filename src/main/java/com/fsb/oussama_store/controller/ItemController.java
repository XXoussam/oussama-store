package com.fsb.oussama_store.controller;

import com.fsb.oussama_store.models.Item;
import com.fsb.oussama_store.service.IItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/item/api/")
public class ItemController {
    @Autowired
    private IItemService itemService;

    @PreAuthorize("hasRole('Owner')")
    @PostMapping("/additem")
    @ApiOperation(value = "Add a new item", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict: the item already exist"),
            @ApiResponse(code = 201, message = "Created: the item is successfully inserted"),
            @ApiResponse(code = 304, message = "Not Modified: the item is unsuccessfully inserted")})
    public ResponseEntity<Item> createNewItem(@RequestBody Item item) {
        Item item1 = itemService.addItem(item);
        if (item1 != null && item1.getId() != null) {
            return new ResponseEntity<Item>(item1, HttpStatus.CREATED);
        }
        return new ResponseEntity<Item>(HttpStatus.NOT_MODIFIED);
    }

    @PreAuthorize("hasRole('Owner')")
    @PutMapping("/updateitem")
    @ApiOperation(value = "update a item", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Not Found : the item does not exist"),
            @ApiResponse(code = 200, message = "OK: the item successfully updated"),
            @ApiResponse(code = 304, message = "Not Modified : the item unsuccessfully updated")})
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        System.out.println("/////////////////////////////////////");
        if (!itemService.checkIfExist(item.getId())) {
            return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
        }
        Item item1 = itemService.editItem(item);
        if (item1 != null) {
            return new ResponseEntity<Item>(HttpStatus.OK);
        }
        return new ResponseEntity<Item>(HttpStatus.NOT_MODIFIED);
    }

    @PreAuthorize("hasAnyRole('Owner','Client')")
    @GetMapping("/finditem")
    @ApiOperation(value = "find item by id", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : item not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<Optional<Item>> findItemById(@RequestParam("id") Long id) {
        Optional<Item> item = itemService.findItemById(id);
        if (item.isPresent()) {
            return new ResponseEntity<Optional<Item>>(item, HttpStatus.OK);
        }
        return new ResponseEntity<Optional<Item>>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('Owner')")
    @DeleteMapping("/deleteitem/{itemId}")
    @ApiOperation(value = "Delete a item, if the item does not exist, nothing is done", response = String.class)
    @ApiResponse(code = 204, message = "No Content: item sucessfully deleted")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('Owner','Client')")
    @GetMapping("/allitems")
    @ApiOperation(value = "find all items", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no items found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> allItems = itemService.getAllItems();
        if (!allItems.isEmpty()) {
            return new ResponseEntity<List<Item>>(allItems, HttpStatus.OK);
        }
        return new ResponseEntity<List<Item>>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('Owner','Client')")
    @GetMapping("/finditemByName")
    @ApiOperation(value = "find item by name", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : item not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Item>> findItemByName(@RequestParam("name") String name) {
        List<Item> items = itemService.findItemByName(name);
        if (!items.isEmpty()) {
            return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
        }
        return new ResponseEntity<List<Item>>(HttpStatus.NO_CONTENT);
    }

    /*@PutMapping("/conflit")
    public void pushItem(){
        Optional<Item> i =itemService.findItemById(12L);
        Item i1 =itemService.findItemById(15L).get();
        Client c = clientService.findClient(1L);
        System.out.println("////////////////////////");
        System.out.println(i);
        System.out.println(i1);
        //c.getProcurement().add(i.get());
        c.getProcurement().add(i1);
        clientService.editClient(c);
        System.out.println(c);

    }*/
}
