/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.mouseHooverAnimationPieChart;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import org.fxmisc.easybind.EasyBind;

/**
 *
 * @author Eskil Hesselroth
 */
public class Visualize {

    protected void getPieChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, PieChart pieChart, Label lbl) {
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());

        System.out.println("Kolonne id for data er " + nameColumn + "Kolonne id for name er " + valueColumn);
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {
                    String name = (String) rowData.get(nameColumn);
                    int value = Integer.parseInt(rowData.get(valueColumn));
                    return new PieChart.Data(name, value);
                }));
        System.out.println("aa " + pieChartData.get(0));
        pieChart.setData(pieChartData);

        for (PieChart.Data d : pieChartData) {
            //deretter legger vi animasjon på piecharten.. Men husk, her trenger vi å bytte ut label med en ny label som lages hver gang.
            d.getNode().setOnMouseClicked(new mouseHooverAnimationPieChart.MouseHoverAnimation(d, pieChart, lbl));
            d.getNode().setUserData(d.getPieValue());

        }
    }

    protected void getLineChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, LineChart lineChart) {
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        XYChart.Series series1 = new XYChart.Series();

        ObservableList<XYChart.Data> lineChartData
                = FXCollections.observableArrayList(EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {
                    String name = (String) rowData.get(nameColumn);
                    double value = Integer.parseInt(rowData.get(valueColumn));
                    return new XYChart.Data(name, value);
                }));

        series1.setData(lineChartData);
        lineChart.getData().add(series1);

    }

    protected void getBarChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, BarChart barChart) {
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        ObservableList<XYChart.Data<String, Number>> barChartData = EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {
            String name = (String) rowData.get(nameColumn);
            System.out.println(name);
            double value = Integer.parseInt(rowData.get(valueColumn));
            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().addAll(barChartData);
       barChart.getData().addAll(barChartData);


        
   for (Object s : barChart.getData())
       {
           System.out.println(s.toString());
       }
    }
}
