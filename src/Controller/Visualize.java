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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import org.fxmisc.easybind.EasyBind;

/**
 *
 * @author Eskil Hesselroth
 */
public class Visualize {

    private final Glow glow = new Glow(.8);
    //klassen for å lage dataen for visualiseringer

    protected void getPieChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, PieChart pieChart, Label lbl, Boolean newSeries) {
        if (!newSeries) {
            pieChart.getData().clear();
        }
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());

        System.out.println("Kolonne id for data er " + nameColumn + "Kolonne id for name er " + valueColumn);
        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {
                    String name = (String) rowData.get(nameColumn);
                    int value = Integer.parseInt(rowData.get(valueColumn));
                    return new PieChart.Data(name, value);
                }));
        System.out.println("aa " + pieChartData.get(0));
        pieChart.getData().addAll(pieChartData);

        for (PieChart.Data d : pieChart.getData()) {
            //deretter legger vi animasjon på piecharten.. Men husk, her trenger vi å bytte ut label med en ny label som lages hver gang.
            d.getNode().setOnMouseClicked(new mouseHooverAnimationPieChart.MouseHoverAnimation(d, pieChart));
            final Node n = d.getNode();
            Tooltip tooltip = new Tooltip();
            String toolTipText = "Value : " + d.getPieValue();
            tooltip.setText(toolTipText);
            Tooltip.install(n, tooltip);
            n.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.setEffect(glow);
                }
            });
            n.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.setEffect(null);
                }
            });

        }

    }

    protected void getLineChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, LineChart lineChart, Boolean newSeries) {
        if (!newSeries) {
            lineChart.getData().clear();
        }
        lineChart.setAnimated(false);
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        XYChart.Series series1 = new XYChart.Series();

        ObservableList<XYChart.Data> lineChartData
                = FXCollections.observableArrayList(EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {

                    String name = rowData.get(nameColumn);

                    Number value = Integer.parseInt(rowData.get(valueColumn));

                    return new XYChart.Data(name, value);
                }));

        series1.setData(lineChartData);
        lineChart.getData().add(series1);
        setupHover(series1);

    }

    protected void getBarChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, BarChart barChart, Boolean newSeries) {
        if (!newSeries) {
            barChart.getData().clear();
        }
        barChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        ObservableList<XYChart.Data> barChartData = EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {

            String name = rowData.get(nameColumn);

            Number value = Integer.parseInt(rowData.get(valueColumn));
            System.out.println(name);

            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().addAll(barChartData);
        barChart.getData().addAll(series1);

        setupHover(series1);

    }

    protected void getAreaChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, StackedAreaChart areaChart, Boolean newSeries) {
        if (!newSeries) {
            areaChart.getData().clear();
        }

        areaChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        ObservableList<XYChart.Data> areaChartData = EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {
            String name = rowData.get(nameColumn);

            Number value = Integer.parseInt(rowData.get(valueColumn));
            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series(areaChartData);

        areaChart.getData().addAll(series1);
        //setupHover(series1);
    }

    protected void getScatterChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, ScatterChart scatterChart, Boolean newSeries) {
        if (!newSeries) {
            scatterChart.getData().clear();
        }
        scatterChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        ObservableList<XYChart.Data> scatterChartData = EasyBind.map(tablesList.get(selectedTable).sortedData, rowData -> {

            String name = rowData.get(nameColumn);

            Number value = Integer.parseInt(rowData.get(valueColumn));
            System.out.println(name);

            return new XYChart.Data(name, value);
        });

        XYChart.Series series1 = new XYChart.Series();
        series1.getData().addAll(scatterChartData);
        scatterChart.getData().addAll(series1);

        setupHover(series1);

    }

    private void setupHover(XYChart.Series<String, Number> series) {
        for (final XYChart.Data dt : series.getData()) {

            final Node n = dt.getNode();
            Tooltip tooltip = new Tooltip();
            String toolTipText = "XValue : " + dt.getXValue() + " & YValue : " + dt.getYValue();
            tooltip.setText(toolTipText);
            tooltip.setStyle(toolTipText);
            Tooltip.install(n, tooltip);

            n.setEffect(null);
            n.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.setEffect(glow);
                }
            });
            n.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.setEffect(null);
                }
            });

        }
    }
}
