/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.DataInsight;

import Model.Item;
import Model.Kolonne;
import Model.Table;
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
 * @ Data intelligence klassen(objektet).
 * @ En instans av objektet Tabell kan inneholde et data intelligence objekt
 * @ Objektet inneholder mange metoder som kan kjøres for å få data insight basert på transaksjonsdata.
 *
 */
public class DataIntelligence {

    Map<String, List<Item>> transdata;
    Map<String, Item> itemMap;
    Map<Integer, String> invertedItemMap;
    Map<String, String> itemIDandDescriptionMap;
    Integer createdProductNumber = 1;
    AlgoFPGrowth fpGrowth = new AlgoFPGrowth();
    AlgoAprioriInverse aprioriInverse = new AlgoAprioriInverse();
    AlgoAgrawalFaster94 algoAgrawal = new AlgoAgrawalFaster94();
    AssocRules rules;
    public Itemsets result;
    PopOver popover = new PopOver();
    public int numberOfTransactionsFound;

    String stats;
    Boolean rareItemSets = false;
    Boolean frequentItemSets = false;
    private String itemSetsStats;
    private String assocationRulesStats;

    public DataIntelligence(String rareOrFrequent) {

        if (rareOrFrequent.equals("rare")) {
            rareItemSets = true;
        }

        if (rareOrFrequent.equals("frequent")) {
            frequentItemSets = true;
        }
    }

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
        //metode for å finne sjeldene itemsets i et map av transaksjonsdata basert på AprioriInverse

        //Først henter vi ut hvilken tabell som nå er valgt i tableslisten
        //deretter looper vi igjennom dataen og legger den til i transdata mappet.
        //Deretter lager vi en tabell vi skal putte alle de nye itemsetsene inn i for å vise det til brukeren
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
        result = aprioriInverse.runAlgorithm(transdata, 0.0001, 0.0002);
        numberOfTransactionsFound = aprioriInverse.getDatabaseSize();
        itemSetsStats = aprioriInverse.getStats();
        rules = algoAgrawal.runAlgorithm(result, fpGrowth.getDatabaseSize(), 0.001);
        assocationRulesStats = algoAgrawal.getStats();

    }

    public void getFrequentItemSets(Table selectedTable, int transactionColumn, int itemColumn) throws IOException, FileNotFoundException, UnsupportedEncodingException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Metode for å finne populære itemsets i et map av transaksjonsdata basert på FPGrowth

        //Først henter vi ut hvilken tabell som nå er valgt i tableslisten
        //deretter looper vi igjennom dataen og legger den til i transdata mappet.
        //Deretter lager vi en tabell vi skal putte alle de nye itemsetsene inn i for å vise det til brukeren
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
        result = fpGrowth.runAlgorithm(transdata, 0.008);
        numberOfTransactionsFound = fpGrowth.getDatabaseSize();
        itemSetsStats = fpGrowth.getStats();
        rules = algoAgrawal.runAlgorithm(result, fpGrowth.getDatabaseSize(), 0.001);
        assocationRulesStats = algoAgrawal.getStats();
        //  rules.printRules(fpGrowth.getDatabaseSize());

    }

    public Table getItemSets(Itemsets result) throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {
        //Generell kode for å lage en tabell som inneholder itemsets, enten de er sjeldene eller populære

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

    public Table getAssociationRules() {
        //Kode for å finne assication rules i et sett av itemsets.(altså, denne finner assocation rules i et resultat fra en Frequent eller Rare itemsets metode)
        //For eksempel hvis du kjører FPGrowth, får du et objekt "Result", og da kan du finne assocation rules i objektet result. 
        Table table = new Table("Association rules");
        Kolonne ruleKolonne = new Kolonne("Assocation rule", 0, table, false, false);
        Kolonne supportKolonne = new Kolonne("Support ", 1, table, false, true);
        Kolonne confidenceKolonne = new Kolonne("Confidence", 2, table, false, true);

        for (Rule rule : rules.getRules()) {
            StringBuilder aRule = new StringBuilder();
            double confidence;
            int support;

            for (int i = 0; i < rule.getItemset1().length; i++) {
                aRule.append(invertedItemMap.get(rule.getItemset1()[i]));
                if (i != rule.getItemset1().length - 1 && rule.getItemset1().length > 0) {
                    aRule.append(" & ");
                }
            }

            aRule.append(" ==> ");
            for (int i = 0; i < rule.getItemset2().length; i++) {
                aRule.append(invertedItemMap.get(rule.getItemset2()[i]));
                if (i != rule.getItemset2().length - 1 && rule.getItemset2().length > 0) {
                    aRule.append(" & ");
                }
            }
            support = rule.getAbsoluteSupport();
            confidence = rule.getConfidence();
            System.out.println(aRule.toString());

            //Deretter looper vi igjennom itemsettene og legger til dataen i tabellen
            ruleKolonne.addField(aRule.toString());
            // print the support of this itemset
            supportKolonne.addField("" + support);
            confidenceKolonne.addField("" + confidence);
        }

        //legger til de nye kolonnene med all dataen i tabellen
        table.listofColumns.add(ruleKolonne);

        table.listofColumns.add(supportKolonne);
        table.listofColumns.add(confidenceKolonne);
        table.numberofRows = numberOfTransactionsFound;

        return table;

    }

    public List<Table> createUnderstandableSummary(Table itemTable, int itemIDColumn, int itemDescriptionColumn) {
        //Når metoden kalles opprettes det en mer forståelig visning kalt summary.
        //Summary krever at brukeren har koblet opp til en produkttabell som inneholder feltene " produktID og produktnavn".

        List<Table> tables;
        if (frequentItemSets) {
            tables = frequentSummary(itemTable, itemIDColumn, itemDescriptionColumn);
            return tables;
        }
        if (rareItemSets) {
            tables = rareSummary(itemTable, itemIDColumn, itemDescriptionColumn);
            return tables;
        } else {
            return null;
        }

    }

    private List<Table> rareSummary(Table itemTable, int itemIDColumn, int itemDescriptionColumn) throws NumberFormatException {
        //Hvis det er sjeldene itemsets, lag et "rare summary".
        //Hoved forskjellen er om det skal stå "least single items", eller "most popular items"
        // og "items that should not be placed together", eller "items that should be placed together".
        itemIDandDescriptionMap = new HashMap<>();
        List<Table> tables = new ArrayList();
        for (List<String> a : itemTable.sortedData) {
            itemIDandDescriptionMap.put(a.get(itemIDColumn), a.get(itemDescriptionColumn));
        }
        int levelCount = 0;
        for (List<Itemset> level : result.getLevels()) {

            if (levelCount > 0) {
                Table tabell;
                if (levelCount == 1) {
                    tabell = new Table("Least popular single items");
                } else {
                    tabell = new Table("These " + levelCount + " items should probably not be placed together");
                }

                Kolonne items = new Kolonne("Items that should be placed together", 1, tabell, false, false);
                Kolonne support = new Kolonne("Threshold", 2, tabell, true, false);

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

                    Double soldTogether = (Double.parseDouble(itemset.getRelativeSupportAsString(numberOfTransactionsFound)) / 100) * numberOfTransactionsFound;
                    int supportNormalized = (int) Math.round(soldTogether);
                    items.addField(itemSet);
                    support.addField("" + supportNormalized);
                    if (itemset.size() > 1) {
                        //tabell.rowMessages.add("The reason is because this is sold together " + soldTogether + " times out of all the " + fpGrowth.getDatabaseSize() + " transactions");
                        tabell.rowMessages.add(String.format("We recommend not placing these items together because out of your %d transactions, this combination of items are only sold together %.0f times.", numberOfTransactionsFound, soldTogether));
                    } else {

                        tabell.rowMessages.add(String.format("This item is not popular,  because we found that out of your %d transactions, this item is only sold %.0f times.", numberOfTransactionsFound, soldTogether));
                    }
                }

                tabell.listofColumns.add(items);
                tabell.listofColumns.add(support);

                tables.add(tabell);
                tabell.numberofRows = items.allFields().size();
            }
            levelCount++;

        }
        tables.add(assoicationRulesSummary());
        return tables;
    }

    private List<Table> frequentSummary(Table itemTable, int itemIDColumn, int itemDescriptionColumn) throws NumberFormatException {
        //Hvis det er populære itemsets, lag et "frequent summary".
        //Hoved forskjellen er om det skal stå "least single items", eller "most popular items"
        // og "items that should not be placed together", eller "items that should be placed together".
        itemIDandDescriptionMap = new HashMap<>();
        List<Table> tables = new ArrayList();
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
                Kolonne support = new Kolonne("Threshold", 2, tabell, true, false);

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

                    Double soldTogether = (Double.parseDouble(itemset.getRelativeSupportAsString(numberOfTransactionsFound)) / 100) * numberOfTransactionsFound;
                    int supportNormalized = (int) Math.round(soldTogether);
                    items.addField(itemSet);
                    support.addField("" + supportNormalized);
                    if (itemset.size() > 1) {
                        //tabell.rowMessages.add("The reason is because this is sold together " + soldTogether + " times out of all the " + fpGrowth.getDatabaseSize() + " transactions");
                        tabell.rowMessages.add(String.format("We recommend placing these items together because out of your %d transactions, this combination of items are sold together %.0f times.", numberOfTransactionsFound, soldTogether));
                    } else {

                        tabell.rowMessages.add(String.format("This item is popular because we found that out of your %d transactions, this item is sold %.0f times.", numberOfTransactionsFound, soldTogether));
                    }
                }

                tabell.listofColumns.add(items);
                tabell.listofColumns.add(support);

                tables.add(tabell);
                tabell.numberofRows = items.allFields().size();
            }
            levelCount++;

        }

        tables.add(assoicationRulesSummary());

        return tables;
    }

    private Table assoicationRulesSummary() {
        //Lager et summary for assocation rules.
        //altså istendfor at det brukes uforståelige tall og produktID'er
        //brukes det produkttekster og normaliserte tall.
        Table table = new Table("Association rules");
        Kolonne ruleKolonne = new Kolonne("Assocation rule", 0, table, false, false);
        Kolonne supportKolonne = new Kolonne("Treshhold", 1, table, true, false);
        Kolonne confidenceKolonne = new Kolonne("Confidence", 2, table, true, false);

        for (Rule rule : rules.getRules()) {
            StringBuilder aRule = new StringBuilder();
            StringBuilder messagePart1 = new StringBuilder();
            StringBuilder messagePart2 = new StringBuilder();
            double confidence;
            int support;

            for (int i = 0; i < rule.getItemset1().length; i++) {
                aRule.append(itemIDandDescriptionMap.get(invertedItemMap.get(rule.getItemset1()[i])).trim());
                messagePart1.append(itemIDandDescriptionMap.get(invertedItemMap.get(rule.getItemset1()[i])).trim());
                if (i != rule.getItemset1().length - 1 && rule.getItemset1().length > 0) {

                    aRule.append(" & ");
                    messagePart1.append(" & ");
                }
            }

            aRule.append(" ==> ");
            for (int i = 0; i < rule.getItemset2().length; i++) {
                aRule.append(itemIDandDescriptionMap.get(invertedItemMap.get(rule.getItemset2()[i])).trim());
                messagePart2.append(itemIDandDescriptionMap.get(invertedItemMap.get(rule.getItemset2()[i])).trim());
                if (i != rule.getItemset2().length - 1 && rule.getItemset2().length > 0) {

                    aRule.append(" & ");
                    messagePart2.append(" & ");
                }
            }
            support = rule.getAbsoluteSupport();
            confidence = rule.getConfidence();
            System.out.println(aRule.toString());

            //Deretter looper vi igjennom itemsettene og legger til dataen i tabellen
            ruleKolonne.addField(aRule.toString());
            // print the support of this itemset
            supportKolonne.addField("" + support);
            confidenceKolonne.addField("" + Math.round(100 * confidence));
            System.out.println(confidence);
            table.rowMessages.add(String.format(" If a customer buys " + messagePart1.toString() + " there is a %d%% chance of the customer buying " + messagePart2 + " ", Math.round(100 * confidence)));

        }

        //legger til de nye kolonnene med all dataen i tabellen
        table.listofColumns.add(ruleKolonne);

        table.listofColumns.add(supportKolonne);
        table.listofColumns.add(confidenceKolonne);
        table.numberofRows = rules.getRulesCount();

        return table;
    }

    public String getItemSetsStats() {

        return itemSetsStats;
    }

    public String getAssociationRulesStats() {

        return assocationRulesStats;
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
