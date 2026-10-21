package com.controller;

import com.models.Person;
import com.models.Role;
import com.models.User;
import com.repository.PersonRepository;
import com.repository.RoleRepository;
import com.repository.UserRepository;
import com.dto.RegistrationDTO;  // Импортируем DTO
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Log4j2
public class RegistrationController {

    private final PersonRepository personRepo;
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;


    public String register(RegistrationDTO dto) {
        log.info("Регистрация пользователя: {}", dto.getLogin());

        // Проверка на существование пользователя с таким логином
        Optional<User> existingUser = userRepo.findByLogin(dto.getLogin());
        if (existingUser.isPresent()) {
            log.warn("Попытка зарегистрировать пользователя с логином, который уже существует: {}", dto.getLogin());
            return "Логин уже занят"; // Если логин занят, возвращаем сообщение об ошибке
        }

        // Если логин не занят, продолжаем регистрацию
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setMiddleName(dto.getMiddleName());
        personRepo.save(person);

        String roleName = "user".trim();
        Optional<Role> role = roleRepo.findByName(roleName);
        if (role.isEmpty()) {
            log.error("Роль с именем '{}' не найдена", roleName);
            throw new RuntimeException("Роль не найдена");
        }

        User user = new User();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        user.setPerson(person);
        user.setRole(role.get());
        userRepo.save(user);

        log.info("Успешная регистрация: {}", dto.getLogin());
        return "Зарегистрирован";
    }
}
