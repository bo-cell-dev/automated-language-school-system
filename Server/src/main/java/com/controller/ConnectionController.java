package com.controller;

import com.dto.RoleDTO;
import com.dto.UserConnectionDTO;
import com.dto.UserDTO;
import com.network.Response;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class ConnectionController {

    private final List<UserDTO> connectedUsers = new CopyOnWriteArrayList<>();

    public void addUser(UserDTO user) {
        if (user == null) return;

        if (user.getId() == null || connectedUsers.stream().noneMatch(u -> u.getId() != null && u.getId().equals(user.getId()))) {
            connectedUsers.add(user);
            System.out.println("Пользователь добавлен: login=" + user.getLogin() + ", role=" + user.getRole().getName());
        }
    }



    public void removeUser(UserDTO user) {
        if (user != null) {
            connectedUsers.removeIf(u -> u.getId().equals(user.getId()));
        }
    }

    public Response getConnectedUsers() {
        List<UserConnectionDTO> dtoList = new ArrayList<>();

        int guestCounter = 1;

        for (UserDTO user : connectedUsers) {
            String roleName = user.getRole().getName();
            String loginDisplay;

            if ("GUEST".equalsIgnoreCase(roleName)) {
                loginDisplay = "Гость" + guestCounter++;
            } else {
                loginDisplay = user.getLogin();
            }

            dtoList.add(new UserConnectionDTO(loginDisplay, roleName));
        }

        return new Response(true, "Список подключённых", dtoList);
    }

}
