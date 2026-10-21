package com.controller;

import com.dto.LessonDTO;
import com.enums.StagePath;
import com.statics.ClientManager;
import com.util.Loader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ScheduleChartController {

    @FXML
    private Text backText;

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private void initialize() {
        lessons = ClientManager.getLessons(); // забираем из "глобалки"
        fillChart();
    }
    @FXML
    void handleBack(MouseEvent event) {
        Loader.loadScene((Stage) backText.getScene().getWindow(), StagePath.SCHEDULE);
    }

    private ObservableList<LessonDTO> lessons;

    public void setLessons(ObservableList<LessonDTO> lessons) {
        this.lessons = lessons;
        fillChart();
    }

    private void fillChart() {
        if (lessons == null) return;

        Map<String, Integer> counts = new HashMap<>();
        for (LessonDTO lesson : lessons) {
            counts.put(lesson.getDay(), counts.getOrDefault(lesson.getDay(), 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Уроки");

        counts.forEach((day, count) -> {
            series.getData().add(new XYChart.Data<>(day, count));
        });

        barChart.getData().clear();
        barChart.getData().add(series);
    }
}
