package com.controller;

import com.dto.PersonDTO;
import com.dto.UserDTO;
import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountController {
    @FXML
    private Text backText;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label middleNameLabel;

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }

    @FXML
    public void initialize() {
        loadUserInfo();
    }

    private void loadUserInfo() {
        try {
            UserDTO currentUser = ClientManager.getCurrentUser();
            if (currentUser == null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь не авторизован.");
                return;
            }

            Request request = new Request(Operation.GET_USER_INFO, currentUser);
            Response response = ClientManager.getClient().sendRequest(request);

            if (response.isSuccess()) {
                Object data = response.getData();

                if (data instanceof UserDTO userDTO) {
                    PersonDTO person = userDTO.getPerson();
                    if (person != null) {
                        firstNameLabel.setText("Имя: " + person.getFirstName());
                        lastNameLabel.setText("Фамилия: " + person.getLastName());

                        String middleName = person.getMiddleName();
                        if (middleName == null || middleName.trim().isEmpty()) {
                            middleName = "-";
                        }

                        middleNameLabel.setText("Отчество: " + middleName);
                    }

                } else {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Получен неверный тип данных.");
                }

            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось загрузить данные пользователя.");
        }
    }
}
