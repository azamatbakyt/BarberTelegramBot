package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CustomerServiceGroupRepository extends JpaRepository<CustomerServiceGroup, Long> {
    CustomerServiceGroup findByName(String name);

}
