package com.controller;

import com.dto.LessonDTO;
import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.dto.UserDTO;
import com.request.RegisterToLessonRequest;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.Serializable;

public class LessonDetailsController {

    @FXML
    private Button cancelRegisterBtn;

    @FXML
    private Button registerBtn;

    @FXML
    private Text timeText;

    @FXML
    private Text languageText;

    @FXML
    private Text levelText;

    @FXML
    private Text teacherText;

    @FXML
    private Text backText;

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }

    private LessonDTO lesson;

    @FXML
    public void initialize() {
        lesson = ClientManager.getSelectedLesson();
        if (lesson != null) {
            updateUI();
        }
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson = lesson;
        updateUI();
    }

    private void updateUI() {
        timeText.setText("Время: " + lesson.getTime());
        languageText.setText("Язык: " + lesson.getLanguage());
        levelText.setText("Уровень: " + lesson.getLevel());
        teacherText.setText("Преподаватель: " + lesson.getTeacher());
    }
    @FXML
    private void handleRegister() {
        try {
            if (lesson == null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Нет выбранного урока.");
                return;
            }

            UserDTO user = ClientManager.getCurrentUser();
            if (user == null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь не авторизован.");
                return;
            }
            RegisterToLessonRequest regReq = new RegisterToLessonRequest(lesson.getId(), user);
            Request request = new Request(Operation.REGISTER_TO_LESSON, regReq);

            Response response = ClientManager.getClient().sendRequest(request);

            if (response.isSuccess()) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Успешно", "Вы успешно записались на урок.");
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при регистрации: " + e.getMessage());
        }
    }


    @FXML
    private void handleCancelRegister() {
        try {
            if (lesson == null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Нет выбранного урока.");
                return;
            }

            UserDTO user = ClientManager.getCurrentUser();
            if (user == null) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Пользователь не авторизован.");
                return;
            }

            RegisterToLessonRequest cancelReq = new RegisterToLessonRequest(lesson.getId(), user);
            Request request = new Request(Operation.CANCEL_REGISTER_TO_LESSON, cancelReq);

            Response response = ClientManager.getClient().sendRequest(request);

            if (response.isSuccess()) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Успешно", "Вы успешно отменили запись на урок.");
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при отмене записи: " + e.getMessage());
        }
    }

}
