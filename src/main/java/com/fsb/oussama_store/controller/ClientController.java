package com.fsb.oussama_store.controller;

import com.fsb.oussama_store.authentication.Service.UserService;
import com.fsb.oussama_store.authentication.entity.User;
import com.fsb.oussama_store.models.Client;
import com.fsb.oussama_store.service.IClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/client/api/")
public class ClientController {
    @Autowired
    private IClientService iClientService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/addclient")
    @ApiOperation(value = "Add a new client", response = Client.class)
    @ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict: the client already exist"),
            @ApiResponse(code = 201, message = "Created: the client is successfully inserted"),
            @ApiResponse(code = 304, message = "Not Modified: the client is unsuccessfully inserted"),
            @ApiResponse(code = 500, message = "this mail already exist")})
    public ResponseEntity<Client> createNewClient(@RequestBody Client client) {
        Client client1 = iClientService.addClient(client);
        if (client1 != null && client1.getId() != null) {
            User user = new User(client.getEmail(),
                    client.getName(),
                    client.getPassword(), null);
            userService.registerNewUser(user);


            return new ResponseEntity<Client>(client1, HttpStatus.CREATED);


        }
        return new ResponseEntity<Client>(HttpStatus.NOT_MODIFIED);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /*
     * when i need
     */
    @PutMapping("/updateclient")
    @ApiOperation(value = "update a client", response = Client.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Not Found : the client does not exist"),
            @ApiResponse(code = 200, message = "OK: the client successfully updated"),
            @ApiResponse(code = 304, message = "Not Modified : the client unsuccessfully updated")})
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        if (!iClientService.checkIfExist(client.getId())) {
            return new ResponseEntity<Client>(HttpStatus.NOT_FOUND);
        }
        Client client1 = iClientService.editClient(client);
        if (client1 != null) {
            return new ResponseEntity<Client>(HttpStatus.OK);
        }
        return new ResponseEntity<Client>(HttpStatus.NOT_MODIFIED);
    }

    @GetMapping("/findclient")
    @PreAuthorize("hasRole('Owner')")
    @ApiOperation(value = "find client by id", response = Client.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : client not found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<Client> findClientById(@RequestParam("id") Long id) {
        Client client = iClientService.findClient(id);
        if (client != null) {
            return new ResponseEntity<Client>(client, HttpStatus.OK);
        }
        return new ResponseEntity<Client>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteclient/{clientId}")
    @PreAuthorize("hasRole('Owner')")
    @ApiOperation(value = "Delete a client, if the client does not exist, nothing is done", response = String.class)
    @ApiResponse(code = 204, message = "No Content: client sucessfully deleted")
    public ResponseEntity<String> deleteClient(@PathVariable("clientId") Long clientId) {
        System.out.println(iClientService.findClient(clientId));
        userService.deleteUser(iClientService.findClient(clientId).getEmail());
        iClientService.deleteClient(clientId);
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allclients")
    @PreAuthorize("hasRole('Owner')")
    @ApiOperation(value = "find allClients", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no clients found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> allClients = iClientService.getAllClients();
        if (!allClients.isEmpty()) {
            return new ResponseEntity<List<Client>>(allClients, HttpStatus.OK);
        }
        return new ResponseEntity<List<Client>>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allemails")
    @ApiOperation(value = "find allMails", response = List.class)
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No Content : no mails found"),
            @ApiResponse(code = 200, message = "OK : successfull reserch")})
    public ResponseEntity<List<String>> allEmails() {
        List<Client> allClients = this.iClientService.getAllClients();
        List<String> allEmails = allClients.stream()
                .map(client -> client.getEmail()).collect(Collectors.toList());
        System.out.println(new ResponseEntity<List<String>>(allEmails, HttpStatus.OK));
        return new ResponseEntity<List<String>>(allEmails, HttpStatus.OK);

    }


}
