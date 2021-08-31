import utils.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        //Создаём базу данных
        //SqlCreateDB.sqlConnection();
        //создаём таблицы
        //SqlCreate.sqlConnection();

        //Регистрируем первого юзера
        //UserReg.userRegistration();

        //Регистрируем аккаунт для юзера, добавляем валюту и сумму на счёт
        //AccReg.registration();

        //Добавляем деньги на счёт.
        //TransactionsPerform.getUserID("Petrovich");
        int check = 100;
        int check2 = 100*-1;
        System.out.println(check2);


        System.out.println("Начинаем. Ведите число для того чтобы поработать с базой.\n" +
                "1. Для создания базы данных и таблиц.\n" +
                "2. Для регистрации новых пользователей.\n" +
                "3. Создаём аккаунт и баланс для существующих юзеров. \n" +
                "4. Для просмотра списка пользователей и их счетов и балансов. \n" +
                "5. Для перевода денег от одного пользователя другому. Переводы себе запрещены.\n\n" +
                "Вводите: ");

        Scanner sc = new Scanner(System.in);
        switch (sc.nextInt()){
            case 1:
                System.out.println("Создание базы данных.");
                SqlCreateDB.sql();
                SqlCreate.sql();
                break;
            case 2:
                System.out.println("Регистрация новых пользователей.");
                UserReg.userRegistration();
                break;

            case 3:
                System.out.println("Создадим аккаунт новому пользователю и добавим деньжат.");
                AccReg.registration();
                break;

            case 4:
                System.out.println("Просмотр пользователей и их балансов.");
                TransactionsPerform.getAllUsersAndBalances();
                break;
            case 5:
                System.out.println("Перевод другому пользователю.");
                TransactionsPerform.transPerf();
                break;
            default:
                System.out.println("Ошибка.");
                break;

        }
    }
}
