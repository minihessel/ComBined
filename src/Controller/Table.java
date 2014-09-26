package Controller;

import combined.SQL_manager;
import combined.checkForInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

/**
 *
 * @author Eskil Hesselroth
 */
public class Table {

    //En tabell må ha antall rader og hvilken tabell det er i alle tabellene(indeksen i en liste av tabeller)
    //ArrayList<Rader> rows = FXCollections.observableArrayList();
    ArrayList<Kolonne> listofColumns;
    int tableNumber;
    int numberofRows;
    ObservableList<List<String>> dataen;

    public Table() {
        listofColumns = new ArrayList<>();
        dataen = FXCollections.observableArrayList();
        

    }

    /**
     * Laster inn data fra angitt tabell fra angitt db. Bruker har valgt hvilken tabell - vi laster inn kolonner og rader i tableview
     */
    public void loadData(String SQL, SQL_manager sql_manager, Table tbl, int tableNumb) throws SQLException {
        numberofRows = 0;
        tableNumber = tableNumb;
        ResultSet rs = sql_manager.getDataFromSQL("localhost", 8889, "mysql", SQL);
        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
            String kolonneNavn = rs.getMetaData().getColumnName(i);
            Kolonne kol = new Kolonne(kolonneNavn, i - 1, tbl);

            listofColumns.add(kol);
            System.out.println("lzzzzegger til kolonne i settet med navn " + kol);

        }

        //deretter legges all dataen til i kolonnene ved hjelp av rader
        while (rs.next()) {
            numberofRows++;
            ObservableList<String> row = FXCollections.observableArrayList();

            for (Kolonne k : listofColumns) {
                k.addField(rs.getString(k.NAVN));

            }

            //deretter legger vi til alle feltene i de riktige kolonnene
        }

    }

    public void loadDataCombined(Table tbl) {

        int teller = 0;

        for (int a = 1; a <= tbl.numberofRows; a++) {
            ObservableList<String> row = FXCollections.observableArrayList();

            for (int i = 1; i <= listofColumns.size(); i++) {
                Kolonne kol = listofColumns.get(i - 1);

                try {
                    System.out.println(kol.allFields().get(1) + " herr");
                    row.add(kol.allFields().get(teller));

                } //Dersom SQL databasen ikke har noe data i denne kolonnen(null), legger vi bare inn et tomt felt
                catch (NullPointerException npe) {
                    row.add(" ");
                }

            }
            teller++;
            //legger til all dataen i arraylisten "dataen"
            dataen.add(row);

        }
        System.out.println("lagt til");

    }

   

    public TableView fillTableView(TableView tableView, Table tbl) {

        //Metode for å fylle tableview med kolonner og rader
        //først henter vi ut alle kolonnene og legger til de i tableview
        int counter = 0;

        //denne for løkken legger til kolonner dynamisk.
        //Dette må til da vi på forhånd ikke vet hvor mange kolonner det er og ikke har sjans til å lage en modell som forteller det
        for (Kolonne kol : listofColumns) {

            final int j = counter;
            TableColumn col = new TableColumn(kol.NAVN);
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());

                }
            });

            col.prefWidthProperty().bind(tableView.widthProperty().divide(4)); //for å automatisere bredden på kolonnene 

            col.setUserData(counter);
            tableView.getColumns().add(col);

            counter++;

        }

      
        for (Kolonne kol : listofColumns) {
            if(kol.amICombined == true)
            {
                kol.combineColumns();
            }
          dataen.addAll(kol.allFields());

        }

       dataen = transpose(dataen);

        //laster inn all dataen i tableviewen.
        tableView.setItems(dataen);
        //returnerer tableviewn til tableviewn som kalte på denne metoden
        return tableView;

    }

    static <T> ObservableList<List<String>> transpose(ObservableList<List<String>> table) {
        ObservableList<List<String>> ret
                = FXCollections.observableArrayList();

        // = <List<String>>();
        final int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            ObservableList<String> col = FXCollections.observableArrayList();
            for (List<String> row : table) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }

}
