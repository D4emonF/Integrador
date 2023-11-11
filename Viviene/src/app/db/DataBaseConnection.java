package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnection {
    private static Connection connection = null;
    private static Statement statement = null;

    public static void conectaNoBanco(){

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:Viviene.db");
            if (connection.isValid(30)){
                statement = connection.createStatement();
                statement.setQueryTimeout(30);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static Connection getConnexao(){
        return connection;
    }
    public static Statement getStatement(){return statement;}

    public static void desconectaDoBanco(){
        try {
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void fechaStatement(){
        try {
            if (getStatement() != null) {
                getStatement().close();
                System.out.println("Statement fechado!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}