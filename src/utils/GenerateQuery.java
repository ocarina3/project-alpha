package utils;

import data.base.Connect;
import view.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GenerateQuery {
    private static PreparedStatement p;
    private static ResultSet resultSet;

    private static Connect c = new Connect();

    private static void createQuery(String query) {
        String sql = query;

        c.connect();

        p = c.createPreparedStatement(sql);
        resultSet = null;
    }

    private static void endQuery() {
        if(p != null)
        {
            try{
                p.close();
                resultSet = p.getResultSet();
            }catch (SQLException ex){
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE,null,ex);
                System.out.println("ERROOOO");
            }
        }

        c.disconnect();
    }
}
