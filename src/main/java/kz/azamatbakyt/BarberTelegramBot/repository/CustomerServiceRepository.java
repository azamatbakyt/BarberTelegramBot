package kz.azamatbakyt.BarberTelegramBot.repository;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerServiceRepository extends JpaRepository<CustomerService, Long> {
    CustomerService findByName(String name);
    List<CustomerService> findAllByOrderByIdAsc();
}
