package kz.azamatbakyt.BarberTelegramBot.controller;

import kz.azamatbakyt.BarberTelegramBot.entity.Client;
import kz.azamatbakyt.BarberTelegramBot.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String getClients(Model model){
        model.addAttribute("clients", clientService.getAll());

        return "clients/list";
    }

    @GetMapping("/new")
    public String newClient(Model model){
        model.addAttribute("newClient", new Client());
        return "clients/form";
    }

    @PostMapping("/save")
    public String createClient(@ModelAttribute Client client){
        clientService.save(client);

        return "redirect:/clients";

    }

    @GetMapping("/{id}/edit")
    public String editClient(@PathVariable("id") Long id, Model model){
        model.addAttribute("clientToUpdate", clientService.getClient(id));

        return "clients/edit";
    }

    @PostMapping("{id}")
    public String updateClient(@PathVariable("id") Long id,
                               @ModelAttribute("clientToUpdate") Client client){
        Client clientToUpdate = clientService.getClient(id);
        if (clientToUpdate != null){
            clientToUpdate.setName(client.getName());
            clientToUpdate.setChatId(null);
            clientToUpdate.setPhone(client.getPhone());

            clientService.save(clientToUpdate);
        }

        return "redirect:/clients";
    }

    @GetMapping("/{id}/delete")
    public String deleteClient(@PathVariable("id") Long id){
        clientService.delete(id);

        return "redirect:/clients";
    }
}
