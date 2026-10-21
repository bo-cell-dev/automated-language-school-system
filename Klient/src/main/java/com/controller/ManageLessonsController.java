package com.controller;

import com.dto.LessonDTO;
import com.enums.StagePath;
import com.network.Client;
import com.network.Request;
import com.network.Response;
import com.statics.ClientManager;
import com.util.Loader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import com.network.Operation;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ManageLessonsController {
    @FXML
    private Text backText;

  @FXML
    private Button addLesBtn;

    @FXML
    private TableView<LessonDTO> lessonTable;

    @FXML
    private TableColumn<LessonDTO, String> dateColumn;

    @FXML
    private TableColumn<LessonDTO, String> timeColumn;

    @FXML
    private TableColumn<LessonDTO, String> typeColumn;

    @FXML
    private TableColumn<LessonDTO, String> teacherColumn;

    @FXML
    private TableColumn<LessonDTO, String> languageColumn;

    @FXML
    private TableColumn<LessonDTO, Void> editColumn;

    @FXML
    private TableColumn<LessonDTO, Integer> recordColumn;

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }

    @FXML
    void onPicLesBtn(ActionEvent event) {
        ClientManager.setLessons(lessonTable.getItems());
        Loader.loadScene((Stage) lessonTable.getScene().getWindow(), StagePath.GRAPH);
    }
    @FXML
    void initialize() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        languageColumn.setCellValueFactory(new PropertyValueFactory<>("language"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        addEditButtonToTable();
        addRecordButtonToTable();
        loadLessons();
    }
    private void addEditButtonToTable() {
        editColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Редактировать");

            {
                btn.setOnAction(event -> {
                    LessonDTO lesson = getTableView().getItems().get(getIndex());
                    openEditScene(lesson);
                });
                btn.getStyleClass().add("custom-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void addRecordButtonToTable() {
        recordColumn.setCellFactory(param -> new TableCell<>() {
            private final Button listBtn = new Button("Список");

            {
                listBtn.setOnAction(event -> {
                    LessonDTO lesson = getTableView().getItems().get(getIndex());
                    openPeopleListScene(lesson);
                });
                listBtn.getStyleClass().add("custom-button");
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(listBtn);
                }
            }
        });
    }
    private void openPeopleListScene(LessonDTO lesson) {
        ClientManager.setSelectedLesson(lesson);
        Loader.loadScene((Stage) lessonTable.getScene().getWindow(), StagePath.LIST_OF_PEOPLE);
    }


    private void loadLessons() {
        try {
            Client client = ClientManager.getClient();
            if (client == null || !client.isConnected()) {
                System.err.println("Нет подключения к серверу");
                return;
            }

            Request request = new Request();
            request.setOperation(Operation.GET_LESSONS);

            Response response = client.sendRequest(request);

            if (response.isSuccess()) {
                List<LessonDTO> lessonList = (List<LessonDTO>) response.getData();
                ObservableList<LessonDTO> lessons = FXCollections.observableArrayList(lessonList);
                lessonTable.setItems(lessons);
            } else {
                System.err.println("Ошибка загрузки уроков: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onAddLesBtn(ActionEvent event) {
        ClientManager.setSelectedLesson(null);
        Loader.loadScene((Stage) addLesBtn.getScene().getWindow(), StagePath.CHANGE);
    }


    private void openEditScene(LessonDTO lesson) {
        ClientManager.setSelectedLesson(lesson);
        Loader.loadScene((Stage) lessonTable.getScene().getWindow(), StagePath.CHANGE);
    }

}



