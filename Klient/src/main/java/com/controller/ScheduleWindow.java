package com.controller;

import com.dto.LessonDTO;
import com.dto.ScheduleResponseDTO;
import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.network.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScheduleWindow {

    private Client client;

    @FXML
    private Button listConnectBtn;

    @FXML
    private Button listClassesBtn;

    @FXML
    private Button profileBtn;

    @FXML
    private Text backText;
    @FXML
    private GridPane scheduleGrid;

    @FXML
    public void initialize() {
        if (ClientManager.getClient() != null) {
            loadSchedule();
            updateUIByRole();
        } else {
            System.out.println("Клиент не установлен");
        }
    }


    private void loadSchedule() {
        try {
            Request request = new Request(Operation.GET_SCHEDULE);
            Response response = ClientManager.getClient().sendRequest(request);

            List<String> days = null;
            List<String> times = null;
            List<LessonDTO> lessons = null;

            if (response.isSuccess()) {
                ScheduleResponseDTO schedule = (ScheduleResponseDTO) response.getData();
                days = schedule.getDays();
                times = schedule.getTimes();
                lessons = schedule.getLessons();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка загрузки", response.getMessage());
                return;
            }

            scheduleGrid.getChildren().clear();
            scheduleGrid.getColumnConstraints().clear();
            scheduleGrid.getRowConstraints().clear();

            for (int col = 0; col < days.size(); col++) {
                Label dayLabel = new Label(days.get(col));
                dayLabel.getStyleClass().add("subtitle");
                scheduleGrid.add(dayLabel, col + 1, 0);
            }

            for (int row = 0; row < times.size(); row++) {
                Label timeLabel = new Label(times.get(row));
                timeLabel.getStyleClass().add("subtitle");
                scheduleGrid.add(timeLabel, 0, row + 1);
            }

            for (LessonDTO lesson : lessons) {
                int col = days.indexOf(lesson.getDay()) + 1;
                int row = times.indexOf(lesson.getTime()) + 1;

                if (col > 0 && row > 0) {
                    boolean allowInteraction = ClientManager.isUser();

                    Button lessonBtn = new Button(lesson.getLanguage() + "\n" + lesson.getTeacher());
                    lessonBtn.setWrapText(true);
                    lessonBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 10;");

                    if (allowInteraction) {
                        lessonBtn.setOnAction(e -> {
                            ClientManager.setSelectedLesson(lesson);
                            Loader.loadScene((Stage) lessonBtn.getScene().getWindow(), StagePath.LESSON_ACCESS);
                        });
                    } else {
                        lessonBtn.setDisable(true);
                    }

                    scheduleGrid.add(lessonBtn, col, row);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка загрузки", "Не удалось загрузить расписание: " + e.getMessage());
        }
    }


    public void updateUIByRole() {
        if (ClientManager.isGuest()) {
            profileBtn.setDisable(true);
            profileBtn.setVisible(false);
            listConnectBtn.setDisable(true);
            listConnectBtn.setVisible(false);
            listClassesBtn.setDisable(true);
            listClassesBtn.setVisible(false);
        } else if (ClientManager.isUser()) {
            profileBtn.setDisable(false);
            profileBtn.setVisible(true);
            listConnectBtn.setDisable(true);
            listConnectBtn.setVisible(false);
            listClassesBtn.setDisable(true);
            listClassesBtn.setVisible(false);
        } else {
            profileBtn.setDisable(false);
            profileBtn.setVisible(true);
            listConnectBtn.setDisable(false);
            listConnectBtn.setVisible(true);
            listClassesBtn.setDisable(false);
            listClassesBtn.setVisible(true);
        }
    }

    @FXML
    void handleBack(MouseEvent event) {
        client = ClientManager.getClient();
        if (client != null) {
            client.disconnect();
        }
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.ENTRANCE);
    }

    @FXML
    void onListConnectBtn(ActionEvent event) {
        Loader.loadScene((Stage) listConnectBtn.getScene().getWindow(), StagePath.LIST);
    }
    @FXML
    void onListClassesBtn(ActionEvent event) {
        Loader.loadScene((Stage) listClassesBtn.getScene().getWindow(), StagePath.MANAGE);
    }

    @FXML
    void onProfileBtn(ActionEvent event) {
        Loader.loadScene((Stage) profileBtn.getScene().getWindow(), StagePath.PROFILE);
    }


}
