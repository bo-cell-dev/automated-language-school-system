package com.controller;

import com.dto.LessonDTO;
import com.dto.UserDTO;
import com.enums.StagePath;
import com.network.Client;
import com.network.Request;
import com.network.Response;
import com.network.Operation;
import com.statics.ClientManager;
import com.util.Loader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ListOfpeopleController {

    @FXML
    private TableView<UserDTO> tableView;

    @FXML
    private TableColumn<UserDTO, String> loginColumn;

    @FXML
    private TableColumn<UserDTO, String> surnameColumn;

    @FXML
    private TableColumn<UserDTO, String> nameColumn;

    @FXML
    private TableColumn<UserDTO, String> pColumn;

    @FXML
    private Text backText;

    @FXML
    void initialize() {
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));

        surnameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPerson().getLastName()));
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPerson().getFirstName()));
        pColumn.setCellValueFactory(cellData -> {
            String middleName = "";
            if (cellData.getValue() != null && cellData.getValue().getPerson() != null) {
                middleName = cellData.getValue().getPerson().getMiddleName();
            }
            if (middleName == null || middleName.trim().isEmpty()) {
                middleName = "-";
            }
            return new SimpleStringProperty(middleName);
        });

        loadUsersForLesson();
    }

    private void loadUsersForLesson() {
        try {
            LessonDTO selectedLesson = ClientManager.getSelectedLesson();
            if (selectedLesson == null) return;

            Client client = ClientManager.getClient();
            if (client == null || !client.isConnected()) return;

            Request request = new Request();
            request.setOperation(Operation.GET_USERS_BY_LESSON);
            request.setData(selectedLesson.getId());

            Response response = client.sendRequest(request);

            if (response.isSuccess()) {
                Object data = response.getData();

                System.out.println("Response data class: " + data.getClass());

                if (data instanceof List<?>) {
                    List<?> list = (List<?>) data;
                    System.out.println("List size: " + list.size());
                    for (Object o : list) {
                        System.out.println("Element class: " + o.getClass());
                    }

                    // Проверяем, что все элементы UserDTO
                    boolean allUsers = list.stream().allMatch(o -> o instanceof UserDTO);
                    if (allUsers) {
                        @SuppressWarnings("unchecked")
                        List<UserDTO> userList = (List<UserDTO>) list;
                        ObservableList<UserDTO> observableList = FXCollections.observableArrayList(userList);
                        tableView.setItems(observableList);
                    } else {
                        System.err.println("В списке есть объекты, не являющиеся UserDTO");
                    }
                } else {
                    System.err.println("Данные не являются списком");
                }
            } else {
                System.err.println("Ошибка загрузки пользователей: " + response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }
}
