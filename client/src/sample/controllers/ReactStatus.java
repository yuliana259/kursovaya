package sample.controllers;

import javafx.scene.control.Alert;

public abstract class ReactStatus {
    public static void react(String status)
    {
        if (status.equals("Success"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Статус операции");
            alert.setHeaderText("Успех");
            alert.setContentText("Операция завершена успешно!");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Статус редактирования");
            alert.setHeaderText("Ошибка");
            alert.setContentText("Ошибка операции");
            alert.showAndWait();
        }
    }
}
