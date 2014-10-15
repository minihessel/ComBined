package Controller;

import combined.SQL_manager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    SortedList<List<String>> sortedData;
    FilteredList<List<String>> filteredItems;
    SQL_manager sql_manager = new SQL_manager();

    public Table() {
        listofColumns = new ArrayList<>();
        dataen = FXCollections.observableArrayList();
        

    }

    /**
     * Laster inn data fra angitt tabell fra angitt db. Bruker har valgt hvilken tabell - vi laster inn kolonner og rader i tableview
     */
    public void loadData(String SQL, Table tbl, int tableNumb) throws SQLException {
        numberofRows = 0;
        tableNumber = tableNumb;

        sql_manager.getDataFromSQL(SQL);

        for (int i = 1; i <= sql_manager.rs.getMetaData().getColumnCount(); i++) {
            String kolonneNavn = sql_manager.rs.getMetaData().getColumnName(i);
            Kolonne kol = new Kolonne(kolonneNavn, i - 1, tbl);

            listofColumns.add(kol);

        }

        //deretter legges all dataen til i kolonnene ved hjelp av rader
        while (sql_manager.rs.next()) {
            numberofRows++;
            ObservableList<String> row = FXCollections.observableArrayList();

            for (Kolonne k : listofColumns) {
                k.addField(sql_manager.rs.getString(k.NAVN));

            }

        }
        //her skal tilkoblingen lukkes, kun fjernet mens jeg tester
        //SQL_manager.conn.close();

    }

    public TableView fillTableView(TableView tableView, Table tbl) {
        List<TextField> listOfTxtFields = new ArrayList();

        //Metode for å fylle tableview med kolonner og rader
        //først henter vi ut alle kolonnene og legger til de i tableview
        int counter = 0;

        //denne for løkken legger til kolonner dynamisk.
        //Dette må til da vi på forhånd ikke vet hvor mange kolonner det er og ikke har sjans til å lage en modell som forteller det
        for (Kolonne kol : listofColumns) {

            final int j = counter;
            TableColumn col = new TableColumn();

            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());

                }
            });

            col.setSortable(true);
            col.prefWidthProperty().bind(tableView.widthProperty().multiply(0.10)); //for å automatisere bredden på kolonnene 
            col.setUserData(counter);
            //For å legge til filtere på tableView dynamisk bruker jeg denne koden. Jeg lager en ny label, en ny tekstboks
            // disse legger jeg til i en vBoks som jeg setter som grafikkElement på hver eneste kolonne i tableviewet.
            TextField txtField = new TextField();
            Label lbl = new Label(kol.NAVN);
            lbl.setStyle("-fx-font-size:13px;");
            VBox vbox = new VBox();

            vbox.getChildren().add(lbl);

            vbox.getChildren().add(txtField);

            listOfTxtFields.add(txtField);
            col.setGraphic(vbox);

            tableView.getColumns().add(col);

            counter++;

        }

        //Her sjekker jeg om kolonnen som kommer er en vanlig eller kombinert kolonne. Er den en kombinert kaller vi på CombineColumns()
        // for å kombinere kolonnen.
        for (Kolonne kol : listofColumns) {
            if (kol.amICombined == true) {
                kol.combineColumns();
            }
            dataen.addAll(kol.allFields());

        }
        //ettersom jeg snakker til data vertikalt(fordi jeg snakker om kolonner), men tableView snakker om data i rader(horisontalt)
        //, snur jeg dataen fra vertikalt til horisontalt ved å bruke transpose.
        dataen = transpose(dataen);

        // Her legger jeg til filtreringen på tekstfeltene. Det viktige er at dette skjer dynamisk, fordi jeg ikke vet hvor mange tekstfelter jeg har
        //Bruker lambda funksjon som sier at HVIS det finnes rader som har teksten fra alle tekstfeltene, vis dem
        // med andre ord: den sjekker rett og slett :
        // SHOW DATA; WHERE DATA=txtField1,txtField2 osv.
        filteredItems = new FilteredList(dataen, e -> true);

        filteredItems.predicateProperty().bind(Bindings.createObjectBinding(()
                -> li -> {
                    for (int i = 0; i < li.size(); i++) {
                        if (!li.get(i).toLowerCase().
                        contains(
                                listOfTxtFields.get(i).getText().toLowerCase()
                        )) {
                            return false;
                        }
                    }
                    return true;
                },
                listOfTxtFields.stream().map(TextField::textProperty)
                .collect(Collectors.toList())
                .toArray(new StringProperty[listOfTxtFields.size()])));

        tableView.setMinHeight(1000);

        //for å ikke miste muligheten for å sortere data, legger vi det inn i en sorted list
        sortedData = new SortedList<>(filteredItems);
        //å binder det til tableViewen..Da mister vi ikke sorting funksjonalitet.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        //deretter setter vi tableView til å bruke denne nye "sorted data". 
        tableView.setItems(sortedData);

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
