package sample.controllers;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


public class EmployeeManagement implements Navigatable{
    private static BufferedWriter out;
    private static BufferedReader in;
    ObservableList<User> data = FXCollections.observableArrayList();
    public ObservableList<User> selectedUser;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<User, String> surnameColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> patronColumn;

    @FXML
    private TableColumn<User, String> birthColumn;

    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private Button addButton;

    @FXML
    private TextField searchField;

    @FXML
    private RadioButton FioCheckbox;

    @FXML
    private RadioButton posCheckbox;

    @FXML
    private Button chooseBtn;

    @FXML
    private RadioButton alphabetSortChB;

    @FXML
    private RadioButton dateSortChB;

    @FXML
    private Button okSearch;

    @FXML
    private Button okSort;

    @FXML
    private Button BackButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label errorLabel;

    @FXML
    private TableView<User> table = new TableView<User>();
    private final TableView.TableViewSelectionModel<User> selectionModel = table.getSelectionModel();

    @FXML
    void initialize() throws SQLException, IOException {
        SocketHolder socketHolder = SocketHolder.getInstance();
        Socket socket = socketHolder.get();
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        viewEmployees();
        BackButton.setOnAction(actionEvent -> {
            navigate("/sample/UI/main_page.fxml");
        });
        addButton.setOnAction((actionEvent -> {
            errorLabel.setText("");
            navigate("/sample/UI/add_employee.fxml");

        }));
        okSearch.setOnAction(actionEvent -> {
            errorLabel.setText("");
            if (FioCheckbox.isSelected())
            {
                String searchString= searchField.getText();
                ObservableList<User> resultList = FXCollections.observableArrayList();

                if(!searchString.equals(""))
                {
                    ObservableList<User> allElements = table.getItems();
                    int size = allElements.size();
                    for(int i = 0; i < size; i++){
                        if(allElements.get(i).getLastName().contains(searchString))
                            resultList.add(allElements.get(i));
                    }
                    table.setItems(resultList);
                }
                else
                {
                    errorLabel.setText("Строка поиска не может быть пустой!");
                }
            }
            else if (posCheckbox.isSelected())
            {
                String searchString= searchField.getText();
                ObservableList<User> resultList = FXCollections.observableArrayList();

                if(!searchString.equals(""))
                {
                    ObservableList<User> allElements = table.getItems();
                    int size = allElements.size();
                    for(int i = 0; i < size; i++){
                        if(allElements.get(i).getId().contains(searchString))
                            resultList.add(allElements.get(i));
                    }
                    table.setItems(resultList);
                }
                else
                {
                    errorLabel.setText("Строка поиска не может быть пустой!");

                }
            }
            else
            {
                errorLabel.setText("Выберите параметр поиска!");

            }
        });
        okSort.setOnAction(actionEvent -> {
            errorLabel.setText("");
            if(alphabetSortChB.isSelected())
            {
                ObservableList<User> allElements = table.getItems();
                FXCollections.sort(allElements, Comparator.comparing(User::getLastName));
                table.setItems(allElements);
            }
            else if(dateSortChB.isSelected())
            {
                ObservableList<User> allElements = table.getItems();
                FXCollections.sort(allElements, Comparator.comparing(User::getBirthDay));
                table.setItems(allElements);
            }
            else{
                errorLabel.setText("Выберите параметр сортировки!");
            }
        });
        deleteButton.setOnAction(actionEvent -> {
            errorLabel.setText("");
            String status_d="";
            selectedUser=table.getSelectionModel().getSelectedItems();
            try {
                DataHandler.Serialize(selectedUser.get(0));
                out.write("deleteUser\n");
                out.flush();
                String status = in.readLine();
                if (status.equals("Success"))
                {
                    status_d = DataHandler.createDocument("уволить");
                    if(status_d.equals("Success form"))
                        ReactStatus.react(status);
                    else
                        ReactStatus.react("Error");
                }

            } catch (IOException | ClassNotFoundException e) {
                errorLabel.setText("Выберите работника!");
            }
            table.getItems().remove(selectedUser.get(0));

        });
        chooseBtn.setOnAction(actionEvent -> {
            errorLabel.setText("");
            try
            {
                selectedUser=table.getSelectionModel().getSelectedItems();
                UserHolder holder = UserHolder.getInstance();
                User user = new User(selectedUser.get(0).getId(), selectedUser.get(0).getFirstName(),selectedUser.get(0).getLastName(),
                        selectedUser.get(0).getPatronymic(), selectedUser.get(0).getBirthDay());
                holder.set(user);
                navigate("/sample/UI/employee.fxml");
            }
            catch (IndexOutOfBoundsException ex)
            {
                errorLabel.setText("Выберите работника!");
            }
        });
    }
    public void viewEmployees() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        patronColumn.setCellValueFactory(new PropertyValueFactory<User, String>("patronymic"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<User, String>("birthDay"));
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet result = dbhandler.getBasicInfo();
        while(result.next())
        {
            data.add(new User(result.getObject(1).toString(),result.getObject(3).toString(),  result.getObject(2).toString(), result.getObject(4).toString(),
                    result.getObject(5).toString()));
        }
        table.setItems(data);
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
