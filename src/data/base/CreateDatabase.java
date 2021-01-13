package data.base;

import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabase {

    private final Connect c;

    public CreateDatabase(Connect c) {
        this.c = c;
    }

    // Criação da tabela passando a query, cria a tabela apenas se ela não existe
    public void createTableUser() {
        String query = "CREATE TABLE IF NOT EXISTS user(" +
                "id integer primary key," +
                "name varchar," +
                "email varchar," +
                "password varchar," +
                "birthDate date," +
                "admin bool" +
                ");";
        boolean connected = false;

        try {
            connected = c.connect();
            Statement statement = c.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connected) c.disconnect();
        }
    }

    public void createTableMovie() {
        String query = "CREATE TABLE IF NOT EXISTS movie(" +
                "id integer primary key," +
                "name varchar," +
                "score float," +
                "movieDirector varchar," +
                "movieGenre varchar," +
                "synopsis varchar ," +
                "minimumAge integer" +
                ");";

        boolean connected = false;

        try {
            connected = c.connect();
            Statement statement = c.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connected) c.disconnect();
        }
    }

    public void createTableRating() {
        String query = "CREATE TABLE IF NOT EXISTS rating(" +
                "id integer primary key," +
                "rating float," +
                "id_user integer," +
                "id_movie integer" +
                ");";

        boolean connected = false;

        try {
            connected = c.connect();
            Statement statement = c.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connected) c.disconnect();
        }
    }

}