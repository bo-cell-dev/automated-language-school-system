package com.controller;

import com.dto.UserDTO;
import com.enums.StagePath;
import com.network.Client;
import com.network.Request;
import com.network.Response;
import com.request.LoginRequest;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import com.network.Operation;
import com.statics.ClientManager;
import javafx.stage.Stage;

public class LoginWindow {
    @FXML
    private Button Entrance;

    @FXML
    private Text RegisterEntrance;

    @FXML
    private Text backText;
    @FXML
    private TextField username;
    @FXML
    private PasswordField passwordF;

    private Client client;
    public void setClient(Client client) {
        this.client = client;
        System.out.println("Клиент установлен в ScheduleWindow: " + client);
    }

    public Client getClient() {
        return client;
    }

    @FXML
    void handleRegistration(MouseEvent event) {
        Loader.loadScene((Stage) RegisterEntrance.getScene().getWindow(), StagePath.REGISTER);
    }

    @FXML
    void handleBack(MouseEvent event) {
        System.out.println("Нажатие назад зафиксировано");
        client = ClientManager.getClient();
        if (client != null) {
            client.disconnect();
        }
        ClientManager.clear();
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.ENTRANCE);
    }

    @FXML
    void onEntrance(ActionEvent event) {
        String login = username.getText();
        String password = passwordF.getText();

        if (login.isEmpty() || password.isEmpty()) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Ошибка", "Введите логин и пароль");
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(login);
        loginRequest.setPassword(password);

        Request request = new Request(Operation.LOGIN, loginRequest);

        try {
            Response response = ClientManager.getClient().sendRequest(request);

            if (response.isSuccess()) {
                Object rawData = response.getData();

                if (rawData instanceof UserDTO userDTO) {
                    System.out.println("Вошёл пользователь: " + userDTO.getLogin());
                    ClientManager.setUser(userDTO);
                } else {
                    System.out.println("Получен неожиданный объект: " + rawData);
                }

                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Успешный вход", "Добро пожаловать!");
                Loader.loadScene((Stage) Entrance.getScene().getWindow(), StagePath.SCHEDULE);
            } else {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Ошибка входа", response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка подключения", "Не удалось связаться с сервером");
        }
    }
}