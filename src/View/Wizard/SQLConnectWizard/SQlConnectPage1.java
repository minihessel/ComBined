/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Wizard.SQLConnectWizard;

import Controller.SQL_manager;
import View.Wizard.ValidatorRegler;
import View.Wizard.WizardPage;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author Eskil Hesselroth
 */
public final class SQlConnectPage1 implements WizardPage {

    public String header = "Please enter your connection info";
    Label lblConnectionStatus = new Label("");
    TextField txtField = new TextField("");
    TextField txtDataBase = new TextField("db");
    Label portORdatabasenameLabel = new Label("Port:");
    TextField txtPort = new TextField("");
    TextField txtInstance = new TextField("");
    PasswordField txtPassword = new PasswordField();
    TextField txtUserName = new TextField();
    Button testConnectionButton = new Button("Test connection");
    ComboBox whichDataBaseType = new ComboBox();
    private GridPane pane = new GridPane();

    private boolean fulfilled = false;
    SQL_manager sql_manager = new SQL_manager();

    public SQlConnectPage1() {
        whichDataBaseType.getItems().addAll("MySQL", "Oracle", "MSSQL");
        addElements();
        whichDataBaseType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (t1.equals("MySQL")) {
                    txtInstance.setDisable(false);
                    txtPort.setDisable(false);
                    portORdatabasenameLabel.setText("Port:");
                    txtInstance.setText("");
                }
                if (t1.equals("MSSQL")) {
                    portORdatabasenameLabel.setText("Database name:");
                    txtInstance.setDisable(false);
                    txtPort.setDisable(false);
                    txtInstance.setText("");
                }
                if (t1.equals("Oracle")) {
                    portORdatabasenameLabel.setText("Port:");
                    txtInstance.setDisable(true);
                    txtInstance.setText("no need");
                }

            }
        });
    }

    @Override
    public void addElements() {
        pane.add(new Label("Select database type: "), 0, 0);
        pane.add(whichDataBaseType, 1, 0);
        pane.add(new Label("IP adress: "), 0, 1);
        pane.add(txtField, 1, 1);
        pane.add(new Label("Instance: "), 0, 2);
        pane.add(txtInstance, 1, 2);
        pane.add(portORdatabasenameLabel, 0, 3);
        pane.add(txtPort, 1, 3);
        pane.add(new Label("Username: "), 0, 4);
        pane.add(txtUserName, 1, 4);
        pane.add(new Label("Password: "), 0, 5);
        pane.add(txtPassword, 1, 5);
        pane.add(new Label(""), 0, 6);
        pane.add(new Label("Test Connection: "), 0, 7);
        pane.add(testConnectionButton, 1, 7);
        testConnectionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                testSQLConnection();
            }

        });
        pane.add(lblConnectionStatus, 1, 8);
    }

    @Override
    public GridPane getPane() {
        return pane;
    }

    @Override
    public String getHeader() {
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

    @Override
    public void onEnter() {
        txtDataBase.setText("");
        txtField.setText("");
        txtInstance.setText("");
        txtPassword.setText("");
        txtPort.setText("");
        txtUserName.setText("");

    }

    @Override
    public void onFinish() {

    }

    private void testSQLConnection() {
        try {
            SQL_manager.getConnection(txtField.getText(), txtPort.getText(), txtInstance.getText(), txtUserName.getText(), txtPassword.getText(), whichDataBaseType.getSelectionModel().getSelectedItem().toString());
        } catch (SQLException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(SQlConnectPage1.class.getName()).log(Level.SEVERE, null, ex);
        }
        SQL_manager.task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                        // This handler will be called if Task succesfully executed login code
                // disregarding result of login operation

                // and here we act according to result of login code
                if (SQL_manager.task.getValue()) {
                    lblConnectionStatus.setText("Success, logged in");

                } else {
                    System.out.println("Invalid login");
                    lblConnectionStatus.setText("Sorry, we could not establish a conncetion");

                }

            }
        });
    }
}
