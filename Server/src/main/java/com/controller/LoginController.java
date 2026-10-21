package com.controller;

import com.dto.PersonDTO;
import com.dto.RoleDTO;
import com.network.Response;
import com.dto.LoginDTO;
import com.dto.UserDTO;
import com.models.User;
import com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserRepository userRepo;

    public LoginController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    private RoleDTO getGuestRole() {
        RoleDTO guestRole = new RoleDTO();
        guestRole.setName("GUEST");
        return guestRole;
    }

    public Response login(LoginDTO loginDTO) {
        String login = loginDTO.getLogin();
        String password = loginDTO.getPassword();

        log.info("Попытка входа для логина: {}", login);

        if ("Гость".equals(login)) {
            long guestId = System.currentTimeMillis();
            UserDTO guestUser = new UserDTO();
            guestUser.setId(guestId);
            guestUser.setLogin("Гость_" + guestId);
            guestUser.setRole(getGuestRole());

            return new Response(true, "Гость вошёл", guestUser);
        }

        Optional<User> userOpt = userRepo.findByLogin(login);
        if (userOpt.isEmpty()) {
            log.warn("Логин не найден: {}", login);
            return new Response(false, "Пользователь не найден");
        }

        User user = userOpt.get();

        if (!user.getPassword().equals(password)) {
            log.warn("Неверный пароль для логина: {}", login);
            return new Response(false, "Неверный пароль");
        }

        log.info("Успешный вход: {}", login);

        UserDTO userDTO = new UserDTO(user.getId(), user.getLogin(), user.getPassword(),
                new PersonDTO(user.getPerson()), new RoleDTO(user.getRole()));

        return new Response(true, "Вход выполнен", userDTO);
    }

}
