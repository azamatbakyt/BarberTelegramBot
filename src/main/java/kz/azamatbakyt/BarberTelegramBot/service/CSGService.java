package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.exception.ServiceGroupNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceGroupRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceRepository;


@Service
public class CSGService {

    private final CustomerServiceGroupRepository customerServiceGroupRepository;

    @Autowired
    public CSGService(CustomerServiceGroupRepository customerServiceGroupRepository) {
        this.customerServiceGroupRepository = customerServiceGroupRepository;
    }

    public List<CustomerServiceGroup> getServiceGroups() {
        return customerServiceGroupRepository.findAll();
    }

    public CustomerServiceGroup getService(Long id) {
        return customerServiceGroupRepository.findById(id)
                .orElseThrow(() -> new ServiceGroupNotFoundException("Данная группа услуг не найдена"));
    }

    public void saveServiceGroup(CustomerServiceGroup group) {
        customerServiceGroupRepository.save(group);
    }

    public void deleteGroupById(Long id) {
        customerServiceGroupRepository.deleteById(id);
    }


}
