package sample.controllers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Organisation implements Navigatable{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    @FXML
    private Button saveButton;

    @FXML
    private Button uploadButton;
    @FXML
    private Button backButton;

    @FXML
    void initialize() {
        saveButton.setOnAction(actionEvent -> {
            if (!nameField.equals("")&&!numberField.equals(""))
            {
                try(FileWriter writer = new FileWriter("organizationData.txt", false))
                {
                    String text = nameField.getText()+","+numberField.getText();
                    writer.write(text);
                    writer.flush();
                    ReactStatus.react("Success");
                }
                catch(IOException ex){
                    ReactStatus.react("Error");
                }
            }
        });
        uploadButton.setOnAction(actionEvent -> {
            try(FileReader reader = new FileReader("organizationData.txt"))
            {
                char[] buf = new char[256];
                int c;
                while((c = reader.read(buf))>0){
                    if(c < 256){
                        buf = Arrays.copyOf(buf, c);
                    }

                }
                String[] value = String.valueOf(buf).split(",");
                nameField.setText(value[0]);
                numberField.setText(value[1]);
                ReactStatus.react("Success");
            }
            catch(IOException ex){
                ReactStatus.react("Error");
            }
        });
        backButton.setOnAction(actionEvent -> {
            navigate("/sample/UI/setting.fxml");
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
