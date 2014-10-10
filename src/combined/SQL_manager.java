/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combined;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Eskil
 */
public class SQL_manager {

    static HikariDataSource ds;

    static Statement st;

    public String instanceName;

    public SQL_manager() {

    }

    public void getAllTables(ChoiceBox choiceBox) throws SQLException {
        System.out.println(ds.getConnection().toString());
        //Henter ut alle tabellene i databasen ved hjelp av metadata
        DatabaseMetaData md = ds.getConnection().getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        while (rs.next()) {
            System.out.println(rs.getString(3));
            //deretter legger jeg til alle tabellene i en choicebox så brukeren kan velge hvilken tabell
            choiceBox.getItems().add(rs.getString(3));

        }
        ds.getConnection().close();

    }

    public boolean getConnection(String mySqlAdress, String myPort, String sqlInstance) throws SQLException, ClassNotFoundException {
    //metode for å etablere en tilkobling mot en database
    //bruker Hikari som er et library for Connection Pool
    //vi legger tilkoblingen i connection poolet. 
        try {
            HikariConfig config = new HikariConfig();
            config.setMaximumPoolSize(100);
            config.setConnectionTimeout(5000);
            config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
            config.addDataSourceProperty("serverName", mySqlAdress);
            config.addDataSourceProperty("port", myPort);
            config.addDataSourceProperty("databaseName", sqlInstance);
            config.addDataSourceProperty("user", "root");
            config.addDataSourceProperty("password", "root");

            ds = new HikariDataSource(config);
            ds.getConnection();
            ds.getConnection().close();
            System.out.println("Success, connected");
            instanceName = sqlInstance;
            return true;

        } catch (Exception e) {
            System.out.println("Oh noes, not connected " + e);
            return false;

        }

    }

    public ResultSet getDataFromSQL(String SQL) throws SQLException {

        try {

            ResultSet rs = ds.getConnection().createStatement().executeQuery(SQL);
            return rs;
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        ds.getConnection().close();
        return null;

    }
}
