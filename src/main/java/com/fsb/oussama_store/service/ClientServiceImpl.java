package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Client;
import com.fsb.oussama_store.rep.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements IClientService {
    @Autowired
    private ClientRepo clientRepo;


    @Override
    public Client addClient(Client client) {
        return clientRepo.save(client);
    }

    @Override
    public Client editClient(Client client) {
        return clientRepo.save(client);
    }

    @Override
    public Client findClient(Long clientId) {
        return clientRepo.findById(clientId).get();
    }

    @Override
    public void deleteClient(Long clientId) {
        clientRepo.deleteById(clientId);
    }

    @Override
    public boolean checkIfExist(Long id) {
        return clientRepo.existsById(id);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientRepo.findByEmail(email);
    }
}
