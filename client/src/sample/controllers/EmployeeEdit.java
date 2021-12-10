package sample.controllers;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EmployeeEdit extends ReactStatus implements Navigatable {
    private Socket socket = null;
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out;
    @FXML
    private TextField surnameEditLabel;

    @FXML
    private TextField nameEditLabel;

    @FXML
    private TextField patronEditLabel;

    @FXML
    private TextField idEditField;

    @FXML
    private ChoiceBox<String> locationEditField;

    @FXML
    private ChoiceBox<String> familyEditField;

    @FXML
    private ChoiceBox<String> subCategoryEdit;

    @FXML
    private ChoiceBox<String> positionEdit;

    @FXML
    private RadioButton femChBEdit;

    @FXML
    private RadioButton menChBEdit;

    @FXML
    private TextField birthdayEdit;

    @FXML
    private TextField accessEdit;

    @FXML
    private TextField fireEdit;

    @FXML
    private TextField insuranceEdit;

    @FXML
    private DatePicker startDateEdit;

    @FXML
    private DatePicker endDateEdit;

    @FXML
    private Button saveButtonEdit;

    @FXML
    private Button backButtonEdit;

    @FXML
    private Label errorLabel;

    @FXML
    void initialize() throws IOException, SQLException {
        SocketHolder socketHolder = SocketHolder.getInstance();
        socket = socketHolder.get();
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        setData();
        saveButtonEdit.setOnAction(actionEvent -> {
            User edited_user = getData();
            try {
                DataHandler.Serialize(edited_user);
                out.write("edit\n");
                out.flush();
                String status = in.readLine();
                ReactStatus.react(status);
            } catch (IOException e) {
                e.printStackTrace();
            }
            navigate("/sample/UI/hr_management.fxml");
        });
        backButtonEdit.setOnAction(actionEvent -> {
           navigate("/sample/UI/hr_management.fxml");
        });
    }
    @Override
    public void navigate(String path)
    {
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
        String[] id = idEditField.getText().split(" ");
        User user = new User();
        user.setId(id[2]);
        user.setFirstName(nameEditLabel.getText());
        user.setLastName(surnameEditLabel.getText());
        user.setPatronymic(patronEditLabel.getText());
        user.setBirthDay(birthdayEdit.getText());
        user.setAcceptDay(accessEdit.getText());
        user.setFireDay(fireEdit.getText());
        user.setLocation(locationEditField.getValue());
        user.setFamily(familyEditField.getValue());
        user.setDepartment(subCategoryEdit.getValue());
        user.setPosition(positionEdit.getValue());
        user.setInsuranceNumb(insuranceEdit.getText());
        if(menChBEdit.isSelected())
            user.setGender(menChBEdit.getText());
        else if(femChBEdit.isSelected())
            user.setGender(femChBEdit.getText());
        return user;
    }
    private void setData() throws SQLException {

        familyEditField.getItems().addAll(WidgetHandler.getFamilyStatus());
        locationEditField.getItems().addAll(WidgetHandler.getNationality());
        subCategoryEdit.getItems().addAll(WidgetHandler.getSubcategory());
        positionEdit.getItems().addAll(WidgetHandler.getPositions());

        UserHolder userHolder = UserHolder.getInstance();
        User user = userHolder.get();
        idEditField.setText(user.getId());
        nameEditLabel.setText(user.getFirstName());
        surnameEditLabel.setText(user.getLastName());
        patronEditLabel.setText(user.getPatronymic());
        birthdayEdit.setText(user.getBirthDay().substring(0,10));
        accessEdit.setText(user.getAcceptDay().substring(0,10));
        fireEdit.setText(user.getFireDay().substring(0,10));
        locationEditField.setValue(user.getLocation());
        familyEditField.setValue(user.getFamily());
        subCategoryEdit.setValue(user.getDepartment());
        positionEdit.setValue(user.getPosition());
        insuranceEdit.setText(user.getInsuranceNumb());
    }
}
