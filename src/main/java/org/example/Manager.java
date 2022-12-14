package org.example;

import java.sql.*;
public class Manager {
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "fryshchyn";

    private static Connection connection;


    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // First
    public void infoAllPersons() {
        System.out.println("Info about all persons in data:");
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person ORDER BY Person.person_id";
            ResultSet resultSet = statement.executeQuery(SQL);
            setPersonInfo(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("------------------------------------");
    }

    // Second
    public void booksAmountMoreThan30() {
        System.out.println("There are books amount of which more than 30:");
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Book where amount>30";
            ResultSet resultSet = statement.executeQuery(SQL);
            setBookInfo(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("------------------------------------");
    }

    //Third
    public void showPersonsBook() {
        System.out.println("There are data about what book each person reads:");
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM Person  LEFT JOIN Book ON Person.book_credit=Book.book_id ORDER BY Person.name";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                int person_id = resultSet.getInt("person_id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                System.out.println(person_id + " " + name + " " + " " + surname + " reads " + title + " written by " + author);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("------------------------------------");
    }

    // Fourth
    public void selectBookByAuthor() {
        System.out.println("Books written by Frederik Backman:");
        try {
            String SQL = "SELECT * FROM Book WHERE author=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, "Backman Frederik");
            ResultSet resultSet = preparedStatement.executeQuery();
            setBookInfo(resultSet);
        } catch (
                SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("------------------------------------");
    }

    // Fifth
    public void getPersonWhereAgeMore30() {
        System.out.println("There are persons with age more than 30:");
        try {
            String SQL = "SELECT * FROM Person WHERE age>=?";
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setInt(1, 30);
            ResultSet resultSet = preparedStatement.executeQuery();
            setPersonInfo(resultSet);
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setPersonInfo(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Person person = new Person();
            person.setName(resultSet.getString("name"));
            person.setAge(resultSet.getInt("age"));
            person.setSurname(resultSet.getString("surname"));
            person.setEmail(resultSet.getString("email"));
            person.setPerson_id(resultSet.getInt("person_id"));
            person.setBook_credit(resultSet.getInt("book_credit"));
            System.out.println(person);
        }
    }

    public void setBookInfo(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Book book = new Book();
            book.setBook_id(resultSet.getInt("book_id"));
            book.setTitle(resultSet.getString("title"));
            book.setAuthor(resultSet.getString("author"));
            book.setAmount(resultSet.getInt("amount"));
            System.out.println(book);
        }
    }
}
