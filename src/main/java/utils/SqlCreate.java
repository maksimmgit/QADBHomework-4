package utils;

import java.sql.*;

public class SqlCreate {
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/homework4";
    static final String USER = "postgres";
    static final String PASS = "bec56Cr";

    static String sqlTableUsers = "CREATE TABLE IF NOT EXISTS users(" +
            " userId SERIAL NOT NULL PRIMARY KEY," +
            " name VARCHAR(50), " +
            " address VARCHAR(255))";
    static String sqlTableAccounts = "CREATE TABLE IF NOT EXISTS ACCOUNTS(" +
            "accountId SERIAL NOT NULL," +
            "userId INTEGER NOT NULL," +
            "balance INTEGER NOT NULL," +
            "currency varchar(10) NOT NULL," +
            "PRIMARY KEY(accountId)," +
            "FOREIGN KEY(userId) REFERENCES USERS (userId)" +
            ");";

    static String sqlTableTransactions = "CREATE TABLE TRANSACTIONS(" +
            "transactinId SERIAL NOT NULL," +
            "accountId INTEGER NOT NULL," +
            "amount INTEGER NOT NULL," +
            "PRIMARY KEY(transactinId)," +
            "FOREIGN KEY(accountId) REFERENCES accounts (accountId)" +
            ");";



    public static void sql() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No driver");
            e.printStackTrace();

        }


        try (
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement statement = connection.createStatement()
                //Statement statement = SqlConnect.sqlConnection();
        ) {

            //создаём таблицу Users
            statement.executeUpdate(sqlTableUsers);
            System.out.println("Table Users successfully created.");

            //создаём таблицу Accounts
            statement.executeUpdate(sqlTableAccounts);
            System.out.println("Table Accounts successfully created.");

            //создаём таблицу Transactions
            statement.executeUpdate(sqlTableTransactions);
            System.out.println("Table Transactions created.");




        }
        catch (
                SQLException e) {
            System.out.println("SQL error");
            e.printStackTrace();
        }
        catch (ArithmeticException e) {
            System.out.println("No connection");
            e.printStackTrace();
        }
    }

}
