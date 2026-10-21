package com.controller;

import com.enums.StagePath;
import com.network.Operation;
import com.network.Request;
import com.network.Response;
import com.dto.UserConnectionDTO;
import com.statics.ClientManager;
import com.util.AlertUtil;
import com.util.Loader;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class ConnectedListController {
    @FXML
    private Text backText;

    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }

    @FXML
    private TableView<UserConnectionDTO> tableView;
    @FXML
    private TableColumn<UserConnectionDTO, String> loginColumn;
    @FXML
    private TableColumn<UserConnectionDTO, String> statusColumn;

    @FXML
    public void initialize() {
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadConnectedUsers();
    }

    private void loadConnectedUsers() {
        Request request = new Request(Operation.GET_CONNECTED_USERS, (Object) null);

        try {
            Response response = ClientManager.getClient().sendRequest(request);
            if (response.isSuccess()) {
                List<UserConnectionDTO> users = (List<UserConnectionDTO>) response.getData();
                tableView.getItems().setAll(users);
            } else {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Ошибка", response.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось получить список подключённых");
        }
    }

}
