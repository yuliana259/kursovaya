package sample.controllers;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client  {
    private Socket socket = null;
    private String login;
    private String password;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;
    String status;
    String role;

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
    private Button submitButton;

    @FXML
    private Button signupButton;

    @FXML
    private Button signButton;

    @FXML
    private Label appLabel;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() throws IOException {
        boolean serverLaunched = false;
        while (!serverLaunched)
        {
            try {
                socket = new Socket(InetAddress.getByName("127.0.0.1"), 4004);
                serverLaunched=true;

            } catch (ConnectException x) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Статус подключения");
                alert.setHeaderText("Ошибка");
                alert.setContentText("Сервер не запущен!");
                alert.showAndWait();
                continue;
            }
        }

        SocketHolder socketHolder = SocketHolder.getInstance();
        socketHolder.set(socket);
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        submitButton.setOnAction(actionEvent -> {
            login=loginEntry.getText().trim();
            password=passwordEntry.getText().trim();
            if (socket == null) {
                return;
            }
            try {
                out.write(login+" "+password + " login\n"); // отправляем сообщение на сервер
                out.flush();
                String full_result = in.readLine();
                String[] result = full_result.split(" ");
                System.out.println(full_result);
                String status = result[0];
                String role = result[1];
                if (status.equals("Success"))
                {
                    RoleHolder roleHolder = RoleHolder.getInstance();
                    roleHolder.set(role);
                    submitButton.getScene().getWindow().hide();
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
                else
                {
                    errorLabel.setText("Неверный логин или пароль, повторите попытку");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        signupButton.setOnAction(actionEvent -> {
            signupButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/UI/signup.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        });


    }

}

