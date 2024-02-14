package kz.azamatbakyt.BarberTelegramBot.service;

import kz.azamatbakyt.BarberTelegramBot.entity.Role;
import kz.azamatbakyt.BarberTelegramBot.entity.User;
import kz.azamatbakyt.BarberTelegramBot.exception.EntityNotFoundException;
import kz.azamatbakyt.BarberTelegramBot.helpers.Roles;
import kz.azamatbakyt.BarberTelegramBot.repository.RoleRepository;
import kz.azamatbakyt.BarberTelegramBot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found: " + username));
    }

    public User save(User user){
        Role role = roleRepository.findByRoleName(Roles.USER.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        user.setRole(role);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void delete(Long id){
        userRepository.deleteById(id);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));
    }
}
