package com.controller;

import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.network.ServerConfig;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.request.RegistrationRequest;

public class RegistrationWindow {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField middleName;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;

    @FXML
    private Button createAccountBtn;
    @FXML
    private Text backText;

    private String SERVER_ADDRESS;
    private int SERVER_PORT;

    public void initialize() {
        SERVER_ADDRESS = ServerConfig.getServerIp();
        SERVER_PORT = ServerConfig.getServerPort();
        System.out.println("Загружен IP: " + SERVER_ADDRESS + ", порт: " + SERVER_PORT);
    }

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.LOGIN);
    }

    @FXML
    void onEntrance() {
        Loader.loadScene((Stage) createAccountBtn.getScene().getWindow(), StagePath.LOGIN);
    }


    @FXML
    void onRegisterClicked(ActionEvent event) {
        if (firstName.getText().isEmpty() ||
                lastName.getText().isEmpty() ||
                login.getText().isEmpty() ||
                password.getText().isEmpty()) {

            AlertUtil.showAlert(
                    Alert.AlertType.WARNING,
                    "Недостаточно данных",
                    "Пожалуйста, заполните все обязательные поля (Имя, Фамилия, Логин, Пароль)."
            );
            return;
        }
        if (firstName.getText().length() > 20 ||
                lastName.getText().length() > 20 ||
                middleName.getText().length() > 20 ||
                login.getText().length() > 20 ||
                password.getText().length() > 20) {

            AlertUtil.showAlert(
                    Alert.AlertType.WARNING,
                    "Превышение длины",
                    "Каждое поле должно содержать не более 20 символов."
            );
            return;
        }

        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName(firstName.getText());
        registrationRequest.setLastName(lastName.getText());
        registrationRequest.setPatronymic(middleName.getText());
        registrationRequest.setLogin(login.getText());
        registrationRequest.setPassword(password.getText());

        Request request = new Request(Operation.REGISTER, registrationRequest);

        // Проверка подключения
        if (!ClientManager.isClientConnected()) {
            AlertUtil.showAlert(
                    Alert.AlertType.ERROR,
                    "Ошибка подключения",
                    "Нет подключения к серверу. Попробуйте позже."
            );
            return;
        }

        try {
            Response response = ClientManager.getClient().sendRequest(request);

            Platform.runLater(() -> {
                if (response.isSuccess()) {
                    AlertUtil.showAlert(
                            Alert.AlertType.INFORMATION,
                            "Успех",
                            "Регистрация прошла успешно!"
                    );
                    onEntrance();
                } else {
                    AlertUtil.showAlert(
                            Alert.AlertType.ERROR,
                            "Ошибка регистрации",
                            "Ошибка: " + response.getMessage()
                    );
                    onEntrance();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(
                    Alert.AlertType.ERROR,
                    "Сбой",
                    "Произошла ошибка при отправке запроса: " + e.getMessage()
            );
        }
    }

}