package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlCreateDB {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String USER = "postgres";
    static final String PASS = "bec56Cr";


    public static void sql() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No driver");
            e.printStackTrace();

        }
        Statement statement = null;
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
         statement =connection.createStatement();
        String dbCreate = "CREATE DATABASE HOMEWORK4";
        assert statement != null;
        statement.executeUpdate(dbCreate);
        System.out.println("Database successfully created...");
    }
}
