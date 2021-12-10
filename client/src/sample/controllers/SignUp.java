package sample.controllers;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SignUp{
    private Socket socket = null;
    private String login;
    private String password;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label enterLabel;

    @FXML
    private TextField loginEntry;

    @FXML
    private PasswordField passwordEntry;

    @FXML
    private ImageView appImage;

    @FXML
    private Label appLabel;

    @FXML
    private Button signButton;

    @FXML
    void initialize(){
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 4004);
            SocketHolder socketHolder = SocketHolder.getInstance();
            socketHolder.set(socket);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException x) {
            System.out.println("Input/output error");
            x.printStackTrace();
        }
        signButton.setOnAction(actionEvent -> {
            login=loginEntry.getText().trim();
            password=passwordEntry.getText().trim();
            if (socket == null) {
                return;
            }
            try {
                out.write(login+" "+password + " signUp\n"); // отправляем сообщение на сервер
                out.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    RoleHolder roleHolder = RoleHolder.getInstance();
                    roleHolder.set("Администратор");
                    signButton.getScene().getWindow().hide();
                    navigate("/sample/UI/main_page.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void navigate(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/UI/main_page.fxml"));
        loader.load();
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        SceneController window = SceneController.getInstance();
        window.setStage(stage);
        window.setScene(scene);
        stage.setScene(scene);
        stage.showAndWait();
    }
}

