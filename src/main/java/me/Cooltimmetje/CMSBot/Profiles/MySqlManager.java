package me.Cooltimmetje.CMSBot.Profiles;

import com.zaxxer.hikari.HikariDataSource;

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

}
