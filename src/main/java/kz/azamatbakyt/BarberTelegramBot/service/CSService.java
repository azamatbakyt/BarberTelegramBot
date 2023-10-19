package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.exception.ServiceNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceGroupRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSService {

    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceGroupRepository customerServiceGroupRepository;

    @Autowired
    public CSService(CustomerServiceRepository customerServiceRepository, CustomerServiceGroupRepository customerServiceGroupRepository) {
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceGroupRepository = customerServiceGroupRepository;
    }

    public List<CustomerService> getServices() {
        return customerServiceRepository.findAllByOrderByIdAsc();
    }

    public CustomerService getServiceById(Long id) {
        return customerServiceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Услуга не найдена"));
    }

    public void saveCustomerService(CustomerService service) {
        customerServiceRepository.save(service);
    }

    public void deleteById(Long id) {
        customerServiceRepository.deleteById(id);
    }

}
