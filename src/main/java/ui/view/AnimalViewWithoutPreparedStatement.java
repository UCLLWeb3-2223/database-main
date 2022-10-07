package ui.view;

import domain.model.Animal;

import java.sql.*;
import java.util.Properties;

public class AnimalViewWithoutPreparedStatement {

    public static void main(String[] args) {

        // constants for your project
        // replace "webontwerp" by your own database, e.g. '2TX34'
        String url = "jdbc:postgresql://databanken.ucll.be:62223/webontwerp";
        // replace 'web3' by your own schema name, e.g. groep102
        String schema = "web3";


        // set properties for db connection
        Properties properties = new Properties();

        // set user and password
        try {
            Class.forName("ui.view.Secret"); // check if Secret does exist
            Secret.setPass(properties);
        } catch (ClassNotFoundException e) {
            System.out.println("Class ui.view.Secret with credentials not found");
        }

        properties.setProperty("ssl", "true");
        properties.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        properties.setProperty("sslmode", "prefer");

        //open the db connection
        try (Connection connection = DriverManager.getConnection(url, properties)) {

            // search an animal with name
            // without prepared statement
            System.out.println("Without prepared statement");

            String searchValue = "'' OR 1=1 OR '1'='1'";
            //String searchValue = "'Max'";
            String query = String.format("SELECT * from %s.animal where name = %s;", schema, searchValue);
            Statement statementInsert = connection.createStatement();
            ResultSet resultSet = statementInsert.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int food = resultSet.getInt("food");
                Animal animal = new Animal(id, name, type, food);
                System.out.println(animal.toString());
            }

            System.out.println("With prepared statement");
            // with prepared statement
            query = String.format("SELECT * from %s.animal where name = ?;", schema);
            PreparedStatement preparedStatementInsert = connection.prepareStatement(query);
            preparedStatementInsert.setString(1, searchValue);
            resultSet = preparedStatementInsert.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                int food = resultSet.getInt("food");
                Animal animal = new Animal(id, name, type, food);
                System.out.println(animal.toString());
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Connection no succes");
        }
    }

}
