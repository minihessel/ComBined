/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.MainFXMLController;
import DataInsight.AlgoFPGrowth;
import DataInsight.Itemset;
import DataInsight.Itemsets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.controlsfx.control.PopOver;

/**
 *
 * @author Eskil Hesselroth
 */
public class DataInsight {

    Map<String, List<Item>> transdata = new HashMap<>();
    Map<String, Item> itemMap = new HashMap<>();
    Map<Integer, String> invertedItemMap = new HashMap();
    Map<String, String> itemIDandDescriptionMap = new HashMap();
    Integer createdProductNumber = 1;
    AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
    Itemsets result;
    PopOver popover = new PopOver();

    void addNewDataToTransactionsMap(String key, String name) {
        //Metode for å legge til nye transaksjoner i transdata mappet
        //intialsierer et nytt objekt item
        Item item;
        //Vi gidder ikke ha flere eksemplarer av itemet, så vi sjekker i mappet om itemet finnes. 
        //Gjør det det, tilegner vi det itemet til objektet item.
        if (itemMap.containsKey(name)) {
            item = itemMap.get(name);

        } //eksisterer ikke itemet i itemMappet, lager vi et nytt item objekt som vi putter i mappet. 
        // i tillegg tilegner vi itemet en UNIK int verdi. String verdien er produktnummeret, men ettersom data mining går mye raskere med INT enn STRING,
        // tilegner vi produktet både en int verdi og en string verdi. String verdien er den originale produktid'en fra databasen, mens INT verdien er en tilegnet verdi
        else {
            item = new Item(name, createdProductNumber);
            itemMap.put(name, item);
            createdProductNumber++;
            invertedItemMap.put(item.createdInt, item.ID);
        }

        //deretter sjekker vi om keyen(transaksjonsid'en) finnes i transdata mappet, hvis den allerede finnes betyr det at vi skal legge til
        // item objektet i denne transaksjonen. 
        //Finnes den ikke, lager vi en ny transaksjon og legger til 
        if (transdata.containsKey(key)) {
            transdata.get(key).add(item);

        } else {
            List<Item> transaction = new ArrayList();
            transdata.put(key, transaction);
            transaction.add(item);

        }
    }

    void addNewItemToItemIDandDescriptionMap(String key, String name) {

    }

    //Metode for å regne ut itemsets(hvilke produkter som ofte blir solgt sammen)
    //Dette er en del av data mining og pattern recognition 
    //metoden benytter seg av FPGrowth, som ligger i et open source library utviklet av Philippe Fournier-Viger(http://www.philippe-fournier-viger.com/spmf/)
    // Jeg har tilpasset FPGrowth og bibliteket masse og optimalisert det en del for at det skal fungere med min kode.  
    public Table getInsight(Integer nameColumn, Integer valueColumn, TabPane tabPane, Map mapOverTabsAndTables, int transactionColumn, int itmeColumn) throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Først henter vi ut hvilken tabell som nå er valgt i tableslisten
        //deretter looper vi igjennom dataen og legger den til i transdata mappet.
        Table selectedTable = (Table) mapOverTabsAndTables.get(tabPane.getSelectionModel().getSelectedItem());
        for (List<String> a : selectedTable.sortedData) {
            addNewDataToTransactionsMap(a.get(transactionColumn), a.get(itmeColumn));
        }

        //minsup betyr minimum support..Alså, hva er minste grensen i % for itemsets vi skal se etter.
        double minsup = 0.004;

        //kjører FPGrowth algoritmen for å finne itemsets i transdataen
        result = fpGrowth.runAlgorithm(transdata, null, minsup);

        //Deretter lager vi en tabell vi skal putte alle de nye itemsetsene inn i for å vise det til brukeren
        Table table = new Table("Data insight");
        //Lager tre kolonner, en for itemset, en for support og en for level. 
        // Itemset er settet av produktene
        // Support er hvor stor andel av alle transaksjonene som har dette itemsettet
        // Level er hvor mange produkter det er i itemsettet
        Kolonne itemSetKolonne = new Kolonne("itemset", 0, table, true);
        Kolonne supportKolonne = new Kolonne("Support ", 1, table, true);
        Kolonne supporNormalizedColumn = new Kolonne("Support normalized", 2, table, true);
        Kolonne Level = new Kolonne("Number of items", 3, table, true);

        int levelCount = 0;

        //Deretter looper vi igjennom itemsettene og legger til dataen i tabellen
        for (List<Itemset> level : result.getLevels()) {

            for (Itemset itemset : level) {
                Arrays.sort(itemset.getItems());
                int[] intArrayOfItemSets = itemset.getItems();
                String itemSet = "";
                int numberOfItems = 1;
                for (int i : intArrayOfItemSets) {
                    //Vi vil nå vise brukeren hvilket produktnummer det er istedenfor den tilegnede int verdien laget. 
                    // Av den grunn spør vi en inverted versjon av itemmappet om hva strengen(produktid'en) er for produktet

                    itemSet += invertedItemMap.get(i).replace(" ", "");
                    //en if setning for å legge til & tegn mellom strengen for produkter i strengen
                    if (intArrayOfItemSets.length > numberOfItems) {
                        itemSet += " & ";
                    }
                    numberOfItems++;

                }

                itemSetKolonne.addField(itemSet);
                // print the support of this itemset

                supportKolonne.addField(itemset.getRelativeSupportAsString(fpGrowth.getDatabaseSize()));

                Level.addField("" + levelCount);
                supporNormalizedColumn.addField(" " + (Double.parseDouble(itemset.getRelativeSupportAsString(fpGrowth.getDatabaseSize())) / 100) * fpGrowth.getDatabaseSize());
            }

            levelCount++;
        }
        //legger til de nye kolonnene med all dataen i tabellen
        table.listofColumns.add(itemSetKolonne);
        table.listofColumns.add(Level);
        table.listofColumns.add(supportKolonne);
        table.listofColumns.add(supporNormalizedColumn);
        table.numberofRows = fpGrowth.getDatabaseSize();
        return table;

    }

    public Tab createSummary(Table itemTable, int itemIDColumn, int itemDescriptionColumn) {
        Tab summaryTab = new Tab("Summary");
        TreeView treeView = new TreeView();
        summaryTab.setContent(treeView);
        TreeItem rootItem = new TreeItem("Summary");
        treeView.setRoot(rootItem);

        for (List<String> a : itemTable.sortedData) {
            itemIDandDescriptionMap.put(a.get(itemIDColumn), a.get(itemDescriptionColumn));
        }

        int levelCount = 0;
        for (List<Itemset> level : result.getLevels()) {
            if (levelCount > 0) {
                TreeItem levelRoot;
                if (levelCount == 1) {
                    levelRoot = new TreeItem("The following single items are popular");
                } else {
                    levelRoot = new TreeItem("A combination of these " + levelCount + " items should be placed together");
                }

                for (Itemset itemset : level) {
                    Arrays.sort(itemset.getItems());
                    int[] intArrayOfItemSets = itemset.getItems();
                    String itemSet = "";
                    int numberOfItems = 1;
                    TreeItem<String> treeItemSet = new TreeItem();
                    for (int i : intArrayOfItemSets) {
                        //Vi vil nå vise brukeren hvilket produktnummer det er istedenfor den tilegnede int verdien laget. 
                        // Av den grunn spør vi en inverted versjon av itemmappet om hva strengen(produktid'en) er for produktet

                        itemSet += itemIDandDescriptionMap.get(invertedItemMap.get(i));
                        //en if setning for å legge til & tegn mellom strengen for produkter i strengen
                        if (intArrayOfItemSets.length > numberOfItems) {
                            itemSet += " and ";
                        }
                        numberOfItems++;

                    }
                    Label lbl = new Label("Here is some info on the itemset you clicked, blablalbalba");
                    treeItemSet.setValue(itemSet);
                    Button button = new Button("?");

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            popover.setContentNode(lbl);
                            popover.show(button);

                        }
                    });
                    levelRoot.getChildren().add(treeItemSet);
                    treeItemSet.setGraphic(button);

                }
                rootItem.getChildren().add(levelRoot);
            }
            levelCount++;

        }
        rootItem.expandedProperty().set(true);

        return summaryTab;
    }

    public Table createSummary2(Table itemTable, int itemIDColumn, int itemDescriptionColumn, MainFXMLController mainFXMLController) {
        Table tabell = new Table("Combination of items");
        for (List<String> a : itemTable.sortedData) {
            itemIDandDescriptionMap.put(a.get(itemIDColumn), a.get(itemDescriptionColumn));
        }

        int levelCount = 0;
        for (List<Itemset> level : result.getLevels()) {

            if (levelCount > 0) {

                if (levelCount == 1) {

                } else {

                }
                Kolonne items = new Kolonne("Items that should be placed together", 0, tabell, true);
                Kolonne support = new Kolonne("Threshold", 1, tabell, true);
                Kolonne howManyItems = new Kolonne("How many items", 2, tabell, false);

                for (Itemset itemset : level) {

                    Arrays.sort(itemset.getItems());
                    int[] intArrayOfItemSets = itemset.getItems();
                    String itemSet = "";
                    int numberOfItems = 1;

                    for (int i : intArrayOfItemSets) {
                        //Vi vil nå vise brukeren hvilket produktnummer det er istedenfor den tilegnede int verdien laget. 
                        // Av den grunn spør vi en inverted versjon av itemmappet om hva strengen(produktid'en) er for produktet

                        itemSet += itemIDandDescriptionMap.get(invertedItemMap.get(i));
                        //en if setning for å legge til & tegn mellom strengen for produkter i strengen
                        if (intArrayOfItemSets.length > numberOfItems) {
                            itemSet += " and ";
                        }
                        numberOfItems++;

                    }
                    items.addField(itemSet);
                    support.addField("" + (Double.parseDouble(itemset.getRelativeSupportAsString(fpGrowth.getDatabaseSize())) / 100) * fpGrowth.getDatabaseSize());
                    howManyItems.addField("" + levelCount);

                }
                tabell.listofColumns.add(items);
                tabell.listofColumns.add(support);
                tabell.listofColumns.add(howManyItems);

            }
            levelCount++;

        }

        return tabell;
    }

    public String getStats() {

        return fpGrowth.printStats();
    }

    //metode for å inverte ett map
    static <V, K> Map<V, K> invert(Map<K, V> map) {

        Map<V, K> inv = new HashMap<V, K>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            inv.put(entry.getValue(), entry.getKey());
        }

        return inv;
    }

}
