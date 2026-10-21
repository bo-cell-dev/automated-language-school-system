package com.enums;

import lombok.Getter;

@Getter
public enum StagePath {
    ENTRANCE("/fxml/entrance.fxml"),
    CHANGE("/fxml/change.fxml"),
    LESSON_ACCESS("/fxml/lessonAccess.fxml"),
    LIST("/fxml/list.fxml"),
    LOGIN("/fxml/login.fxml"),
    MANAGE("/fxml/manage.fxml"),
    REGISTER("/fxml/register.fxml"),
    SCHEDULE("/fxml/schedule.fxml"),
    LIST_OF_PEOPLE("/fxml/listOfpeople.fxml"),
    GRAPH("/fxml/ScheduleChart.fxml"),
    PROFILE ("/fxml/profile.fxml");

    private final String pathToFxml;

    StagePath(String pathToFxml) {
        this.pathToFxml = pathToFxml;
    }
}
