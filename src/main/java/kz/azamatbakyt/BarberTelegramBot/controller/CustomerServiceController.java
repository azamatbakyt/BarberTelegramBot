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
@RequestMapping("/services")
public class CustomerServiceController {

    private final CustomerServiceRepository customerServiceRepository;
    private final CustomerServiceGroupRepository customerServiceGroupRepository;

    @Autowired
    public CustomerServiceController(CustomerServiceRepository customerServiceRepository,
                                     CustomerServiceGroupRepository customerServiceGroupRepository) {
        this.customerServiceRepository = customerServiceRepository;
        this.customerServiceGroupRepository = customerServiceGroupRepository;
    }

    @GetMapping
    public String getAll(Model model) {
        List<CustomerService> services = customerServiceRepository.findAllByOrderByIdAsc();
        model.addAttribute("services", services);
        return "customerService/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        CustomerService service = customerServiceRepository.findById(id).orElse(null);
        model.addAttribute("service", service);
        return "customerService/card";
    }

    @GetMapping("/new")
    public String getServiceForm(Model model) {
        model.addAttribute("service", new CustomerService());
        model.addAttribute("serviceGroups", customerServiceGroupRepository.findAll());
        return "customerService/form";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute CustomerService service) {
        customerServiceRepository.save(service);
        return "redirect:/services";
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable("id") Long id) {
        customerServiceRepository.deleteById(id);
        return "redirect:/services";
    }

    @GetMapping("/{id}/edit")
    public String editById(@PathVariable("id") Long id, Model model) {
        CustomerService customerService = customerServiceRepository.findById(id).orElse(null);
        model.addAttribute("updatedService", customerService);
        return "customerService/edit";
    }

    @PostMapping("{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute("updatedService") CustomerService service) {
        CustomerService customerService = customerServiceRepository.findById(id).orElse(null);

        if (customerService != null) {
            customerService.setName(service.getName());
            customerService.setPrice(service.getPrice());
            customerService.setDuration(service.getDuration());

            customerServiceRepository.save(customerService);
        }

        return "redirect:/services";
    }




}
