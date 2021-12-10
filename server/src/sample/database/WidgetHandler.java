package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WidgetHandler {
    public static ObservableList getNationality() throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet resultSet = dbhandler.getNationality();
        List<String> list = new ArrayList<String>();
        ObservableList<String> location = FXCollections.observableList(list);
        while (resultSet.next())
        {
            location.add(resultSet.getString("Гражданство"));
        }
        return location;
    }
    public static ObservableList getFamilyStatus() throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet resultSet = dbhandler.getFamily();
        List<String> list = new ArrayList<String>();
        ObservableList<String> family = FXCollections.observableList(list);
        while (resultSet.next())
        {
            family.add(resultSet.getString("СемейныйСтатус")+" "+resultSet.getString("КоличествоДетей")+" детей");
        }
        return family;
    }
    public static ObservableList getSubcategory() throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet resultSet = dbhandler.getDepartment();
        List<String> list = new ArrayList<String>();
        ObservableList<String> category = FXCollections.observableList(list);
        while (resultSet.next())
        {
            category.add(resultSet.getString("Подразделение"));
        }
        return category;
    }
    public static ObservableList getPositions() throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        ResultSet resultSet = dbhandler.getPosition();
        List<String> list = new ArrayList<String>();
        ObservableList<String> position = FXCollections.observableList(list);
        while (resultSet.next())
        {
            position.add(resultSet.getString("Должность"));
        }
        return position;
    }
}
