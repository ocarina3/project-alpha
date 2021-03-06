package model.repository;

import database.Connect;
import model.entity.Genre;
import model.entity.Movie;
import view.principal.Main;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryMovie {

    private Connect c = new Connect();

    //__________________________________________CREATE_________________________________________________________________

    //Cria um Filme passando suas informações
    public void createMovie(Movie movie) {

        if(movie == null) return;

        String sql = "INSERT INTO movie(id,name,movieDirector,movieGenre,synopsis,minimumAge,images) " +
                "VALUES(?,?,?,?,?,?,?);";

        c.connect();

        PreparedStatement p = c.createPreparedStatement(sql);

        try {
            p.setString(2,movie.getName());
            p.setString(3,movie.getMovieDirector());
            p.setString(4,movie.getMovieGenre().toString());
            p.setString(5, movie.getSynopsis());
            p.setInt(6,movie.getMinimumAge());
            p.setBytes(7, movie.getImageByte());
            int teste = p.executeUpdate();

        }catch (SQLException | IOException e) {
            System.out.println("Deu Erro");
        }
        finally {
            if(p != null) {
                try {
                    p.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
                    System.out.println("ERROOOO");
                }
            }
            c.disconnect();
        }
    }

    //__________________________________________READ_________________________________________________________________

    //Le todos os filmes
    public ArrayList<Movie> readAllMovies() {
        String sql = "SELECT * FROM movie;";

        ResultSet result = null;

        c.connect();
        PreparedStatement p = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try {

            p = c.createPreparedStatement(sql);
            result = p.executeQuery();

            while (result.next()) {
                Movie movie = new Movie();
                movie.setId(result.getInt("id"));
                movie.setName(result.getString("name"));
                movie.setMovieDirector(result.getString("movieDirector"));
                movie.setMovieGenre(Genre.valueOf(result.getString("movieGenre")));
                movie.setSynopsis(result.getString("synopsis"));
                movie.setMinimumAge(result.getInt("minimumAge"));
                movie.setImageByte(result.getBytes("images"));
                movies.add(movie);
            }

        } catch (SQLException | IOException e) {

            e.printStackTrace();
        } finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return movies;
    }

    //Le os Filmes que tem um attributo com valor igual ao selecionado
    private ArrayList<Movie> readMovies(String attribute, String value) {
        String sql = "SELECT * FROM movie WHERE " + attribute + " = ?;";

        ResultSet result = null;

        c.connect();
        PreparedStatement p = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try {

            p = c.createPreparedStatement(sql);
            p.setString(1,value);
            result = p.executeQuery();

            while (result.next()) {
                Movie movie = new Movie();
                movie.setId(result.getInt("id"));
                movie.setName(result.getString("name"));
                movie.setMovieDirector(result.getString("movieDirector"));
                movie.setMovieGenre(Genre.valueOf(result.getString("movieGenre")));
                movie.setSynopsis(result.getString("synopsis"));
                movie.setMinimumAge(result.getInt("minimumAge"));
                movie.setImageByte(result.getBytes("images"));
                movies.add(movie);
            }

        } catch (SQLException | IOException e) {

            e.printStackTrace();
        } finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return movies;
    }

    //seleciona o atributo id para o readMovies
    public Movie readMoviesById(String value){
        return readMovies("id", value).get(0);
    }

    //seleciona o atributo nome para o readMovies
    public ArrayList<Movie> readMoviesByName(String value) {
        return readMovies("name", value);
    }

    //busca filmes que tenham um nome Similar ao valor escolhido
    public ArrayList<Movie> searchMovie(String value) {
        String sql = "SELECT * FROM movie WHERE name LIKE ?;";

        ResultSet result = null;

        c.connect();
        PreparedStatement p = null;
        ArrayList<Movie> movies = new ArrayList<>();

        try {

            p = c.createPreparedStatement(sql);
            p.setString(1,value+"%");
            result = p.executeQuery();

            while (result.next()) {
                Movie movie = new Movie();
                movie.setId(result.getInt("id"));
                movie.setName(result.getString("name"));
                movie.setMovieDirector(result.getString("movieDirector"));
                movie.setMovieGenre(Genre.valueOf(result.getString("movieGenre")));
                movie.setSynopsis(result.getString("synopsis"));
                movie.setMinimumAge(result.getInt("minimumAge"));
                movie.setImageByte(result.getBytes("images"));
                movies.add(movie);
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return movies;
    }

    //__________________________________________UPDATE_________________________________________________________________

    //Atualiza os dados no banco de dados de um filme
    public void updateMovie(Movie movie) {
        String sql = "UPDATE movie SET name = ?, movieDirector = ?, movieGenre = ?, synopsis = ?, minimumAge = ?, images = ?" +
                "WHERE id = ?";

        c.connect();

        PreparedStatement p = null;

        try {
            p = c.createPreparedStatement(sql);
            p.setString(1, movie.getName());
            p.setString(2, movie.getMovieDirector());
            p.setString(3, movie.getMovieGenre().toString());
            p.setString(4, movie.getSynopsis());
            p.setInt(5, movie.getMinimumAge());
            p.setBytes(6, movie.getImageByte());
            p.setInt(7, movie.getId());
            p.executeUpdate();
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    //__________________________________________DELETE_________________________________________________________________

    //Deleta um filme que tem um attributo com valor igual ao selecionado
    private void deleteMovie(String attribute,String value) {
        String sql = "DELETE FROM movie WHERE " + attribute + " = ?";

        c.connect();
        PreparedStatement p = null;
        try{

            p = c.createPreparedStatement(sql);
            p.setString(1,value);
            int deletedUsers = p.executeUpdate();
            System.out.println(deletedUsers);

        }catch (SQLException e){

            e.printStackTrace();
        }finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //seleciona o atributo id para o deleteMovie
    public void deleteMovieById(String value) {
        deleteMovie("id",value);
    }

    //seleciona o atributo nome para o deleteMovie
    public void deleteMovieByName(String value) {
        deleteMovie("name",value);
    }

}
