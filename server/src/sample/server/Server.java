package sample.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Server {
    private static BufferedReader in;
    private static BufferedWriter out;
    private static Socket clientSocket;
    private static ServerSocket server = null;
    public static HrManager currentUser;

    public static void main(String[] args) {
        try {
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(System.in));

            // Reading data using readLine
            System.out.println("Хотите сгенерировать новую базу данных? Да/Нет");
            String generate = reader.readLine();
            if (generate.toLowerCase().equals("да"))
                setDefaultValues();
            server = new ServerSocket(4004);
            System.out.println("Server is listening ...");
            while (true) {
                try
                {
                    clientSocket = server.accept(); //сработает, когда клиент подключится,
                    System.out.println("New client connected "+clientSocket);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    Thread clientThread = new ClientHandler(clientSocket, in, out);
                    clientThread.start();
                    }
                catch(Exception e) {
                    clientSocket.close();
                    }

                }

        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        }
    }

    static String login(String username, String password) throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        User manager = new User();
        manager.setUserName(username);
        manager.setPassword(password);
        ResultSet resultSet = dbhandler.getUser(manager);
        int counter = 0;
        while (resultSet.next())
        {
            manager.setRole(resultSet.getString("Роль"));
            counter++;
        }
        if (counter>=1)
        {
            UserHolder userHolder = UserHolder.getInstance();
            userHolder.set(manager);
            return "Success";
        }
        else
        {
            return "NoUser";
        }
    }

    static void signUp(String username, String password) throws SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        User director = new User();
        director.setRole("Администратор");
        director.setUserName(username);
        director.setPassword(password);
        dbhandler.setUser(director);
        UserHolder userHolder = UserHolder.getInstance();
        userHolder.set(director);

    }
    static String addUser() throws IOException, ClassNotFoundException, SQLException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        String status = dbhandler.setEmployee();
        return status;
    }
    private static void setDefaultValues() throws SQLException, ClassNotFoundException, FileNotFoundException {
        DatabaseHandler dbhandler = new DatabaseHandler();
        dbhandler.setDefault();
    }
}
class ClientHandler extends Thread{
    final BufferedReader in;
    final BufferedWriter out;
    final Socket clientSocket;
    public ClientHandler(Socket clientSocket, BufferedReader in, BufferedWriter out)
    {
        this.clientSocket=clientSocket;
        this.in=in;
        this.out=out;
    }

    @Override
    public void run() {
        while(true)
        {
            try
            {
                String[] clientMessage = in.readLine().split(" ");
                if(clientMessage[0].equals("addUser"))
                {
                    String status = Server.addUser();
                    out.write(status+"\n");
                    out.flush();
                }
                else if(clientMessage[0].equals("stop"))
                {
                    System.out.println("Client "+ this.clientSocket+ "disconnected...");
                    this.clientSocket.close();
                    this.in.close();
                    this.out.close();
                    System.out.println("Connection with "+this.clientSocket+"closed");
                    break;
                }
                else if(clientMessage[0].equals("edit"))
                {
                    User user = DataHandler.Deserialize();
                    DatabaseHandler dbhandler = new DatabaseHandler();
                    String status = dbhandler.updateEmployee(user);
                    out.write(status+"\n");
                    out.flush();
                }
                else if(clientMessage[0].equals("deleteUser"))
                {
                    User user = (User) DataHandler.Deserialize();
                    DatabaseHandler dbhandler = new DatabaseHandler();
                    String status  = dbhandler.deleteUser(user.getId());
                    out.write(status+"\n");
                    out.flush();
                }
                else if (clientMessage[2].equals("login"))
                {
                    String status = Server.login(clientMessage[0], clientMessage[1]);
                    if (status.equals("NoUser"))
                    {
                        out.write(status+" found\n");
                    }
                    else
                    {
                        UserHolder userHolder = UserHolder.getInstance();
                        out.write(status+" "+userHolder.get().getRole().toString()+"\n");
                    }
                    out.flush();
                }
                else if(clientMessage[2].equals("signUp"))
                    Server.signUp(clientMessage[0],clientMessage[1]);
            }
            catch (ArrayIndexOutOfBoundsException | IOException | ClassNotFoundException | SQLException exception)
            {
                System.out.println("Unable to take data");
            }
        }

    }

}


