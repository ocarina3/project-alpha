package model.repository;

import database.Connect;
import model.ModelMovie;
import model.entity.Movie;
import model.entity.User;
import view.principal.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryUser {
    private Connect c = new Connect();

    //__________________________________________CREATE_________________________________________________________________

    //Cria um usuario passando suas informações e se ele é admin ou não
    private void createUser(User user, boolean admin)
    {


        String sql = "INSERT INTO user(id,name,email,password,birthDate,admin) VALUES(?,?,?,?,?,?);";
        c.connect();

        PreparedStatement p = c.createPreparedStatement(sql);

        try
        {
            p.setString(2,user.getName());
            p.setString(3, user.getEmail());
            p.setString(4, user.getPassword());
            p.setString(5,user.getBirthDate().toString());
            p.setBoolean(6,admin);
            p.executeUpdate();
        }catch (SQLException e)
        {
          System.out.println(e);
        }
        finally {
            if(p != null)
            {
                try{
                    p.close();
                }catch (SQLException ex){
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
                    System.out.println(ex);
                }
            }
            c.disconnect();
        }
    }

    //Cria um usuario Cliente
    public void createClient(User user) {
        createUser(user,false);
    }

    //Cria um usuario Admin
    public void createAdmin(User user) {
        createUser(user,true);
    }

    //Coloca um filme nos favoritos de um usuario
    public void favoriteMovies(User user ,Movie movie)
    {
        String sql = "INSERT INTO favoriteMovies(id,id_user,id_movie) VALUES(?,?,?);";
        c.connect();

        PreparedStatement p = c.createPreparedStatement(sql);

        try
        {
            p.setInt(2,user.getId());
            p.setInt(3,movie.getId());
            p.executeUpdate();
        }catch (SQLException e)
        {
            System.out.println(e);
        }
        finally {
            if(p != null)
            {
                try{
                    p.close();
                }catch (SQLException ex){
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
                    System.out.println(ex);
                }
            }
            c.disconnect();
        }
    }

    //__________________________________________READ_________________________________________________________________

    //Le os Usuarios que tem um attributo com valor igual ao selecionado
    private ArrayList<User> readUsers(String attribute, String value) {
        String sql = "SELECT * FROM user WHERE " + attribute + " = ?;";

        ResultSet result = null;

        c.connect();
        PreparedStatement p = null;
        ArrayList<User> users = new ArrayList<>(1);

        try {

            p = c.createPreparedStatement(sql);
            p.setString(1, value);
            result = p.executeQuery();

            while (result.next()) {
                User user = new User();
                user.setId(result.getInt("id"));
                user.setName(result.getString("name"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                String birthDate = result.getString("birthDate");
                user.setBirthDate(LocalDate.parse(birthDate));
                users.add(user);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            if (p != null) {
                try {
                    p.close();
                    c.disconnect();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return users;
    }

    //seleciona o atributo id para o readUsers
    public User readUsersById(String value){
        if(readUsers("id", value).size() != 0){
            return readUsers("id", value).get(0);}
        return null;
    }

    //seleciona o atributo email para o readUsers
    public User readUsersByEmail(String value) {
        if(readUsers("email", value).size() != 0){
        return readUsers("email", value).get(0);}
        return null;
    }

    //Verifica se um usuario é admin
    public boolean isAdmin(User user)
    {
        String sql = "SELECT * FROM user WHERE id = ?;";

        if (user == null) return false;

        ResultSet result = null;
        c.connect();
        PreparedStatement p = null;
        ArrayList<User> users = new ArrayList<>(1);

        try {

            p = c.createPreparedStatement(sql);
            p.setInt(1,user.getId());
            result = p.executeQuery();

            while (result.next()) {
                return result.getBoolean("admin");
            }


        } catch (SQLException e) {

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
        return false;
    }

    //seleciona o atributo Nome para o readUsers
    public ArrayList<User> readUsersByName(String value) {
        if(readUsers("name", value).size() != 0){
            return readUsers("name", value);}
        return null;
    }

    //Lê os filmes favoritos de um usuario
    public ArrayList <Movie> readFavoriteMovies(User user) {
        String sql = "SELECT id_movie FROM favoriteMovies Where id_user = ?;";
        ResultSet result = null;

        c.connect();
        PreparedStatement p = null;
        ArrayList<Movie> movies = new ArrayList<>(1);

        try {

            p = c.createPreparedStatement(sql);
            p.setInt(1, user.getId());
            result = p.executeQuery();

            int movieId;
            while (result.next()) {
                movieId = result.getInt("id_movie");
                movies.add(ModelMovie.getInstance().readMoviesById(String.format("%d",movieId)));
            }

        } catch (SQLException e) {

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

    //ve se um filme esta na lista de favoritos de um usuario
    public boolean isFavotited(User user, Movie movie) {
        String sql = "SELECT * FROM favoriteMovies WHERE id_user = ? AND id_movie = ?;";
        ResultSet result = null;
        Boolean r = false;
        c.connect();
        PreparedStatement p = null;
        ArrayList<Movie> movies = new ArrayList<>(1);
        try {

            p = c.createPreparedStatement(sql);
            p.setInt(1, user.getId());
            p.setInt(2,movie.getId());
            result = p.executeQuery();

             r = true;
            try{
            int i = result.getInt("id_user");
            int j = result.getInt("id_movie");}
            catch (SQLException e) {
                r = false;
            }

        } catch (SQLException e) {

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
        return r;
    }
    
    //__________________________________________UPDATE_________________________________________________________________

    //Atualiza os dados no banco de dados de um usuario
    public void updateUser(User user) {

        String sql = "UPDATE user SET name = ?, email = ?, password = ?, birthDate = ?  WHERE id = ?;";

        c.connect();
        PreparedStatement p = null;
        try{
            p = c.createPreparedStatement(sql);
            p.setString(1,user.getName());
            p.setString(2,user.getEmail());
            p.setString(3,user.getPassword());
            p.setString(4, user.getBirthDate().toString());
            p.setInt(5,user.getId());

            int i = p.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());

        }finally {
            if (p != null) {
                try{
                    p.close();
                    c.disconnect();
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    //__________________________________________DELETE_________________________________________________________________

    //Deleta um usuario que tem um attributo com valor igual ao selecionado
    private void deleteUser(String attribute,String value) {
        String sql = "DELETE FROM user WHERE " + attribute + " = ?";

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

    //seleciona o atributo email para o deleteUser
    public void deleteUserByEmail(String value){
        deleteUser("email", value);
    }

    //seleciona o atributo id para o deleteUser
    public void deleteUserById(String value){
        deleteUser("id", value);
    }

    //Remove um filme da lista de favoritos do usuario
    public void deleteFavoriteMovies(User user, Movie movie) {
        String sql = "DELETE FROM favoriteMovies WHERE id_user = ? AND id_movie = ?";

        c.connect();
        PreparedStatement p = null;
        try{

            p = c.createPreparedStatement(sql);
            p.setInt(1,user.getId());
            p.setInt(2, movie.getId());
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
}
