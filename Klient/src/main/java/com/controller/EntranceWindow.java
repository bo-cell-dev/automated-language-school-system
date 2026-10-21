package com.controller;

import com.dto.RoleDTO;
import com.dto.UserDTO;
import com.enums.Roles;
import com.request.LoginRequest;
import com.statics.ClientManager;
import com.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.application.Platform;
import com.enums.StagePath;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import com.util.Loader;
import com.network.Client;
import com.network.Request;
import com.network.Response;
import com.network.Operation;

public class EntranceWindow {

    @FXML
    private Button GuestEntrance;

    @FXML
    private Button UserEntrance;

    private Client client;

    @FXML
    void onGuestEntrance(ActionEvent event) {
        try {
            Client guestClient = new Client();
            guestClient.connect();

            LoginRequest guestLogin = new LoginRequest("Гость", "");
            Request loginRequest = new Request(Operation.LOGIN, guestLogin);

            Response response = guestClient.sendRequest(loginRequest);

            if (response.isSuccess()) {
                UserDTO guestUser = (UserDTO) response.getData();

                ClientManager.setClient(guestClient);
                ClientManager.setUser(guestUser);
                ClientManager.setRole(Roles.GUEST);

                Loader.loadScene((Stage) GuestEntrance.getScene().getWindow(), StagePath.SCHEDULE);
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось подключиться как гость.");
        }
    }


    @FXML
    void onUserEntrance(ActionEvent event) {
        try {
            client = new Client();
            client.connect();

            Response response = client.sendRequest(new Request(Operation.CONNECT));
            if (response.isSuccess()) {
                ClientManager.setClient(client);
                Loader.loadScene((Stage) UserEntrance.getScene().getWindow(), StagePath.LOGIN);
            } else {
                showError("Ошибка входа", response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Ошибка подключения", "Не удалось подключиться к серверу. Пожалуйста, попробуйте позже.");
        }
    }

    @FXML
    void onExitEntrance(ActionEvent event) {

        if (client != null) {
            try {
                client.sendRequest(new Request(Operation.CONNECT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Platform.exit();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
