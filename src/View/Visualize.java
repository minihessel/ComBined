/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Table;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Eskil Hesselroth
 */
public class Visualize {

    private final Glow glow = new Glow(.8);
    //klassen for å lage dataen for visualiseringer
    private Map<String, Double> data = new HashMap<>();
    List<XYChart.Series> areaChartSeries = new ArrayList<>();
    List<XYChart.Series> lineChartSeries = new ArrayList<>();

    void getPieChartD() {

    }

    void addNewDataPoint(String name, double value) {
        data.merge(name, value, Double::sum);
    }

    void addNewDataPoint2(String name, double value) {
        data.merge(name, value, Double::sum);

    }

    public void getPieChartData(Integer nameColumn, Integer valueColumn, Tab tab, Map mapOverTabsAndTables, PieChart pieChart, Label lbl, Boolean newSeries, Boolean rowCounter) {
        data.clear();

        if (!newSeries) {
            pieChart.getData().clear();
        }

        addDataFromTable(mapOverTabsAndTables, tab, valueColumn, nameColumn, rowCounter);

        data.entrySet().stream().map(entry -> new PieChart.Data(entry.getKey(), entry.getValue())).forEach(pieChart.getData()::add);

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
            final ContextMenu contextMenu = new ContextMenu();
            MenuItem changeColor = new MenuItem("Change Color");
            MenuItem delete = new MenuItem("Standard color");
            ColorPicker cp = new ColorPicker();
            changeColor.setGraphic(cp);
            contextMenu.getItems().addAll(changeColor, delete);

            d.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if (t.getButton() == MouseButton.SECONDARY) {
                        delete.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                d.getNode().setStyle("");
                            }

                        });

                        cp.setValue(null);
                        cp.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                String hex1 = "#" + Integer.toHexString(cp.getValue().hashCode());

                                d.getNode().setStyle("-fx-background-color: " + hex1 + ";");
                            }
                        });

                        contextMenu.show(d.getNode(), t.getScreenX(), t.getScreenY());
                    }
                }

            });

        }

    }

    public void getLineChartData(Integer nameColumn, Integer valueColumn, Tab tab, Map mapOverTabsAndTables, LineChart lineChart, Boolean newSeries, Boolean rowCounter) {
        data.clear();
        ObservableList<XYChart.Data<String, Double>> lineChartData;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            lineChart.getData().clear();

        }
        lineChart.setAnimated(false);//bug fix
        addDataFromTable(mapOverTabsAndTables, tab, valueColumn, nameColumn, rowCounter);

        lineChartData
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data<String, Double>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(lineChartData);
        lineChart.getData().addAll(series1);
        lineChartSeries.add(series1);
        series1.getData().sort(Comparator.comparing(BarChart.Data<String, Double>::getYValue).reversed());
        series1.getNode().setUserData(lineChartSeries.size() - 1);
        addColorChangeOnSeries(series1);
        setupHover(series1);

    }

    public void getBarChartData(Integer nameColumn, Integer valueColumn, Tab tab, Map mapOverTabsAndTables, BarChart barChart, Boolean newSeries, Boolean rowCounter) {
        data.clear();
        ObservableList<XYChart.Data<String, Double>> barChartData;
        XYChart.Series series1 = new XYChart.Series();

        if (!newSeries) {
            series1.getData().clear();
            barChart.getData().clear();

        }
        barChart.setAnimated(false);//bug fix
        addDataFromTable(mapOverTabsAndTables, tab, valueColumn, nameColumn, rowCounter);

        barChartData
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data<String, Double>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        barChart.getData().addAll(series1);
        series1.getData().addAll(barChartData);

        series1.getData().sort(Comparator.comparing(BarChart.Data<String, Double>::getYValue).reversed());

        setupHover(series1);
        addColorChangeOnIndividual(barChartData);

    }

    public void getAreaChartData(Integer nameColumn, Integer valueColumn, Tab tab, Map mapOverTabsAndTables, StackedAreaChart areaChart, Boolean newSeries, Boolean rowCounter) {
        data.clear();
        ObservableList<XYChart.Data<String, Double>> areaChartData;
        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            areaChart.getData().clear();
            areaChartSeries.clear();

        }
        areaChart.setAnimated(false);//bug fix
        addDataFromTable(mapOverTabsAndTables, tab, valueColumn, nameColumn, rowCounter);

        areaChartData
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data<String, Double>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(areaChartData);

        areaChart.getData().addAll(series1);

        areaChartSeries.add(series1);

        series1.getData().sort(Comparator.comparing(BarChart.Data<String, Double>::getYValue).reversed());

        series1.getNode().setUserData(areaChartSeries.size() - 1);
        setupHover(series1);

        addColorChangeOnSeries(series1);

    }

    public void getScatterChartData(Integer nameColumn, Integer valueColumn, Tab tab, Map mapOverTabsAndTables, ScatterChart scatterChart, Boolean newSeries, Boolean rowCounter) {
        data.clear();
        ObservableList<XYChart.Data<String, Double>> scatterChartData;

        XYChart.Series series1 = new XYChart.Series();
        if (!newSeries) {
            series1.getData().clear();
            scatterChart.getData().clear();

        }
        scatterChart.setAnimated(false);//bug fix
        addDataFromTable(mapOverTabsAndTables, tab, valueColumn, nameColumn, rowCounter);

        scatterChartData
                = data.entrySet().stream()
                .map(entry -> new XYChart.Data<String, Double>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(() -> FXCollections.observableArrayList()));

        series1.getData().addAll(scatterChartData);
        scatterChart.getData().addAll(series1);
        series1.getData().sort(Comparator.comparing(BarChart.Data<String, Double>::getYValue).reversed());
        setupHover(series1);
        addColorChangeOnIndividual(scatterChartData);

    }

    private void addColorChangeOnIndividual(ObservableList<XYChart.Data<String, Double>> data
    ) {

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem changeColor = new MenuItem("Change Color");
        MenuItem delete = new MenuItem("Standard color");

        ColorPicker cp = new ColorPicker();
        changeColor.setGraphic(cp);
        contextMenu.getItems().addAll(changeColor, delete);

        for (XYChart.Data d : data) {

            d.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    if (t.getButton() == MouseButton.SECONDARY) {
                        delete.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                d.getNode().setStyle("");
                            }

                        });

                        cp.setValue(null);
                        cp.setOnAction(new EventHandler() {
                            public void handle(Event t) {
                                String hex1 = "#" + Integer.toHexString(cp.getValue().hashCode());

                                d.getNode().setStyle("-fx-background-color: " + hex1 + ";");
                            }
                        });

                        contextMenu.show(d.getNode(), t.getScreenX(), t.getScreenY());
                    }
                }

            });
        }

    }

    private void addDataFromTable(Map mapOverTabsAndTables, Tab tab, Integer valueColumn, Integer nameColumn, Boolean rowCount) throws NumberFormatException {
        Table selectedTable = (Table) mapOverTabsAndTables.get(tab);

        for (List<String> a : selectedTable.sortedData) {
            if (!a.get(valueColumn).isEmpty() && !a.get(valueColumn).isEmpty()) {
                //hvis brukeren ønsker å telle hvor mange rader en kategori har, bruker vi bare 1
                //for eksempel, hvor mange produkter har varegruppe Vifte, legg til 1 for hver vare
                if (rowCount) {
                    addNewDataPoint(a.get(nameColumn).toString(), 1);
                } //hvis ikke, bruker vi bare verdien fra kolonnen
                else {
                    addNewDataPoint(a.get(nameColumn).toString(), Double.parseDouble(a.get(valueColumn)));
                }

            } else {

            }
        }
    }

    private void addColorChangeOnSeries(XYChart.Series series
    ) {

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem changeColor = new MenuItem("Change color");
        MenuItem delete = new MenuItem("Standard color");
        ColorPicker cp = new ColorPicker();
        changeColor.setGraphic(cp);
        contextMenu.getItems().addAll(changeColor, delete);

        Node d = series.getNode();

        d.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    delete.setOnAction(new EventHandler() {
                        public void handle(Event t) {
                            if (series.getChart() instanceof StackedAreaChart) {
                                series.getChart().lookup(".default-color" + series.getNode().getUserData() + ".chart-series-area-fill").setStyle("");
                            }
                            if (series.getChart() instanceof LineChart) {
                                series.getChart().lookup(".default-color" + series.getNode().getUserData() + ".chart-series-line").setStyle("");
                            }
                        }

                    });
                    cp.setValue(null);
                    cp.setOnAction(new EventHandler() {
                        public void handle(Event t) {
                            String hex1 = "#" + Integer.toHexString(cp.getValue().hashCode());

                            if (series.getChart() instanceof StackedAreaChart) {
                                series.getChart().lookup(".default-color" + series.getNode().getUserData() + ".chart-series-area-fill").setStyle("-fx-fill:" + hex1 + ";");
                            }
                            if (series.getChart() instanceof LineChart) {
                                series.getChart().lookup(".default-color" + series.getNode().getUserData() + ".chart-series-line").setStyle("-fx-stroke:" + hex1 + ";");
                            }

                        }
                    });

                    contextMenu.show(d, t.getScreenX(), t.getScreenY());
                }
            }

        });
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
