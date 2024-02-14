package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CSService {

    private final CustomerServiceRepository customerServiceRepository;

    @Autowired
    public CSService(CustomerServiceRepository customerServiceRepository) {
        this.customerServiceRepository = customerServiceRepository;
    }

    public List<CustomerService> getServices() {
        return customerServiceRepository.findAllByOrderByIdAsc();
    }

    public CustomerService getServiceById(Long id) {
        return customerServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Услуга не найдена"));
    }

    public CustomerService getServiceByName(String name){
        return customerServiceRepository.findByName(name);
    }

    public void saveCustomerService(CustomerService service) {
        customerServiceRepository.save(service);
    }

    public void deleteById(Long id) {
        customerServiceRepository.deleteById(id);
    }

}
