package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.service.CustomerServiceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class CustomerServiceGroupController {

    private final CustomerServiceGroupService customerServiceGroupService;

    @Autowired
    public CustomerServiceGroupController(CustomerServiceGroupService customerServiceGroupService) {
        this.customerServiceGroupService = customerServiceGroupService;
    }

    @GetMapping
    public String getAll(Model model) {
        List<CustomerServiceGroup> serviceGroupList = customerServiceGroupService.getServiceGroups();
        model.addAttribute("serviceGroupList", serviceGroupList);
        return "customerService/serviceGroupList";
    }

    @GetMapping("{id}")
    public String getServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup serviceGroup = customerServiceGroupService.getServiceGroup(id);
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

        customerServiceGroupService.saveServiceGroup(customerServiceGroup);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/delete")
    public String deleteServiceGroup(@PathVariable("id") Long id) {
        customerServiceGroupService.deleteGroupById(id);

        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String updateServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup customerServiceGroup = customerServiceGroupService.getServiceGroup(id);
        model.addAttribute("updatedServiceGroup", customerServiceGroup);
        return "customerService/serviceGroupEdit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updatedServiceGroup") CustomerServiceGroup serviceGroup) {
        CustomerServiceGroup customerServiceGroup = customerServiceGroupService.getServiceGroup(id);
        if (customerServiceGroup != null) {
            customerServiceGroup.setId(serviceGroup.getId());
            customerServiceGroup.setName(serviceGroup.getName());

            customerServiceGroupService.saveServiceGroup(customerServiceGroup);
        }

        return "redirect:/groups";
    }


}
