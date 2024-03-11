package org.example.dao;

import org.example.models.Person;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {


    private static Connection connection;
    private static final String URL = "jdbc:postgresql://localhost:5432/spring_app_1";
    private static final String USER = "postgres";
    private static final String PASSWORD = "99311111";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Person> index() throws SQLException {
        List<Person> list = new ArrayList<>();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM person");
        while (resultSet.next()){
            Person person = new Person(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getString(4)
            );
            list.add(person);
        }
        return list;

    }

    public Person show(int id) throws SQLException {
        String sql = "SELECT * FROM person WHERE id = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Person person = new Person(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getString(4)
        );
        return person;
    }

    public void save(Person person) throws SQLException {
        String sql = "INSERT INTO person (id, name, age, email) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, person.getId());
        preparedStatement.setString(2, person.getName());
        preparedStatement.setInt(3, person.getAge());
        preparedStatement.setString(4, person.getEmail());
        int rowsCreated = preparedStatement.executeUpdate();
        System.out.println("Rows created - " + rowsCreated);
    }

    public void update(int id, Person person) throws SQLException {
        String sql = "UPDATE public.person SET " +
                "name = ?::character varying," +
                "age = ?::integer," +
                "email = ?::character varying " +
                "WHERE id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, person.getName());
        preparedStatement.setInt(2, person.getAge());
        preparedStatement.setString(3, person.getEmail());
        preparedStatement.setInt(4, person.getId());
        int rowsUpdated = preparedStatement.executeUpdate();
        System.out.println("Rows updated - " + rowsUpdated);
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM public.person WHERE id IN (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        int rowsDeleted = preparedStatement.executeUpdate();
        System.out.println("Rows deleted - " + rowsDeleted);
    }
}
