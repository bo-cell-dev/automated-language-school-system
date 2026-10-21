package com.statics;

import com.dto.LessonDTO;
import com.dto.UserDTO;
import com.network.Client;
import javafx.collections.ObservableList;
import lombok.Getter;

import com.enums.Roles;

@Getter
public class ClientManager {
    private static Client client;
    private static LessonDTO selectedLesson;
    private static UserDTO currentUser;
    private static Roles role;

    public static Client getClient() {
        return client;
    }
    private static ObservableList<LessonDTO> lessons;

    public static ObservableList<LessonDTO> getLessons() {
        return lessons;
    }

    public static void setLessons(ObservableList<LessonDTO> lessons) {
        ClientManager.lessons = lessons;
    }
    public static void setClient(Client c) {
        client = c;
    }

    public static boolean isClientConnected() {
        return client != null && client.isConnected();
    }

    public static void setSelectedLesson(LessonDTO lesson) {
        selectedLesson = lesson;
    }

    public static LessonDTO getSelectedLesson() {
        return selectedLesson;
    }

    public static UserDTO getCurrentUser() {
        return currentUser;
    }
    public static boolean isGuest() {
        return role == Roles.GUEST;
    }

    public static boolean isUser() {
        return role == Roles.USER;
    }

    public static boolean isAdmin() {
        return role == Roles.ADMIN;
    }

    public static void setRole(Roles newRole) {
        role = newRole;
    }

    public static void setUser(UserDTO user) {
        currentUser = user;
        if (user != null && user.getRole() != null) {
            role = Roles.valueOf(user.getRole().getName().toUpperCase());
        }
    }

    public static void setGuest() {
        role = Roles.GUEST;
        currentUser = null;
        client = null;
    }
    public static void setUser() {
        role = Roles.USER;
        currentUser = null;
        client = null;
    }
    public static void setAdmin() {
        role = Roles.ADMIN;
        currentUser = null;
        client = null;
    }

    public static void clear() {
        role = null;
        currentUser = null;
        client = null;
    }
    public static void setCurrentUser(UserDTO user) {
        currentUser = user;
        if (user != null && user.getRole() != null) {
            role = Roles.valueOf(user.getRole().getName().toUpperCase());
        }
    }
}
