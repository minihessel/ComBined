package Controller;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eskil Hesselroth
 * This class defines what a column is
 */
public class Kolonne {

    //En kolonne må ha et navn
    //Den må også tilhøre en tabell
    public final String NAVN;
    public final Table tbl;

    //En kolonne inneholder felter(altså dataen i en kolonne
    private List<String> fields;

    public Kolonne(String kolonneNavn, Integer kolonneIndex, Table tbl1) {
        //konstruktøren
        NAVN = kolonneNavn;
        tbl = tbl1;

        fields = new ArrayList<>();
    }

    public Kolonne(List<Kolonne> listOfColumns, String navn, Table tbl1) {
        //Konstruktøren for en kombinert kolonne
        fields = new ArrayList<>();
        for (Kolonne kol : listOfColumns) {
            fields.addAll(kol.fields);
        }
        tbl = tbl1;
        NAVN = navn;

    }

    void addField(String item) {
        fields.add(item);
    }

    List<String> allFields() {
        return new ArrayList<String>(fields);
    }

    //Overkjører toString for å isteden returnere kolonnenavn og felter
    @Override
    public String toString() {
        return NAVN + fields.toString();
    }

}