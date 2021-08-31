package utils;

import dto.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class UserReg {

    public static void userRegistration() throws SQLException {

        Scanner sc = new Scanner(System.in);
        String request = "INSERT INTO users (name, address) VALUES (?, ?)";


        System.out.println("Insert new user");
        System.out.println("|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

        //вносим пацанов в базу
        PreparedStatement preparedStatement = SqlConnect.sqlConnection().prepareStatement(request);
        for (int i = 0; i < 1; i++) {
            System.out.println("Имя");
            String name = sc.nextLine();
            System.out.println("Адрес");
            String address = sc.nextLine();
            User user = new User(name, address);
            System.out.println(user);
            //preparedStatement.setInt(1,userid);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,address);
            preparedStatement.executeUpdate();
        }
        preparedStatement.close();

    }
}
