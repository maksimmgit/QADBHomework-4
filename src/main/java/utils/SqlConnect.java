package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlConnect {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/homework4";
    static final String USER = "postgres";
    static final String PASS = "bec56Cr";

    public static Connection sqlConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No driver");
            e.printStackTrace();

        }
        Connection connection = null;
        Statement statement = null;
        connection = DriverManager.getConnection(DB_URL, USER, PASS);

        return connection;
    }
}
