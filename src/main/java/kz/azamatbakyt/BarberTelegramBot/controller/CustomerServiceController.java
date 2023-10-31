package kz.azamatbakyt.BarberTelegramBot.controller;

import jakarta.validation.Valid;
import kz.azamatbakyt.BarberTelegramBot.entity.CustomerService;
import kz.azamatbakyt.BarberTelegramBot.service.CustomerServiceGroupService;
import kz.azamatbakyt.BarberTelegramBot.service.CSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/services")
public class CustomerServiceController {

    private final CSService csService;
    private final CustomerServiceGroupService customerServiceGroupService;

    @Autowired
    public CustomerServiceController(CSService csService, CustomerServiceGroupService customerServiceGroupService) {
        this.csService = csService;
        this.customerServiceGroupService = customerServiceGroupService;
    }

    @GetMapping
    public String getAll(Model model) {
        List<CustomerService> services = csService.getServices();
        model.addAttribute("services", services);
        return "customerService/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long id, Model model) {
        CustomerService service = csService.getServiceById(id);
        model.addAttribute("service", service);
        return "customerService/card";
    }

    @GetMapping("/new")
    public String getServiceForm(Model model) {
        model.addAttribute("service", new CustomerService());
        model.addAttribute("serviceGroups", customerServiceGroupService.getServiceGroups());
        return "customerService/form";
    }

    @PostMapping("/save")
    public String saveService(@ModelAttribute @Valid CustomerService service,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "customerService/form";
        }
        csService.saveCustomerService(service);
        return "redirect:/services";
    }

    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable("id") Long id) {
        csService.deleteById(id);
        return "redirect:/services";
    }

    @GetMapping("/{id}/edit")
    public String editById(@PathVariable("id") Long id, Model model) {
        CustomerService customerService = csService.getServiceById(id);
        model.addAttribute("updatedService", customerService);
        model.addAttribute("serviceGroups", customerServiceGroupService.getServiceGroups());
        return "customerService/edit";
    }

    @PostMapping("{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updatedService") @Valid CustomerService service,
                         BindingResult bindingResult) {
        CustomerService customerService = csService.getServiceById(id);

        if (bindingResult.hasErrors())
            return "customerService/edit";


        if (customerService != null) {
            customerService.setName(service.getName());
            customerService.setPrice(service.getPrice());
            customerService.setDuration(service.getDuration());

            csService.saveCustomerService(customerService);
        }

        return "redirect:/services";
    }


}
