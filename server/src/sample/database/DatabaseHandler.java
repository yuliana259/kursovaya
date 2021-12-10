package sample.database;
import java.io.*;
import java.sql.*;

import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseHandler extends Configs {
    Connection dbConnection;
    public Connection getDbConnection() throws ClassNotFoundException, SQLException
    {
        String connectionString = "jdbc:mysql://"+ dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public ResultSet getUser(User manager)
    {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Const.USER_TABLE+" WHERE "+ Const.USERS_NAME + "=? AND "+    Const.USERS_PASS + "=?";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, manager.getUserName());
            preparedStatement.setString(2, manager.getPassword());
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void setUser(User director)
    {
        String set = "INSERT INTO "+Const.EMPLOYEE_TABLE+ "("+Const.USERS_ROLE+","+ Const.USERS_NAME+","+ Const.USERS_PASS+")"+
        "VALUES (?,?,?)";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(set);
            System.out.println(director.getRole()+director.getUserName()+director.getPassword());
            preparedStatement.setString(1, director.getRole());
            preparedStatement.setString(2, director.getUserName());
            preparedStatement.setString(3, director.getPassword());
            preparedStatement.executeUpdate();
            System.out.println("Server has added a user");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public String setEmployee() throws IOException, ClassNotFoundException, SQLException {
        User user = (User) DataHandler.Deserialize();
        ResultSet resultSet = null;
        String set = " ";
        String[] family_array = user.getFamily().split(" ");
        String location_select = "(SELECT " + Const.NATIONALITY_CODE + " FROM " + Const.NATIONALITY_TABLE + " WHERE "
                + Const.NATIONALITY + "='" + user.getLocation() + "')";
        String position_select = "(SELECT " + Const.POSITION_CODE + " FROM " + Const.POSITION_TABLE + " WHERE "
                + Const.POSITION + "='" + user.getPosition() + "')";
        String family_select = "(SELECT " + Const.FAMILY_CODE + " FROM " + Const.FAMILY_TABLE + " WHERE "
                + Const.FAMILY_STATUS + "='" + family_array[0] + " " + family_array[1] + "' AND " + Const.FAMILY_HAS_CHILDREN + " ='" + family_array[2] + "')";
        String subcategory_select = "(SELECT " + Const.SUBCATEGORY_CODE + " FROM " + Const.SUBCATEGORY_TABLE + " WHERE "
                + Const.SUBCATEGORY + "='" + user.getDepartment() + "')";
        try {
            if (user.getRole().equals("Пользователь")) {
                set = "INSERT INTO " + Const.EMPLOYEE_TABLE +
                        "(`Фамилия`,\n" +
                        "`Имя`,\n" +
                        "`Отчество`,\n" +
                        "`КодГражданства`,\n" +
                        "`КодДолжности`,\n" +
                        "`КодСемейногоПоложения`,\n" +
                        "`КодПодразделения`,\n" +
                        "`ДатаПриема`,\n" +
                        "`ДатаУвольнения`,\n" +
                        "`Пол`,\n" +
                        "`ДатаРождения`,\n" +
                        "`НомерСоциальногоСтрахования`, `Роль`, `ИмяПользователя`, `Пароль`) VALUES (?,?,?, " + location_select + "," + position_select + "," + family_select + "," + subcategory_select + ",?,?,?,?,?, '"
                        + user.getRole() + "','" + user.getUserName() + "','" + user.getPassword() + "')";
            }
        } catch (NullPointerException exception) {
            set = "INSERT INTO " + Const.EMPLOYEE_TABLE +
                    "(`Фамилия`,\n" +
                    "`Имя`,\n" +
                    "`Отчество`,\n" +
                    "`КодГражданства`,\n" +
                    "`КодДолжности`,\n" +
                    "`КодСемейногоПоложения`,\n" +
                    "`КодПодразделения`,\n" +
                    "`ДатаПриема`,\n" +
                    "`ДатаУвольнения`,\n" +
                    "`Пол`,\n" +
                    "`ДатаРождения`,\n" +
                    "`НомерСоциальногоСтрахования`) VALUES (?,?,?, " + location_select + "," + position_select + "," + family_select + "," + subcategory_select + ",?,?,?,?,?)";
        } finally {
            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(set);
                preparedStatement.setString(2, user.getFirstName());
                preparedStatement.setString(1, user.getLastName());
                preparedStatement.setString(3, user.getPatronymic());
                preparedStatement.setString(4, user.getAcceptDay().toString());
                preparedStatement.setString(5, user.getFireDay().toString());
                preparedStatement.setString(6, user.getGender());
                preparedStatement.setString(7, user.getBirthDay().toString());
                preparedStatement.setString(8, user.getInsuranceNumb());
                preparedStatement.executeUpdate();
                return "Success";
            } catch (SQLException | ClassNotFoundException e) {
                return "Error";

            }
        }
    }

    public ResultSet getEmployee(String id)
    {
        ResultSet resultSet = null;
        String select = "SELECT "+ Const.USERS_ID+","+Const.EMPLOYEE_NAME + ","+Const.EMPLOYEE_SURNAME+","+
                Const.EMPLOYEE_PATRON+","+Const.EMPLOYEE_BIRTHDAY+","+Const.NATIONALITY_TABLE+"."+Const.NATIONALITY+
                ","+Const.FAMILY_TABLE+"."+Const.FAMILY_STATUS+","+Const.FAMILY_TABLE+"."+Const.FAMILY_HAS_CHILDREN+
                ","+Const.EMPLOYEE_INSURANCE+","+Const.EMPLOYEE_GENDER+","+Const.SUBCATEGORY_TABLE+"."+Const.SUBCATEGORY+","+
                Const.POSITION_TABLE+"."+Const.POSITION+","+Const.EMPLOYEE_ACCEPT_DATE+","+Const.EMPLOYEE_FIRE_DATE+
                " FROM " + Const.EMPLOYEE_TABLE + ","+Const.NATIONALITY_TABLE+","+Const.FAMILY_TABLE+","+
                Const.SUBCATEGORY_TABLE+","+Const.POSITION_TABLE +
                " WHERE "+ Const.USERS_ID + "=?"+
                " AND "+Const.NATIONALITY_TABLE+"."+Const.NATIONALITY_CODE+"="+Const.EMPLOYEE_TABLE+"."+Const.EMPLOYEE_LOCATION_CODE+ " AND " +
                Const.FAMILY_TABLE+"."+Const.FAMILY_CODE+"="+Const.EMPLOYEE_TABLE+"."+Const.EMPLOYEE_FAMILY_CODE +
                " AND "+Const.SUBCATEGORY_TABLE+"."+Const.SUBCATEGORY_CODE+"="+Const.EMPLOYEE_TABLE+"."+Const.EMPLOYEE_SUBCATEGORY_CODE +
                " AND "+Const.POSITION_TABLE+"."+Const.POSITION_CODE+"="+Const.EMPLOYEE_TABLE+"."+Const.EMPLOYEE_POSITION_CODE;
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public String updateEmployee(User user)
    {
        String[] family_array = user.getFamily().split(" ");
        String update= "UPDATE "+Const.EMPLOYEE_TABLE+" SET "+Const.EMPLOYEE_NAME+"='"+user.getFirstName()+"', "+
                Const.EMPLOYEE_SURNAME+"='"+user.getLastName()+"', "+
                Const.EMPLOYEE_PATRON+"='"+user.getPatronymic()+"', "+
                Const.EMPLOYEE_BIRTHDAY+"='"+user.getBirthDay()+"', "+
                Const.EMPLOYEE_LOCATION_CODE+"=(SELECT "+Const.NATIONALITY_CODE+" FROM "+ Const.NATIONALITY_TABLE + " WHERE "+Const.NATIONALITY+"='"+user.getLocation()+"'), "+
                Const.EMPLOYEE_FAMILY_CODE+"=(SELECT "+Const.FAMILY_CODE+" FROM "+ Const.FAMILY_TABLE + " WHERE "+Const.FAMILY_STATUS+"='"+family_array[0]+" "+family_array[1]+"'" +
                " AND "+Const.FAMILY_HAS_CHILDREN+"="+family_array[2]+"), "+ Const.EMPLOYEE_INSURANCE+"='"+user.getInsuranceNumb()+"', "+
                Const.EMPLOYEE_GENDER+"='"+user.getGender()+"', "+
                Const.EMPLOYEE_SUBCATEGORY_CODE+"=(SELECT "+Const.SUBCATEGORY_CODE+" FROM "+ Const.SUBCATEGORY_TABLE + " WHERE "+Const.SUBCATEGORY+"='"+user.getDepartment()+"'), "+
                Const.EMPLOYEE_POSITION_CODE+"=(SELECT "+Const.POSITION_CODE+" FROM "+ Const.POSITION_TABLE + " WHERE "+Const.POSITION+"='"+user.getPosition()+"'), "+
                Const.EMPLOYEE_ACCEPT_DATE+"='"+user.getAcceptDay()+"', "+Const.EMPLOYEE_FIRE_DATE+"='"+user.getFireDay()+"' WHERE "
                + Const.USERS_ID+"='"+user.getId()+"'";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
            preparedStatement.executeUpdate();
            return "Success";
        }
        catch (SQLException|ClassNotFoundException e)
        {
            System.out.println("Unable to update a record");
            return "Error";
        }

    }
    public void setDefault() throws SQLException, ClassNotFoundException, FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(getDbConnection());
        Reader reader = new BufferedReader(new FileReader("C:\\Users\\MI\\Downloads\\Курсовая\\Курсовая\\App1\\src\\sample\\generateDB.sql"));
        sr.runScript(reader);
    }
    public ResultSet getNationality()
    {
        ResultSet resultSet=null;
        String nationality_select = "SELECT "+Const.NATIONALITY + " FROM " + Const.NATIONALITY_TABLE;
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(nationality_select);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getFamily()
    {
        ResultSet resultSet=null;
        String nationality_select = "SELECT "+Const.FAMILY_STATUS + ", "+ Const.FAMILY_HAS_CHILDREN+ " FROM "  + Const.FAMILY_TABLE;
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(nationality_select);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getDepartment()
    {
        ResultSet resultSet=null;
        String nationality_select = "SELECT "+Const.SUBCATEGORY + " FROM " + Const.SUBCATEGORY_TABLE;
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(nationality_select);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getPosition()
    {
        ResultSet resultSet=null;
        String nationality_select = "SELECT "+Const.POSITION + " FROM " + Const.POSITION_TABLE;
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(nationality_select);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getAcceptedPeople()
    {
        ResultSet resultSet=null;
        String accepted = "SELECT "+Const.EMPLOYEE_ACCEPT_DATE + " FROM " + Const.EMPLOYEE_TABLE + " WHERE "+Const.EMPLOYEE_SURNAME+"!='NULL'";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(accepted);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getFiredPeople()
    {
        ResultSet resultSet=null;
        String fired = "SELECT "+Const.EMPLOYEE_FIRE_DATE + " FROM " + Const.EMPLOYEE_TABLE+ " WHERE "+Const.EMPLOYEE_SURNAME+"!='NULL'";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(fired);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public ResultSet getBasicInfo()
    {
        ResultSet resultSet=null;
        String nationality_select = "SELECT "+Const.USERS_ID+","+Const.EMPLOYEE_SURNAME + ","+Const.EMPLOYEE_NAME+
                ","+ Const.EMPLOYEE_PATRON+ ","+Const.EMPLOYEE_BIRTHDAY+ " FROM " + Const.EMPLOYEE_TABLE + " WHERE "+ Const.EMPLOYEE_SURNAME+"!='NULL'";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(nationality_select);
            resultSet = preparedStatement.executeQuery();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public String deleteUser(String id)
    {
        String delete = "DELETE FROM "+Const.EMPLOYEE_TABLE+" WHERE "+ Const.USERS_ID+"='"+id+"'";
        try
        {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(delete);
            preparedStatement.executeUpdate();
            return "Success";
        } catch (SQLException|ClassNotFoundException e) {
            return "Error";
        }
    }


}
