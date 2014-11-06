/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import apriori.AlgoFPGrowth;
import apriori.Itemset;
import apriori.Itemsets;
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
import javafx.scene.control.TabPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class DataInsight {

    Map<String, List<Item>> transdata = new HashMap<>();
    Map<String, Item> itemMap = new HashMap<>();
    Map<Integer, String> invertedItemMap = new HashMap();
    Integer createdProductNumber = 1;

    void addNewDataToMap(String key, String name) {
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

    //Metode for å regne ut itemsets(hvilke produkter som ofte blir solgt sammen)
    //Dette er en del av data mining og pattern recognition 
    //metoden benytter seg av FPGrowth, som ligger i et open source library utviklet av Philippe Fournier-Viger(http://www.philippe-fournier-viger.com/spmf/)
    // Jeg har tilpasset FPGrowth og bibliteket masse og optimalisert det en del for at det skal fungere med min kode.  
    public Table getInsight(Integer nameColumn, Integer valueColumn, TabPane tabPane, Map mapOverTabsAndTables) throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Først henter vi ut hvilken tabell som nå er valgt i tableslisten
        //deretter looper vi igjennom dataen og legger den til i transdata mappet.
        Table selectedTable = (Table) mapOverTabsAndTables.get(tabPane.getSelectionModel().getSelectedItem());
        for (List<String> a : selectedTable.sortedData) {
            addNewDataToMap(a.get(1), a.get(0));
        }

        //minsup betyr minimum support..Alså, hva er minste grensen i % for itemsets vi skal se etter.
        double minsup = 0.004;

        AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
        //kjører FPGrowth algoritmen for å finne itemsets i transdataen
        Itemsets result = fpGrowth.runAlgorithm(transdata, null, minsup);

        fpGrowth.printStats();

        //Deretter lager vi en tabell vi skal putte alle de nye itemsetsene inn i for å vise det til brukeren
        Table table = new Table("Data insight");
        //Lager tre kolonner, en for itemset, en for support og en for level. 
        // Itemset er settet av produktene
        // Support er hvor stor andel av alle transaksjonene som har dette itemsettet
        // Level er hvor mange produkter det er i itemsettet
        Kolonne itemSetKolonne = new Kolonne("itemset", 0, table);
        Kolonne supportKolonne = new Kolonne("Support", 1, table);
        Kolonne supporNormalizedColumn = new Kolonne("Support normalized", 2, table);
        Kolonne Level = new Kolonne("Level in percentage", 3, table);

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
        table.listofColumns.add(supportKolonne);
        table.listofColumns.add(Level);
        table.listofColumns.add(supporNormalizedColumn);
        table.numberofRows = fpGrowth.getDatabaseSize();
        return table;

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
