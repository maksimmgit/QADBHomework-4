package utils;

import java.sql.*;
import java.util.*;

public class TransactionsPerform {
    public static String sender;
    public static String recipient;
    public static Connection connection = null;


    static {
        try {
            connection = SqlConnect.sqlConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void transPerf() throws SQLException{
        Scanner sc = new Scanner(System.in);
        Statement st = connection.createStatement();


        //получаем список имён из базы
        usersList();

        System.out.println("Введите отправителя");
        sender = sc.nextLine();

        //получаем айди сендера по его имени
        int senderID = getUserID(sender);



        System.out.println("Информация по счетам пользователя: ");

        //работа со счетом отправителя. получаем его данные и записываем в мапу
        ResultSet senderBalance = getUserCurrencyAndBalance(senderID);
        HashMap<String, Long> senderBalanceMap = new HashMap<>();
        while(senderBalance.next()) {
            System.out.println(senderBalance.getString("currency") + " " + senderBalance.getString("balance"));
            senderBalanceMap.put(senderBalance.getString("currency"),senderBalance.getLong("balance"));
        }


        //записываем айди получателя
        //выбираем получателя
        System.out.println("Выбираем получателя!");
        usersList();
        recipient = getRecipient();
        int recipientID = getUserID(recipient);
        ResultSet recipientBalance = getUserCurrencyAndBalance(recipientID);
        HashMap<String, Long> recipientBalanceMap = new HashMap<>();
        while(recipientBalance.next()){
            System.out.println(recipientBalance.getString("currency") + " " + recipientBalance.getString("balance"));
            recipientBalanceMap.put(recipientBalance.getString("currency"), recipientBalance.getLong("balance"));
        }




        System.out.println("Выберите валюту");
        String currency = sc.nextLine();


        //выбираем сумму
        long amount = chooseAmount(sc);






        //выполняем трансфер
        System.out.println("Sender balance check = " + senderBalanceMap.get("USD"));
        System.out.println("senderID = " + senderID);
        System.out.println("recipientID =  " + recipientID);
        System.out.println("amount " + amount);
        System.out.println("currency " + currency);
        System.out.println("senderBalanceMap.get(currency) " + senderBalanceMap.get(currency));
        System.out.println("recipientBalanceMap.get(currency) " + recipientBalanceMap.get(currency));
        performTransfer(senderID,recipientID,amount,currency, senderBalanceMap.get(currency), recipientBalanceMap.get(currency));


    }

    public static void performTransfer(int senderID, int recipientID, long amount, String currency, long senderBalance, long recipientBalance) throws SQLException {
        String senderQuery = "UPDATE accounts SET balance=? WHERE accountid=?";
        int senderAccountID = getAccountId(currency,senderID);
        long senderBalanceQuery = senderBalance - amount;
        PreparedStatement preparedStatement = connection.prepareStatement(senderQuery);
        preparedStatement.setLong(1, senderBalanceQuery);
        preparedStatement.setInt(2, senderAccountID);
        preparedStatement.executeUpdate();

        int recipientAccountId = getAccountId(currency,recipientID);
        long recipientBalanceQuery = recipientBalance + amount;
        String recipientQuery = "UPDATE accounts SET balance=?::integer WHERE accountid=?::integer";
        PreparedStatement preparedStatementRecipient = connection.prepareStatement(recipientQuery);
        preparedStatementRecipient.setString(1, String.valueOf(recipientBalanceQuery));
        preparedStatementRecipient.setString(2,String.valueOf(recipientAccountId));
        preparedStatementRecipient.executeUpdate();

        long senderBalanceToTheTable = amount*-1;
        performTransactionsTableUpdate(senderAccountID, senderBalanceToTheTable);


        performTransactionsTableUpdate(recipientAccountId,amount);




    }

    /**
     * Обновляем таблицу транзакций. записываем либо положительное число, либо отрицательное
     * @param accountid айди аккаунта отправителя либо получателя.
     * @param amount сумма. либо положительная, либо отрицательная
     * @throws SQLException
     */
    public static void performTransactionsTableUpdate(int accountid, long amount) throws SQLException {
        String transactionsListQuery = "INSERT INTO transactions (accountid, amount) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(transactionsListQuery);
        preparedStatement.setInt(1, accountid);
        preparedStatement.setLong(2, amount);
        preparedStatement.executeUpdate();


    }


    public static void usersList()throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT name FROM users");
        while ( rs.next()) {
            System.out.println(rs.getString("name"));
        }
    }

    public static String getRecipient(){
        System.out.println("Выберите получателя");
        Scanner sc = new Scanner(System.in);
        recipient = sc.nextLine();
        if(recipient.equals(sender)){
            System.out.println("Отправитель не может быть получателем в данный момент.");
            return getRecipient();
        }else {
            return recipient;
        }
    }


    /**
     * Получаем айди пользователя по его имени и дальше можно работать с ним.
     * @param name Передаём зарегистрированное имя
     * @return возвращаем Rowset currency+balance для определенного айди
     * @throws SQLException
     */
    public static int getUserID(String name) throws SQLException {
        System.out.println("Список пользователей: ");
        String requestID = "SELECT userid FROM users WHERE name="+"'"+name+"'";
        //PreparedStatement preparedStatementID = SqlConnect.sqlConnection().prepareStatement(requestID);
        //preparedStatementID.setString(1,name);
        Statement statementID = connection.createStatement();
        ResultSet rsID = statementID.executeQuery(requestID);
        int id = 0;
        while(rsID.next()) {
            id = rsID.getInt("userId");
            System.out.println("id = " + id);
        }
        statementID.close();

        return id;
    }

    /**
     * Получаем данные о счетах по айди пользователя чтобы работать с этой информацией.
     * @param id айди пользователя, найденное по фамилии
     * @return ResultSet его валюта+счёт.
     * @throws SQLException
     */
    public static ResultSet getUserCurrencyAndBalance(int id) throws SQLException {
        String requestCurrencyAndBalance = "SELECT currency, balance FROM accounts WHERE userid="+"'"+id+"'";
        Statement statementID = connection.createStatement();
        /*        while(rsCurrAndBalance.next()) {
            System.out.println(rsCurrAndBalance.getString("currency") + " " + rsCurrAndBalance.getString("balance"));
        }*/
        //rsCurrAndBalance.close();
        return statementID.executeQuery(requestCurrencyAndBalance);
    }


    public static long chooseAmount(Scanner sc){
        System.out.println("Выберите сумму не более 100 000 000");
        long amount = sc.nextLong();
        if(amount >= 100_000_000) {
            System.out.println("Введённая сумма больше лимита!");
            return chooseAmount(sc);
        }else{
            return amount;
        }

    }



    public static int getAccountId(String currency, int userid) throws SQLException {
        String requestAccID = "SELECT accountId FROM accounts WHERE userid=?::integer AND currency LIKE ?";
        PreparedStatement preparedStatement = connection.prepareStatement(requestAccID);
        preparedStatement.setString(1, (String.valueOf(userid)));
        preparedStatement.setString(2, currency);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt("accountId");
    }

    public static void  getAllUsersAndBalances() throws SQLException{
        String request = "SELECT users.name, accounts.currency, accounts.balance FROM users INNER JOIN accounts ON users.userid=accounts.userid";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(request);
        while(rs.next()) {
            System.out.println(rs.getString("name") + " " + rs.getString("currency") + " "+rs.getString("balance"));
        }
        st.close();
    }

}
