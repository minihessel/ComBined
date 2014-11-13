/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Eskil Hesselroth
 */
public final class SQlConnectPage1 implements WizardPage {

    public Label header = new Label("Please enter your connection info");
    Label lblConnectionStatus = new Label("");
    TextField txtField = new TextField("");
    TextField txtPort = new TextField("");
    TextField txtInstance = new TextField("");
    Button testConnectionButton = new Button("Test connection");
    final private GridPane pane = new GridPane();
    private boolean fulfilled = false;
    SQL_manager sql_manager = new SQL_manager();

    public SQlConnectPage1() {

        addElements();
    }

    @Override
    public void addElements() {
        pane.add(new Label("IP adress: "), 0, 0);
        pane.add(txtField, 1, 0);
        pane.add(new Label("Instance: "), 0, 1);
        pane.add(txtInstance, 1, 1);
        pane.add(new Label("Port: "), 0, 2);
        pane.add(txtPort, 1, 2);
        pane.add(new Label("Test Connection: "), 0, 3);
        pane.add(testConnectionButton, 1, 3);
        testConnectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                testSQLConnection();
            }

        });
        pane.add(lblConnectionStatus, 1, 5);
    }

    @Override
    public GridPane getPane() {
        return pane;
    }

    @Override
    public Label getHeader() {
        return header;
    }

    @Override
    public boolean valider() {
        fulfilled = ValidatorRegler.ikkeTom(txtField);
        fulfilled = ValidatorRegler.ikkeTom(txtInstance);
        fulfilled = ValidatorRegler.ikkeTom(txtPort);
        fulfilled = ValidatorRegler.connected(lblConnectionStatus, testConnectionButton);
        return fulfilled;
    }

    private void testSQLConnection() {
        try {
            sql_manager.getConnection(txtField.getText(), txtPort.getText(), txtInstance.getText());
        } catch (SQLException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        }
        sql_manager.task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                        // This handler will be called if Task succesfully executed login code
                // disregarding result of login operation

                // and here we act according to result of login code
                if (sql_manager.task.getValue()) {
                    lblConnectionStatus.setText("Success, logged in");

                } else {
                    System.out.println("Invalid login");
                    lblConnectionStatus.setText("Sorry, we could not establish a conncetion");

                }

            }
        });
    }
}
