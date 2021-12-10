package sample.controllers;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.geometry.Insets;

public class Employee extends ReactStatus implements Navigatable{

    private static BufferedWriter out;
    private static BufferedReader in;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField patronField;

    @FXML
    private ImageView photo;

    @FXML
    private TextField idField;

    @FXML
    private ChoiceBox<String> locationField;

    @FXML
    private ChoiceBox<String> familyField;

    @FXML
    private ChoiceBox<String> subCategory;

    @FXML
    private ChoiceBox<String> position;

    @FXML
    private RadioButton femChB;

    @FXML
    private RadioButton menChB;

    @FXML
    private DatePicker birthDay;

    @FXML
    private TextField insurance;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private Button AddButton;

    @FXML
    private Button backButton;
    @FXML
    private Label windowAddLabel;
    @FXML
    private TextField idLabel;

    @FXML
    private Button editProfile;

    @FXML
    private Button deleteProfile;

    @FXML
    private Label birthLabel;

    @FXML
    private Label accessLabel;

    @FXML
    private Label endLabel;

    @FXML
    private RadioButton createSickLeave;

    @FXML
    private RadioButton createVacation;

    @FXML
    private RadioButton createBusinessTrip;

    @FXML
    private Button createDoc;

    @FXML
    private Label nameLabel;

    @FXML
    private Label nationalityLabel;

    @FXML
    private Label familyLabel;

    @FXML
    private Label insuranceLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Label positionLabel;


    @FXML
    void initialize() throws SQLException, IOException {
        SocketHolder socketHolder = SocketHolder.getInstance();
        Socket socket = socketHolder.get();
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        backButton.setOnAction(actionEvent -> {
            navigate("/sample/UI/hr_management.fxml");
        });
        if (location.getFile().contains("add_employee.fxml")) {
            familyField.getItems().addAll(WidgetHandler.getFamilyStatus());
            locationField.getItems().addAll(WidgetHandler.getNationality());
            subCategory.getItems().addAll(WidgetHandler.getSubcategory());
            position.getItems().addAll(WidgetHandler.getPositions());

            AddButton.setOnAction(actionEvent ->
            {
                try {
                    User user = takeData();
                    DataHandler.Serialize(user);
                    out.write("addUser\n"); // отправляем сообщение на сервер
                    out.flush();
                    String status = in.readLine();
                    String status_d = "";
                    if (status.equals("Success"))
                    {
                        status_d = DataHandler.createDocument("принять");
                        if(status_d.equals("Success form"))
                            ReactStatus.react(status);
                        else
                            ReactStatus.react("Error");
                    }

                } catch (IOException | IndexOutOfBoundsException | NullPointerException | ClassNotFoundException e) {
                    ReactStatus.react("Error");
                }
            });

        }
        else if (location.getFile().contains("employee.fxml"))
        {
            UserHolder holder = UserHolder.getInstance();
            User selectedUser = holder.get();
            DatabaseHandler dbhandler = new DatabaseHandler();
            ResultSet resultSet = dbhandler.getEmployee(selectedUser.getId());
            while(resultSet.next())
            {
                idLabel.setText("Табельный номер "+resultSet.getString("Табельный_номер"));
                nameLabel.setText(resultSet.getString("Фамилия")+" "+resultSet.getString("Имя")+" "+resultSet.getString("Отчество"));
                birthLabel.setText(resultSet.getString("ДатаРождения"));
                nationalityLabel.setText(resultSet.getString("Гражданство"));
                familyLabel.setText(resultSet.getString("СемейныйСтатус")+" "+resultSet.getString("КоличествоДетей")+" детей");
                insuranceLabel.setText(resultSet.getString("НомерСоциальногоСтрахования"));
                genderLabel.setText(resultSet.getString("Пол"));
                departmentLabel.setText(resultSet.getString("Подразделение"));
                positionLabel.setText(resultSet.getString("Должность"));
                accessLabel.setText(resultSet.getString("ДатаПриема"));
                endLabel.setText(resultSet.getString("ДатаУвольнения"));
            }
            editProfile.setOnAction(actionEvent -> {
                User selected_user=getData();
                UserHolder user = UserHolder.getInstance();
                user.set(selected_user);
                navigate("/sample/UI/edit_employee.fxml");
            });
        }


    }
    private User takeData()
    {
        User user = new User();
        user.setFirstName(nameField.getText());
        user.setLastName(surnameField.getText());
        user.setPatronymic(patronField.getText());
        user.setBirthDay(birthDay.getValue().toString());
        user.setAcceptDay(startDate.getValue().toString());
        user.setFireDay(endDate.getValue().toString());
        user.setLocation(locationField.getValue());
        user.setFamily(familyField.getValue());
        user.setDepartment(subCategory.getValue());
        user.setPosition(position.getValue());
        user.setInsuranceNumb(insurance.getText());
        if (femChB.isSelected())
            user.setGender(femChB.getText());
        else if (menChB.isSelected())
            user.setGender(menChB.getText());

        if (position.getValue().equals("Специалист по кадрам")) {
            String[] array = registerUser().split(" ");
            user.setRole("Пользователь");
            user.setUserName(array[0]);
            user.setPassword(array[1]);
        }
            return user;
    }
    private String registerUser() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Регистрация пользователя");
        dialog.setHeaderText("Для добавления в систему специалиста по кадрам необходимо его зарегистрировать");
        ButtonType loginButtonType = new ButtonType("Зарегистрировать", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");

        grid.add(new Label("Логин:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль:"), 0, 1);
        grid.add(password, 1, 1);
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> username.requestFocus());
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
        Optional<Pair<String, String>> result = dialog.showAndWait();
        if(result.isPresent())
            return username.getText()+" "+password.getText();
        else
        {
            return "user 1111";
        }
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

    private User getData()
    {
        User selected_user = new User();
        selected_user.setId(idLabel.getText());
        String[] name = nameLabel.getText().split(" ");
        selected_user.setFirstName(name[1]);
        selected_user.setLastName(name[0]);
        selected_user.setPatronymic(name[2]);
        selected_user.setGender(genderLabel.getText());
        selected_user.setBirthDay(birthLabel.getText());
        selected_user.setAcceptDay(accessLabel.getText());
        selected_user.setFireDay(endLabel.getText());
        selected_user.setLocation(nationalityLabel.getText());
        selected_user.setFamily(familyLabel.getText());
        selected_user.setDepartment(departmentLabel.getText());
        selected_user.setPosition(positionLabel.getText());
        selected_user.setInsuranceNumb(insuranceLabel.getText());
        return selected_user;
    }
}
