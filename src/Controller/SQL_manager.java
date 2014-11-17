/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import javafx.concurrent.Task;
import javafx.scene.control.ComboBox;

/**
 *
 * @author Eskil
 */
public class SQL_manager {

    public static Connection conn;
    public static ResultSet rs;
    public Boolean connected;
    public Task<Boolean> service;
    public static Task<Boolean> task;
    public static String instanceName;

    public SQL_manager() {

    }

    public static void getAllTables(ComboBox comboBox) throws SQLException {
        //Henter ut alle tabellene i databasen ved hjelp av metadata
        DatabaseMetaData md = conn.getMetaData();
        String[] types = {"TABLE"};
        ResultSet rs = md.getTables(null, null, "%", types);

        while (rs.next()) {
            System.out.println(rs.getString(3));
            //deretter legger jeg til alle tabellene i en choicebox så brukeren kan velge hvilken tabell
            comboBox.getItems().add(rs.getString(3));

        }

    }

    public static void getConnection(String mySqlAdress, String myPort, String sqlInstance, String userName, String passWord, String whichSQL) throws SQLException, ClassNotFoundException, InterruptedException, ExecutionException {

        task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Boolean result = null;
                try {
                    String myUrl = "";
                    if (whichSQL.equals("Oracle")) {
                        myUrl = "jdbc:oracle:thin:@" + mySqlAdress + ":" + myPort + ":XE";
                        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                    } else if (whichSQL.equals("MySQL")) {
                        myUrl = "jdbc:mysql://" + mySqlAdress + ":" + myPort + "/" + sqlInstance + "?socketTimeout=3000&connectTimeout=3000";
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                    } else if (whichSQL.equals("MSSQL")) {
                        //myPort here isn't the port number but actually what database name the user wants
                        myUrl = "jdbc:sqlserver://" + mySqlAdress + "\\" + sqlInstance + ";database=" + myPort;
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                    }
                    instanceName = sqlInstance;

                    System.out.println("Driver Loaded.");
                    // String myUrl = "jdbc:sqlserver://eskil-server-pc\\SQLExpress;database=test;username=sa;password=1234";

                    DriverManager.setLoginTimeout(10);
                    conn = DriverManager.getConnection(myUrl, userName, passWord);

                    System.out.println("Got Connection. " + conn);
                    result = true;
                } catch (SQLException e) {
                    System.out.println("Driver Loaded FAILED.");
                    System.out.println(e);
                    result = false;
                }
                return result;
            }

        };

        new Thread(task).start();

        //Thread completed, end main thread
        System.out.println("End");

    }

    public static void getDataFromSQL(String SQL) throws SQLException {

        try {
            System.out.println(conn + "blabla");
            rs = conn.createStatement().executeQuery(SQL);
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());

        }

    }
}
