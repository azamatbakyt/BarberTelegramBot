package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.CustomerServiceGroup;
import kz.azamatbakyt.BarberTelegramBot.service.CSGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class CustomerServiceGroupController {

    private final CSGService csgService;

    @Autowired
    public CustomerServiceGroupController(CSGService csgService) {
        this.csgService = csgService;
    }

    @GetMapping
    public String getAll(Model model) {
        List<CustomerServiceGroup> serviceGroupList = csgService.getServiceGroups();
        model.addAttribute("serviceGroupList", serviceGroupList);
        return "customerService/serviceGroupList";
    }

    @GetMapping("{id}")
    public String getServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup serviceGroup = csgService.getServiceGroup(id);
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

        csgService.saveServiceGroup(customerServiceGroup);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/delete")
    public String deleteServiceGroup(@PathVariable("id") Long id) {
        csgService.deleteGroupById(id);

        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String updateServiceGroup(@PathVariable("id") Long id, Model model) {
        CustomerServiceGroup customerServiceGroup = csgService.getServiceGroup(id);
        model.addAttribute("updatedServiceGroup", customerServiceGroup);
        return "customerService/serviceGroupEdit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updatedServiceGroup") CustomerServiceGroup serviceGroup) {
        CustomerServiceGroup customerServiceGroup = csgService.getServiceGroup(id);
        if (customerServiceGroup != null) {
            customerServiceGroup.setId(serviceGroup.getId());
            customerServiceGroup.setName(serviceGroup.getName());

            csgService.saveServiceGroup(customerServiceGroup);
        }

        return "redirect:/groups";
    }


}
