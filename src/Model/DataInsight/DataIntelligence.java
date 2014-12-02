/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataInsight;

import Model.Item;
import Model.Kolonne;
import Model.Table;
import com.sun.jnlp.ApiDialog;
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
import org.controlsfx.control.PopOver;

/**
 *
 * @author Eskil Hesselroth
 */
public class DataIntelligence {

    Map<String, List<Item>> transdata;
    Map<String, Item> itemMap;
    Map<Integer, String> invertedItemMap;
    Map<String, String> itemIDandDescriptionMap;
    Integer createdProductNumber = 1;
    AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
    AlgoAprioriInverse aprioriInverse = new AlgoAprioriInverse();
    public Itemsets result;
    PopOver popover = new PopOver();
    public int numberOfTransactionsFound;
    String stats;

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
            if (transdata.get(key).contains(item)) {

            } else {
                transdata.get(key).add(item);
            }

        } else {
            List<Item> transaction = new ArrayList();
            transdata.put(key, transaction);
            transaction.add(item);

        }
    }

    public void getRareItemSets(Table selectedTable, int transactionColumn, int itemColumn) throws IOException, FileNotFoundException, UnsupportedEncodingException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        transdata = new HashMap<>();
        itemMap = new HashMap<>();
        invertedItemMap = new HashMap();
        result = null;
        createdProductNumber = 1;
        for (List<String> a : selectedTable.sortedData) {
            addNewDataToTransactionsMap(a.get(transactionColumn), a.get(itemColumn));
        }

        //minsup betyr minimum support..Alså, hva er minste grensen i % for itemsets vi skal se etter.
        double minsup = 0.60;

        //kjører FPGrowth algoritmen for å finne itemsets i transdataen
        //  result = fpGrowth.runAlgorithm(transdata, null, minsup);
        //result = fpGrowth.runAlgorithm(transdata, 0.01);
        result = aprioriInverse.runAlgorithm(transdata, 0.0001, 0.0003);
        numberOfTransactionsFound = aprioriInverse.getDatabaseSize();
        stats = aprioriInverse.getStats();

    }

    public void getFrequentItemSets(Table selectedTable, int transactionColumn, int itemColumn) throws IOException, FileNotFoundException, UnsupportedEncodingException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        transdata = new HashMap<>();
        itemMap = new HashMap<>();
        invertedItemMap = new HashMap();
        result = null;
        createdProductNumber = 1;
        for (List<String> a : selectedTable.sortedData) {
            addNewDataToTransactionsMap(a.get(transactionColumn), a.get(itemColumn));
        }

        //minsup betyr minimum support..Alså, hva er minste grensen i % for itemsets vi skal se etter.
        double minsup = 0.60;

        //kjører FPGrowth algoritmen for å finne itemsets i transdataen
        //  result = fpGrowth.runAlgorithm(transdata, null, minsup);
        //result = fpGrowth.runAlgorithm(transdata, 0.01);
        result = fpGrowth.runAlgorithm(transdata, 0.006);
        numberOfTransactionsFound = fpGrowth.getDatabaseSize();
        stats = fpGrowth.getStats();

    }

    //Metode for å regne ut itemsets(hvilke produkter som ofte blir solgt sammen)
    //Dette er en del av data mining og pattern recognition 
    //metoden benytter seg av FPGrowth, som ligger i et open source library utviklet av Philippe Fournier-Viger(http://www.philippe-fournier-viger.com/spmf/)
    // Jeg har tilpasset FPGrowth og bibliteket masse og optimalisert det en del for at det skal fungere med min kode.  
    public Table getInsight(Itemsets result) throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Først henter vi ut hvilken tabell som nå er valgt i tableslisten
        //deretter looper vi igjennom dataen og legger den til i transdata mappet.

        //Deretter lager vi en tabell vi skal putte alle de nye itemsetsene inn i for å vise det til brukeren
        Table table = new Table("Data insight");
        //Lager tre kolonner, en for itemset, en for support og en for level. 
        // Itemset er settet av produktene
        // Support er hvor stor andel av alle transaksjonene som har dette itemsettet
        // Level er hvor mange produkter det er i itemsettet
        Kolonne itemSetKolonne = new Kolonne("itemset", 0, table, false, false);
        Kolonne supportKolonne = new Kolonne("Support ", 1, table, false, true);
        Kolonne supporNormalizedColumn = new Kolonne("Support normalized", 2, table, false, true);
        Kolonne Level = new Kolonne("Number of items", 3, table, false, false);

        int levelCount = 0;

        //Deretter looper vi igjennom itemsettene og legger til dataen i tabellen
        for (List<Itemset> level : result.getLevels()) {

            for (Itemset itemset : level) {
                System.out.println(itemset.getItems());
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
                supportKolonne.addField(itemset.getRelativeSupportAsString(numberOfTransactionsFound));

                Level.addField("" + levelCount);
                supporNormalizedColumn.addField(" " + (Double.parseDouble(itemset.getRelativeSupportAsString(numberOfTransactionsFound)) / 100) * numberOfTransactionsFound);

            }

            levelCount++;
        }
        //legger til de nye kolonnene med all dataen i tabellen
        table.listofColumns.add(itemSetKolonne);
        table.listofColumns.add(Level);
        table.listofColumns.add(supportKolonne);
        table.listofColumns.add(supporNormalizedColumn);
        table.numberofRows = numberOfTransactionsFound;

        return table;

    }

    public List<Table> createSummary2(Table itemTable, int itemIDColumn, int itemDescriptionColumn) {
        itemIDandDescriptionMap = new HashMap<>();
        List<Table> tabs = new ArrayList();
        for (List<String> a : itemTable.sortedData) {
            itemIDandDescriptionMap.put(a.get(itemIDColumn), a.get(itemDescriptionColumn));
        }

        int levelCount = 0;
        for (List<Itemset> level : result.getLevels()) {

            if (levelCount > 0) {
                Table tabell;
                if (levelCount == 1) {
                    tabell = new Table("Most popular single items");
                } else {
                    tabell = new Table("These " + levelCount + " items should be placed together");
                }

                Kolonne items = new Kolonne("Items that should be placed together", 1, tabell, false, false);
                Kolonne support = new Kolonne("Threshold", 2, tabell, false, true);

                for (Itemset itemset : level) {

                    Arrays.sort(itemset.getItems());
                    int[] intArrayOfItemSets = itemset.getItems();
                    String itemSet = "";
                    int numberOfItems = 1;

                    for (int i : intArrayOfItemSets) {
                        //Vi vil nå vise brukeren hvilket produktnummer det er istedenfor den tilegnede int verdien laget. 
                        // Av den grunn spør vi en inverted versjon av itemmappet om hva strengen(produktid'en) er for produktet

                        itemSet += itemIDandDescriptionMap.get(invertedItemMap.get(i));
                        //i f setning for å legge til & tegn mellom strengen for produkter i strengen
                        if (intArrayOfItemSets.length > numberOfItems) {
                            itemSet += " and ";
                        }
                        numberOfItems++;

                    }

                    items.addField(itemSet);
                    support.addField("" + (Double.parseDouble(itemset.getRelativeSupportAsString(numberOfTransactionsFound)) / 100) * numberOfTransactionsFound);
                    Double soldTogether = (Double.parseDouble(itemset.getRelativeSupportAsString(numberOfTransactionsFound)) / 100) * numberOfTransactionsFound;
                    if (itemset.size() > 1) {
                        //tabell.rowMessages.add("The reason is because this is sold together " + soldTogether + " times out of all the " + fpGrowth.getDatabaseSize() + " transactions");
                        tabell.rowMessages.add(String.format("We reccomend placing these items together because out of your %d transactions, this combination of items are sold together %.0f times.", numberOfTransactionsFound, soldTogether));
                    } else {

                        tabell.rowMessages.add(String.format("This item is popular because we found that out of your %d transactions, this item is sold %.0f times.", numberOfTransactionsFound, soldTogether));
                    }
                }

                tabell.listofColumns.add(items);
                tabell.listofColumns.add(support);

                tabs.add(tabell);
                tabell.numberofRows = items.allFields().size();
            }
            levelCount++;

        }

        return tabs;
    }

    public String getStats() {

        return stats;
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
