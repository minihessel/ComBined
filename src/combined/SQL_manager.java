/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package combined;


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
     static Connection conn;

    static Statement st;

    public SQL_manager () {
    
        
    }
    

      public void getAllTables(ChoiceBox choiceBox) throws SQLException {
        //Henter ut alle tabellene i databasen ved hjelp av metadata
        DatabaseMetaData md = SQL_manager.conn.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);

        while (rs.next()) {
            //deretter legger jeg til alle tabellene i en choicebox så brukeren kan velge hvilken tabell
            choiceBox.getItems().add(rs.getString(3));
        }

    }
   

   public Connection getConnection(String mySqlAdress, Integer myPort,String sqlInstance) throws SQLException{
    
        
        try {
            // Step 1: Load the JDBC driver.
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            System.out.println("Driver Loaded.");
            // Step 2: Establish the connection to the database.
              String myUrl = "jdbc:mysql://"+mySqlAdress+":"+myPort + "/" + sqlInstance;

            conn = DriverManager.getConnection(myUrl, "root", "root");
            System.out.println("Got Connection.");

            st = conn.createStatement();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            e.printStackTrace();

        }
        return conn;
   }

 

          public ResultSet getDataFromSQL(String mySqlAdress, Integer myPort,String sqlInstance,String SQL) throws SQLException{
       
        try {
            //String myDriver = "org.gjt.mm.mysql.Driver";
            
            String myUrl = "jdbc:mysql://"+mySqlAdress+":"+myPort + "/" + sqlInstance;
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println(myUrl
              + "user=root&password=1234");
            DriverManager.setLoginTimeout(2);
            //Connection conn = DriverManager.getConnection(myUrl, "root", "1234");
          
            DriverManager.setLoginTimeout(2);
             
            ResultSet rs = conn.createStatement().executeQuery(SQL);
              
            return rs; 
        } 
      catch (Exception e)
{
  System.err.println("Got an exception! ");
  System.err.println(e.getMessage());
}
        return null;
    }
}
    
    

    
    
    
    


    
    

