package utils;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Scanner;

public class AccReg {
    public static void registration() throws SQLException {

        Scanner sc = new Scanner(System.in);
        String request = "INSERT INTO accounts (userid, balance, currency) VALUES (?, ?, ?)";
        String getNames = "SELECT name FROM users";

        System.out.println("Create new account");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

        //создаём новый аккаунт.
        PreparedStatement preparedStatement = SqlConnect.sqlConnection().prepareStatement(request);
        //Это всё постараться вынести в отдельный метод.
        for (int i = 0; i < 1; i++) {
            long balance = 0;
            String currency = "";
            System.out.println("Передайте зарегистрированное имя для нового аккаунта.\n" +
                    "Список доступных имён:");
            getNamesList(getNames);
            System.out.println("\nСписок существующих аккаунтов:");
            TransactionsPerform.getAllUsersAndBalances();
            System.out.println("\nВводите имя существующего пользователя:");
            ResultSet resultSet = selectFromDB(sc.nextLine());
            resultSet.next();
            System.out.println("Какую валюту добавляем?");
            currency = sc.nextLine();
            System.out.println("Сколько денег добавляем?");
            balance = sc.nextLong();


            int userId = resultSet.getInt("userid");
            String ownerName = resultSet.getString("name");
            //System.out.println(userId + " " + ownerName);
            preparedStatement.setInt(1,userId);
            preparedStatement.setLong(2,balance);
            preparedStatement.setString(3,currency);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();


    }

    /**
     * Строка request может меняться под запрос.
     * @param name принимаем имя зарегистрированного пользователя в базе
     * @return возвращаем ResultSet с айди пользователя и его именем из таблицы users
     * @throws SQLException
     */
    public static ResultSet selectFromDB(String name) throws SQLException {
        String request = "SELECT userid, name FROM users WHERE name=?";
        PreparedStatement preparedStatement = SqlConnect.sqlConnection().prepareStatement(request);
        preparedStatement.setString(1,name);
        String request2 = preparedStatement.toString();
        Statement statement = SqlConnect.sqlConnection().createStatement();
        preparedStatement.close();
        return statement.executeQuery(request2);
    }

    private static void getNamesList(String request) throws SQLException {
        Statement st = SqlConnect.sqlConnection().createStatement();
        ResultSet rs = st.executeQuery(request);
        while(rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }
}
