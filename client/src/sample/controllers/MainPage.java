package sample.controllers;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainPage implements Navigatable {
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink configureSystem;

    @FXML
    private Hyperlink manageEmployees;

    @FXML
    private Hyperlink buildReport;

    @FXML
    private Hyperlink exit;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Label roleLabel;

    @FXML
    void initialize() {
        RoleHolder roleHolder = RoleHolder.getInstance();
        String currentUser = roleHolder.get();
        roleLabel.setText(currentUser);
        if (currentUser.equals("Пользователь"))
        {
            configureSystem.setVisible(false);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        currentDateLabel.setText(formatter.format(date));
        manageEmployees.setOnAction(actionEvent ->
        {
            navigate("/sample/UI/hr_management.fxml");
        });
        buildReport.setOnAction(actionEvent -> {
            navigate("/sample/UI/report.fxml");
        });
        configureSystem.setOnAction(actionEvent ->
        {
            navigate("/sample/UI/setting.fxml");
        });
        exit.setOnAction(actionEvent -> {
            SocketHolder socketHolder = SocketHolder.getInstance();
            Socket socket = socketHolder.get();
            if (socket == null) {
                return;
            }
            try {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                out.write("stop\n"); // отправляем сообщение на сервер
                out.flush();
                SceneController sceneController = SceneController.getInstance();
                sceneController.getStage().close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
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
        SceneController sceneController = SceneController.getInstance();
        Stage stage = sceneController.getStage();
        stage.setScene(new Scene(root));
    }
}
