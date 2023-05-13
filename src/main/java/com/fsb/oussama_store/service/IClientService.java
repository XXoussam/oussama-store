package com.fsb.oussama_store.service;

import com.fsb.oussama_store.models.Client;

import java.util.List;

public interface IClientService {
    public Client addClient(Client client);

    public Client editClient(Client client);

    public Client findClient(Long clientId);

    public void deleteClient(Long clientId);

    public boolean checkIfExist(Long id);

    public List<Client> getAllClients();

    Client findClientByEmail(String email);
}
