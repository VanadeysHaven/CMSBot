package me.Cooltimmetje.CMSBot.Profiles;

import com.zaxxer.hikari.HikariDataSource;
import org.json.simple.parser.ParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class used for interfacing with the database.
 * Database should be hosted on always be hosted on localhost.
 *
 * @author Tim (Cooltimmetje)
 * @version v0.1-ALPHA-DEV
 * @since v0.1-ALPHA-DEV
 */
public class MySqlManager {

    /**
     * This is the DataSource instance.
     */
    private static HikariDataSource hikari = null;

    /**
     * This will setup the HikariDataSource with the needed properties and username and password.
     *
     * @param user The username that we can login with.
     * @param pass The password that belongs to the username.
     */
    public static void setupHikari(String user, String pass){
        hikari = new HikariDataSource();
        hikari.setMaximumPoolSize(10);

        hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikari.addDataSourceProperty("serverName", "localhost");
        hikari.addDataSourceProperty("port", 3306);
        hikari.addDataSourceProperty("databaseName", "cms_bot");
        hikari.addDataSourceProperty("user", user);
        hikari.addDataSourceProperty("password", pass);
    }

    /**
     * Used for closing the connection to the database.
     */
    public static void disconnect(){
        hikari.close();
    }

    /**
     * Get a viewer from the database.
     *
     * @param username The username of the viewer that we want.
     * @return The viewer instance.
     */
    public static CMSViewer getUserData(String username){
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CMSViewer viewer = null;

        String query = "SELECT * FROM user_data WHERE username = '" + username + "';";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(query);
            rs = ps.executeQuery();
            if(rs.next()){
                viewer = new CMSViewer(rs.getString("username"), rs.getString("data"));
            } else {
                viewer = null;
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return viewer;
    }


    /**
     * Save a viewer to the database (create new if non-existent)
     *
     * @param viewer The viewer instance.
     */
    public static void saveUserData(CMSViewer viewer){
        Connection c = null;
        PreparedStatement ps = null;
        String create = "INSERT INTO user_data VALUES(?,?) ON DUPLICATE KEY UPDATE data=?";

        try {
            c = hikari.getConnection();
            ps = c.prepareStatement(create);

            ps.setString(1, viewer.getUsername());
            ps.setString(2, viewer.getJSON());
            ps.setString(3, viewer.getJSON());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(c != null){
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
