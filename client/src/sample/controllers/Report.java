package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Report implements Navigatable {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<Number, Number> reportView;

    @FXML
    private DatePicker date;

    @FXML
    private Button formButton;

    @FXML
    private Button backButton;
    @FXML
    private Label errorLabel;

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
    @FXML
    void initialize() {
       backButton.setOnAction(actionEvent -> {
           navigate("/sample/UI/main_page.fxml");
       });
       formButton.setOnAction(actionEvent -> {
           try {
               getReport();
           } catch (NullPointerException | SQLException throwables) {
               errorLabel.setText("Выберите дату для отчета!");

           }
       });

    }
    private void getReport() throws SQLException {
        int peopleAmount = 0;
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Месяц");
        yAxis.setLabel("Количество принятых");
        String year = date.getValue().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        String[] yearArray = year.split(" ");
        reportView = new LineChart<Number, Number>(xAxis,yAxis);
        XYChart.Series acceptSeries = new XYChart.Series();
        XYChart.Series firedSeries = new XYChart.Series();
        System.out.println(year);
        System.out.println(yearArray);
        firedSeries.setName("Уволенные за "+ yearArray[2]);
        acceptSeries.setName("Принятые за "+ yearArray[2]);
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet accepted = dbhandler.getAcceptedPeople();
        ResultSet fired = dbhandler.getFiredPeople();
        Map<Integer, Integer> acceptedDict = new HashMap<Integer, Integer>();
        Map<Integer, Integer> firedDict = new HashMap<Integer, Integer>();
        String accessDate = "";
        while(accepted.next())
        {
            peopleAmount=0;
            accessDate = accepted.getString("ДатаПриема");
            int month = Integer.parseInt(accessDate.substring(5,7));
            if(accessDate.substring(0,4).equals(yearArray[2]))
            {
                if(!acceptedDict.containsKey(month))
                {
                    peopleAmount++;
                    acceptedDict.put(month, peopleAmount);
                }
                else
                {
                    acceptedDict.put(month, acceptedDict.get(month)+1);
                }
            }
        }
        while(fired.next())
        {
            peopleAmount=0;
            accessDate = fired.getString("ДатаУвольнения");
            int month = Integer.parseInt(accessDate.substring(5,7));
            if(accessDate.substring(0,4).equals(yearArray[2]))
            {
                if(!firedDict.containsKey(month))
                {
                    peopleAmount++;
                    firedDict.put(month, peopleAmount);
                }
                else
                {
                    firedDict.put(month, firedDict.get(month)+1);
                }
            }
        }
        for (int i =1; i<13; i++)
        {
            if(!acceptedDict.containsKey(i))
            {
                acceptedDict.put(i, 0);
            }
            if(!firedDict.containsKey(i))
            {
                firedDict.put(i, 0);
            }
        }
        acceptSeries.getData().add(new XYChart.Data(1, acceptedDict.get(1)));
        acceptSeries.getData().add(new XYChart.Data(2,  acceptedDict.get(2)));
        acceptSeries.getData().add(new XYChart.Data(3,  acceptedDict.get(3)));
        acceptSeries.getData().add(new XYChart.Data(4,  acceptedDict.get(4)));
        acceptSeries.getData().add(new XYChart.Data(5, acceptedDict.get(5)));
        acceptSeries.getData().add(new XYChart.Data(6,  acceptedDict.get(6)));
        acceptSeries.getData().add(new XYChart.Data(7,  acceptedDict.get(7)));
        acceptSeries.getData().add(new XYChart.Data(8,  acceptedDict.get(8)));
        acceptSeries.getData().add(new XYChart.Data(9,  acceptedDict.get(9)));
        acceptSeries.getData().add(new XYChart.Data(10,  acceptedDict.get(10)));
        acceptSeries.getData().add(new XYChart.Data(11,  acceptedDict.get(11)));
        acceptSeries.getData().add(new XYChart.Data(12,  acceptedDict.get(12)));

        firedSeries.getData().add(new XYChart.Data(1, firedDict.get(1)));
        firedSeries.getData().add(new XYChart.Data(2,  firedDict.get(2)));
        firedSeries.getData().add(new XYChart.Data(3,  firedDict.get(3)));
        firedSeries.getData().add(new XYChart.Data(4,  firedDict.get(4)));
        firedSeries.getData().add(new XYChart.Data(5, firedDict.get(5)));
        firedSeries.getData().add(new XYChart.Data(6,  firedDict.get(6)));
        firedSeries.getData().add(new XYChart.Data(7,  firedDict.get(7)));
        firedSeries.getData().add(new XYChart.Data(8,  firedDict.get(8)));
        firedSeries.getData().add(new XYChart.Data(9,  firedDict.get(9)));
        firedSeries.getData().add(new XYChart.Data(10,  firedDict.get(10)));
        firedSeries.getData().add(new XYChart.Data(11,  firedDict.get(11)));
        firedSeries.getData().add(new XYChart.Data(12,  firedDict.get(12)));
        Stage stage = new Stage();
        Scene scene  = new Scene(reportView,800,600);
        reportView.getData().add(acceptSeries);
        reportView.getData().add(firedSeries);

        stage.setScene(scene);
        stage.show();


    }
    private void getFiredAmount()
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("My portfolio");
        //populating the series with data
        series.getData().add(new XYChart.Data(1, 23));
        series.getData().add(new XYChart.Data(2, 14));
        series.getData().add(new XYChart.Data(3, 15));
        series.getData().add(new XYChart.Data(4, 24));
        series.getData().add(new XYChart.Data(5, 34));
        series.getData().add(new XYChart.Data(6, 36));
        series.getData().add(new XYChart.Data(7, 22));
        series.getData().add(new XYChart.Data(8, 45));
        series.getData().add(new XYChart.Data(9, 43));
        series.getData().add(new XYChart.Data(10, 17));
        series.getData().add(new XYChart.Data(11, 29));
        series.getData().add(new XYChart.Data(12, 25));
        Stage stage = new Stage();
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }
}
