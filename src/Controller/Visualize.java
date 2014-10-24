/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import View.mouseHooverAnimationPieChart;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
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

/**
 *
 * @author Eskil Hesselroth
 */
public class Visualize {

    private final Glow glow = new Glow(.8);
    //klassen for å lage dataen for visualiseringer
    private Map<String, Double> data = new HashMap<>();

    void getPieChartD() {

    }

    void addNewDataPoint(String name, double value) {
        data.merge(name, value, Double::sum);
    }

    protected void getPieChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, PieChart pieChart, Label lbl, Boolean newSeries) {
        data.clear();

        if (!newSeries) {
            pieChart.getData().clear();
        }

        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        for (List<String> a : tablesList.get(selectedTable).sortedData) {
            addNewDataPoint(a.get(nameColumn), Double.parseDouble(a.get(valueColumn)));
        }

        ObservableList<PieChart.Data> pieChartData2
                = data.entrySet().stream()
                .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));
        pieChart.getData().addAll(pieChartData2);

        for (PieChart.Data d : pieChart.getData()) {
            //deretter legger vi animasjon på piecharten.. 
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
        data.clear();
        ObservableList<XYChart.Data> pieChartData2;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            lineChart.getData().clear();

        }
        lineChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        for (List<String> a : tablesList.get(selectedTable).sortedData) {
            addNewDataPoint(a.get(nameColumn), Double.parseDouble(a.get(valueColumn)));
        }

        pieChartData2
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(pieChartData2);
        lineChart.getData().addAll(series1);

        setupHover(series1);

    }

    protected void getBarChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, BarChart barChart, Boolean newSeries) {
        data.clear();
        ObservableList<XYChart.Data> pieChartData2;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            barChart.getData().clear();

        }
        barChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        for (List<String> a : tablesList.get(selectedTable).sortedData) {
            addNewDataPoint(a.get(nameColumn), Double.parseDouble(a.get(valueColumn)));
        }

        pieChartData2
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(pieChartData2);
        barChart.getData().addAll(series1);

        setupHover(series1);

    }

    protected void getAreaChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, StackedAreaChart areaChart, Boolean newSeries) {
        data.clear();
        ObservableList<XYChart.Data> pieChartData2;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            areaChart.getData().clear();

        }
        areaChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        for (List<String> a : tablesList.get(selectedTable).sortedData) {
            addNewDataPoint(a.get(nameColumn), Double.parseDouble(a.get(valueColumn)));
        }

        pieChartData2
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(pieChartData2);
        areaChart.getData().addAll(series1);

        setupHover(series1);

    }

    protected void getScatterChartData(Integer nameColumn, Integer valueColumn, TabPane tabPane, List<Table> tablesList, ScatterChart scatterChart, Boolean newSeries) {
        data.clear();
        ObservableList<XYChart.Data> pieChartData2;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            scatterChart.getData().clear();

        }
        scatterChart.setAnimated(false);//bug fix
        int selectedTable = Integer.parseInt(tabPane.selectionModelProperty().getValue().getSelectedItem().getId());
        for (List<String> a : tablesList.get(selectedTable).sortedData) {
            addNewDataPoint(a.get(nameColumn), Double.parseDouble(a.get(valueColumn)));
        }

        pieChartData2
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(pieChartData2);
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
