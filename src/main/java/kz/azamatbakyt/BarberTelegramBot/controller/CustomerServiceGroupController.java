package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceGroupRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.CustomerServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class CustomerServiceGroupController {

    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceGroupRepository customerServiceGroupRepository;

    @Autowired
    public CustomerServiceGroupController(CustomerServiceGroupRepository customerServiceGroupRepository,
                                          CustomerServiceRepository customerServiceRepository) {
        this.customerServiceGroupRepository = customerServiceGroupRepository;
        this.customerServiceRepository = customerServiceRepository;
    }

    @GetMapping
    public String getAll(Model model) {
        List<CustomerServiceGroup> serviceGroupList = customerServiceGroupRepository.findAll();
        model.addAttribute("serviceGroupList", serviceGroupList);
        return "customerService/serviceGroupList";
    }

    @GetMapping("{id}")
    public String getServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup serviceGroup = customerServiceGroupRepository.findById(id).orElse(null);
        model.addAttribute("serviceGroup", serviceGroup);
        return "customerService/serviceGroupCard";
    }

    @GetMapping("/new")
    public String getCreateForm(Model model) {
        model.addAttribute("newServiceGroup", new CustomerServiceGroup());
        return "customerService/serviceGroupForm";
    }

    @PostMapping("/save")
    public String createServiceGroup(@ModelAttribute CustomerServiceGroup customerServiceGroup) {

        customerServiceGroupRepository.save(customerServiceGroup);

        return "redirect:/groups";
    }

    @GetMapping("/{id}/delete")
    public String deleteServiceGroup(@PathVariable("id") Long id) {
        customerServiceGroupRepository.deleteById(id);

        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String updateServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup customerServiceGroup = customerServiceGroupRepository.findById(id)
                .orElse(null);
        model.addAttribute("updatedServiceGroup", customerServiceGroup);
        return "customerService/serviceGroupEdit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updatedServiceGroup") CustomerServiceGroup serviceGroup){
        CustomerServiceGroup customerServiceGroup = customerServiceGroupRepository.findById(id).orElse(null);
        if (customerServiceGroup != null){
            customerServiceGroup.setId(serviceGroup.getId());
            customerServiceGroup.setName(serviceGroup.getName());

            customerServiceGroupRepository.save(customerServiceGroup);
        }

        return "redirect:/groups";
    }




}
