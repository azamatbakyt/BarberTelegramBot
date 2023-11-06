package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }



    public Client getClientByChatId(Long chatId){
        return clientRepository.findClientByChatId(chatId);
    }


    public void save(Client client){
        if (client != null) {
            clientRepository.save(client);
        }
    }

    public List<Client> getAll(){
        return clientRepository.findAll();
    }

    public Client getClient(Long id){
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Клиент не найден"));
    }

    public void delete(Long id){
        clientRepository.deleteById(id);
    }

}
