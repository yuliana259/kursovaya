package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Settings implements Navigatable {
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink organizationSetup;

    @FXML
    private Hyperlink positionSetting;

    @FXML
    private Hyperlink departmentSetting;

    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        organizationSetup.setOnAction(actionEvent -> {
            navigate("/sample/UI/organisation.fxml");
        });
        backButton.setOnAction(actionEvent -> {
            navigate("/sample/UI/main_page.fxml");
        });

    }

    @Override
    public void navigate(String path) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = SceneController.getInstance().getStage();
        stage.setScene(new Scene(root));
    }
}
