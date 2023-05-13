package com.fsb.oussama_store.controller;

import com.fsb.oussama_store.models.Client;
import com.fsb.oussama_store.models.Owner;
import com.fsb.oussama_store.service.IOwnerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *           WE NEED ONLY ONE OWNER OF THE STORE
 *               THE OWNER ID ADDED MANUALLY
 * */
@RestController
@RequestMapping("/rest/owner/api")
public class OwnerController {
    @Autowired
    private IOwnerService ownerService;

    @PostMapping("/addowner")
    @ApiOperation(value = "Add a new owner", response = Client.class)
    @ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict: the owner already exist"),
            @ApiResponse(code = 201, message = "Created: the owner is successfully inserted"),
            @ApiResponse(code = 304, message = "Not Modified: the owner is unsuccessfully inserted")})
    public ResponseEntity<Owner> createNewOwner(@RequestBody Owner owner) {
        Owner owner1 = ownerService.addOwner(owner);
        if (owner1 != null && owner1.getId() != null) {
            return new ResponseEntity<Owner>(owner1, HttpStatus.CREATED);
        }
        return new ResponseEntity<Owner>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/updateowner")
    @ApiOperation(value = "update a owner", response = Owner.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Not Found : the owner does not exist"),
            @ApiResponse(code = 200, message = "OK: the owner successfully updated"),
            @ApiResponse(code = 304, message = "Not Modified : the owner unsuccessfully updated")})
    public ResponseEntity<Owner> updateOwner(@RequestBody Owner owner) {
        if (!ownerService.checkIfExist(owner.getId())) {
            return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
        }
        Owner owner1 = ownerService.editOwner(owner);
        if (owner1 != null) {
            return new ResponseEntity<Owner>(HttpStatus.OK);
        }
        return new ResponseEntity<Owner>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/findowner")
    @ApiOperation(value = "find owner by id", response = Owner.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : owner not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<Owner> findOwnerById(@RequestParam("id") Long id) {
        Owner owner = ownerService.findOwnerById(id);
        if (owner != null) {
            return new ResponseEntity<Owner>(owner, HttpStatus.OK);
        }
        return new ResponseEntity<Owner>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteowner/{ownerId}")
    @ApiOperation(value = "Delete a owner, if the owner does not exist, nothing is done", response = String.class)
    @ApiResponse(code = 204, message = "No Content: owner sucessfully deleted")
    public ResponseEntity<String> deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/allowners")
    @ApiOperation(value = "find all owners", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no owners found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Owner>> getAllOwners() {
        List<Owner> allOwners = ownerService.getAllOwners();
        if (!allOwners.isEmpty()) {
            return new ResponseEntity<List<Owner>>(allOwners, HttpStatus.OK);
        }
        return new ResponseEntity<List<Owner>>(HttpStatus.NO_CONTENT);
    }
}
