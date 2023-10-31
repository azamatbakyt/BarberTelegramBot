package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ClientsRepository extends JpaRepository<Clients, Long> {

    Clients findClientByChatId(String chatId);




}
