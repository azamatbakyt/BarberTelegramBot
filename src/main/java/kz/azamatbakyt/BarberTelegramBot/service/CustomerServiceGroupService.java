package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.exception.ServiceGroupNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceGroupService {

    private final CustomerServiceGroupRepository customerServiceGroupRepository;

    @Autowired
    public CustomerServiceGroupService(CustomerServiceGroupRepository customerServiceGroupRepository) {
        this.customerServiceGroupRepository = customerServiceGroupRepository;
    }

    public List<CustomerServiceGroup> getServiceGroups() {
        return customerServiceGroupRepository.findAll();
    }

    public CustomerServiceGroup getServiceGroup(Long id) {
        return customerServiceGroupRepository.findById(id)
                .orElseThrow(() -> new ServiceGroupNotFoundException("Данная группа услуг не найдена"));
    }

    public CustomerServiceGroup getServiceGroupByName(String name){
        return customerServiceGroupRepository.findByName(name);
    }


    public void saveServiceGroup(CustomerServiceGroup group) {
        customerServiceGroupRepository.save(group);
    }


    public void deleteGroupById(Long id) {
        customerServiceGroupRepository.deleteById(id);
    }


}
