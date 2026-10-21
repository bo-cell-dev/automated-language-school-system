package com.controller;

import com.dto.LessonDTO;
import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.request.EditLessonRequest;
import com.request.EditLessonRequest2;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditLessonController {
    @FXML private Text backText;
    @FXML private Button addButton, saveButton, delButton, cancelButton;
    @FXML private TextField teacherField;
    @FXML private TextField languageField;
    @FXML private TextField timeField;
    @FXML private TextField levelField;
    @FXML private TextField lessonTypeField;
    @FXML private TextField dayField;

    private LessonDTO currentLesson;

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }
    @FXML
    public void initialize() {
        currentLesson = ClientManager.getSelectedLesson();

        if (currentLesson != null) {

            teacherField.setText(currentLesson.getTeacher());
            lessonTypeField.setText(currentLesson.getType());
            timeField.setText(currentLesson.getTime());
            levelField.setText(currentLesson.getLevel());
            languageField.setText(currentLesson.getLanguage());
            dayField.setText(currentLesson.getDay());

            addButton.setVisible(false);
            addButton.setManaged(false);

            saveButton.setVisible(true);
            saveButton.setManaged(true);

            delButton.setVisible(true);
            delButton.setManaged(true);

            cancelButton.setVisible(true);
            cancelButton.setManaged(true);
        } else {

            addButton.setVisible(true);
            addButton.setManaged(true);

            saveButton.setVisible(false);
            saveButton.setManaged(false);

            delButton.setVisible(false);
            delButton.setManaged(false);

            cancelButton.setVisible(true);
            cancelButton.setManaged(true);
        }
    }


    @FXML
    void onAddBtn(ActionEvent event) {
        if (teacherField.getText().length() > 12 ||
                lessonTypeField.getText().length() > 12 ||
                timeField.getText().length() > 12 ||
                levelField.getText().length() > 12 ||
                languageField.getText().length() > 12 ||
                dayField.getText().length() > 12) {

            AlertUtil.showAlert(Alert.AlertType.WARNING,
                    "Ошибка валидации",
                    "Каждое поле должно содержать не более 12 символов.");
            return;
        }

        EditLessonRequest editLessonRequest = new EditLessonRequest();

        editLessonRequest.setTeacher(teacherField.getText());
        editLessonRequest.setLessonType(lessonTypeField.getText());
        editLessonRequest.setTime(timeField.getText());
        editLessonRequest.setLevel(levelField.getText());
        editLessonRequest.setLanguage(languageField.getText());
        editLessonRequest.setDay(dayField.getText());

        Request request = new Request(Operation.ADD_LESSON, editLessonRequest);

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

                } else {
                    AlertUtil.showAlert(
                            Alert.AlertType.ERROR,
                            "Ошибка регистрации",
                            "Ошибка: " + response.getMessage()
                    );
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


    @FXML
    void onDelBtn(ActionEvent event) {
        LessonDTO lesson = ClientManager.getSelectedLesson();
        if (lesson == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Ошибка", "Выберите урок для удаления");
            return;
        }

        Request request = new Request(Operation.DELETE_LESSON, lesson.getId());
        try {
            Response response = ClientManager.getClient().sendRequest(request);
            if (response.isSuccess()) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Успех", "Урок удалён");
                Loader.loadScene((Stage) delButton.getScene().getWindow(), StagePath.SCHEDULE);
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Сбой", "Ошибка при отправке запроса: " + e.getMessage());
        }
    }


    @FXML
    void onSaveBtn(ActionEvent event) {
        LessonDTO existing = ClientManager.getSelectedLesson();
        if (teacherField.getText().length() > 12 ||
                lessonTypeField.getText().length() > 12 ||
                timeField.getText().length() > 12 ||
                levelField.getText().length() > 12 ||
                languageField.getText().length() > 12 ||
                dayField.getText().length() > 12) {

            AlertUtil.showAlert(Alert.AlertType.WARNING,
                    "Ошибка валидации",
                    "Каждое поле должно содержать не более 12 символов.");
            return;
        }

        EditLessonRequest2 requestData = new EditLessonRequest2();
        requestData.setTeacher(teacherField.getText());
        requestData.setLessonType(lessonTypeField.getText());
        requestData.setTime(timeField.getText());
        requestData.setLevel(levelField.getText());
        requestData.setLanguage(languageField.getText());
        requestData.setDay(dayField.getText());

        if (existing != null) {
            requestData.setId(existing.getId());
            sendRequest(Operation.UPDATE_LESSON, requestData, "Урок обновлён");
        } else {
            sendRequest(Operation.ADD_LESSON, requestData, "Урок добавлен");
        }
    }

    @FXML
    void onCancelBtn(ActionEvent event) {
        Loader.loadScene((Stage) cancelButton.getScene().getWindow(), StagePath.SCHEDULE);
    }

    private void sendRequest(Operation operation, EditLessonRequest2 requestData, String successMessage) {
        Request request = new Request(operation, requestData);
        sendRequestInternal(request, successMessage);
    }

    private void sendRequestInternal(Request request, String successMessage) {
        try {
            Response response = ClientManager.getClient().sendRequest(request);
            Platform.runLater(() -> {
                if (response.isSuccess()) {
                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Успех", successMessage);
                    Loader.loadScene((Stage) saveButton.getScene().getWindow(), StagePath.SCHEDULE);
                } else {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", response.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Сбой", "Ошибка при отправке запроса: " + e.getMessage());
        }
    }
    private void showMessage(String msg) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }


}
