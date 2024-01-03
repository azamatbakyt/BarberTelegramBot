package kz.azamatbakyt.BarberTelegramBot.controller;


import kz.azamatbakyt.BarberTelegramBot.entity.User;
import kz.azamatbakyt.BarberTelegramBot.repository.RoleRepository;
import kz.azamatbakyt.BarberTelegramBot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepo;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepo) {
        this.userService = userService;
        this.roleRepo = roleRepo;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }


    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("userToUpdate", userService.getUserById(id));
        return "users/edit";

    }

    @PostMapping("/{id}")
    public String saveEditedUser(@PathVariable("id") Long id,
                                 @ModelAttribute("userToUpdate") User user) {
        User userToUpdate = userService.getUserById(id);
        if (userToUpdate != null) {
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setPassword(user.getPassword());
            userToUpdate.setPhoneNumber(user.getPhoneNumber());

            userService.save(userToUpdate);
        }
        return "redirect:/users";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);

        return "redirect:/users";
    }
}
