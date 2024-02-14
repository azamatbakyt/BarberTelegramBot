package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByChatId(Long chatId);




}
