package kz.azamatbakyt.BarberTelegramBot.service;


import kz.azamatbakyt.BarberTelegramBot.entity.Clients;
import kz.azamatbakyt.BarberTelegramBot.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientsService {

    private final ClientsRepository clientsRepository;

    @Autowired
    public ClientsService(ClientsRepository clientsRepository) {
        this.clientsRepository = clientsRepository;
    }



    public Clients getClientByChatId(String chatId){
        return clientsRepository.findClientByChatId(chatId);
    }


    public void save(Clients client){
        if (client != null) {
            clientsRepository.save(client);
        }
    }


}
