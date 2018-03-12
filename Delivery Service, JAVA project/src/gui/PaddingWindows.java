package gui;

import cities.City;
import execution.Data;
import execution.DepartureThread;
import execution.ParcelTableStuff;
import execution.TransportTableStuff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parcel.Parcel;
import transport.Plane;
import transport.Transport;
import transport.Truck;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * Created by IVAN-PC on 01.05.2017.
 */
public class PaddingWindows  {
    public static void displayAddedParcel(String message, int trackingNumber, double price) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(message);
        window.setMinWidth(400);

        Label label = new Label("Your tracking number: " + trackingNumber);
        Label label1 = new Label("Total price is €" + String.format("%.2f", price) + " ");
        Button closeButton = new Button("OK!");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(5,5,5,5));
        layout.getChildren().addAll(label, label1, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayParcelTable(List<Parcel> allParcels, ArrayList<City> cities) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        ObservableList<ParcelTableStuff> parcelTableStuff = FXCollections.observableArrayList();

        Parcel current;
        Iterator<Parcel> iterator = allParcels.iterator();
        while (iterator.hasNext()) {
            current = iterator.next();
            parcelTableStuff.add(new ParcelTableStuff(current.getTrackingNumber(), current.getTypeOfParcel().getName(), current.getTypeOfDelivery(), cities.get(current.getCodeOfDpt()-1).getCity().getName(), cities.get(current.getCodeOfDst()-1).getCity().getName(), current.getStatus(), current.getWeight(), current.getPrice()));
        }

        TableColumn<ParcelTableStuff, Integer> numberColumn = new TableColumn<>("Tracking №");
        numberColumn.setMinWidth(80);
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("trackingNumber"));

        TableColumn<ParcelTableStuff, String> typeOfParcelColumn = new TableColumn<>("Type of Parcel");
        typeOfParcelColumn.setMinWidth(135);
        typeOfParcelColumn.setCellValueFactory(new PropertyValueFactory<>("typeOfParcel"));

        TableColumn<ParcelTableStuff, String> typeOfDelivery = new TableColumn<>("Delivery");
        typeOfDelivery.setMinWidth(85);
        typeOfDelivery.setCellValueFactory(new PropertyValueFactory<>("typeOfDelivery"));

        TableColumn<ParcelTableStuff, String> departureColumn = new TableColumn<>("From");
        departureColumn.setMinWidth(75);
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("dpt"));

        TableColumn<ParcelTableStuff, String> destinationColumn = new TableColumn<>("To");
        destinationColumn.setMinWidth(75);
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("dst"));

        TableColumn<ParcelTableStuff, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(330);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusOfParcel"));

        TableColumn<ParcelTableStuff, Integer> weightColumn = new TableColumn<>("Weight");
        weightColumn.setMinWidth(50);
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<ParcelTableStuff, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableView<ParcelTableStuff> table = new TableView<>();
        table.setItems(parcelTableStuff);
        table.getColumns().addAll(numberColumn, typeOfParcelColumn, typeOfDelivery, departureColumn, destinationColumn, statusColumn, weightColumn, priceColumn);


        TextArea parcelsInfo = new TextArea();
        parcelsInfo.setEditable(false);
        parcelsInfo.setMinHeight(70);
        parcelsInfo.setMinWidth(470);

        TextArea parcelsTracking = new TextArea();
        parcelsTracking.setEditable(false);
        parcelsTracking.setMinHeight(70);
        parcelsInfo.setMinWidth(470);

        table.setOnMouseClicked(e -> {
            parcelsInfo.clear();
            parcelsTracking.clear();
            ObservableList<ParcelTableStuff> selectedItems = table.getSelectionModel().getSelectedItems();
            Iterator<ParcelTableStuff> iterator1 = selectedItems.iterator();
            while (iterator1.hasNext()) {
                int trackingNumber = iterator1.next().getTrackingNumber();
                Iterator<Parcel> parcelIterator = allParcels.iterator();
                Parcel tempParcel;
                while (parcelIterator.hasNext()) {
                    tempParcel = parcelIterator.next();
                    if (tempParcel.getTrackingNumber() == trackingNumber) {
                        parcelsInfo.appendText(tempParcel.getTrackingNumber() + "\n" + tempParcel.getTypeOfParcel().getName());
                        if (!(tempParcel.getTypeOfParcel().getName().equals("Envelope")))
                            parcelsInfo.appendText(" (" + tempParcel.getWeight() + "kg, " + tempParcel.getLength() + " * " + tempParcel.getWidth() + " * " + tempParcel.getHeight() + " cm*cm*cm)");

                        parcelsInfo.appendText("\n" + tempParcel.getTypeOfDelivery() + "\n" + tempParcel.getStatus());

                        Iterator<String> stringIterator = tempParcel.getStatusList().iterator();
                        parcelsTracking.appendText(stringIterator.next());
                        while (stringIterator.hasNext()) {
                            parcelsTracking.appendText("\n" + stringIterator.next());
                        }
                    }
                }

            }


        });

        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5,5,5,5));
        hBox.getChildren().addAll(parcelsInfo, parcelsTracking);
        hBox.setAlignment(Pos.BASELINE_RIGHT);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);
        Scene scene = new Scene(vBox, 960, 400);
        window.setScene(scene);
        window.show();
    }

    public static void displayTransportTable(LinkedList<Transport> transports, ArrayList<City> cities, Data data) throws IOException, ParseException{
        Stage window = new Stage();
        //window.initModality(Modality.APPLICATION_MODAL);

        ObservableList<TransportTableStuff> transportTableStuff = FXCollections.observableArrayList();
        for (Transport transport : transports) transportTableStuff.add(new TransportTableStuff(transport, cities));

        TableColumn<TransportTableStuff, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(80);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeOfTransport"));

        TableColumn<TransportTableStuff, String> workingAreaColumn = new TableColumn<>("Working Area");
        workingAreaColumn.setMinWidth(120);
        workingAreaColumn.setCellValueFactory(new PropertyValueFactory<>("workingArea"));

        TableColumn<TransportTableStuff, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setMinWidth(220);
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<TransportTableStuff, Double> workloadColumn = new TableColumn<>("Workload");
        workloadColumn.setMinWidth(60);
        workloadColumn.setCellValueFactory(new PropertyValueFactory<>("workload"));

        TableView<TransportTableStuff> table = new TableView<>();
        table.setItems(transportTableStuff);
        table.getColumns().addAll(typeColumn, workingAreaColumn, locationColumn, workloadColumn);
        table.setMinWidth(507);

        Button refreshButton = new Button("Refresh");
        Button shipOneButton = new Button("Ship selected transport");
        Button shipAllButton = new Button("Ship all transport");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0f);
        progressBar.setPrefHeight(26);
        progressBar.setPrefWidth(160);
        Button cancelButton = new Button("Cancel");
        cancelButton.setDisable(true);

        TextArea contentsArea = new TextArea();
        contentsArea.setEditable(false);
        contentsArea.setMinHeight(340);
        contentsArea.setMinWidth(680);

        HBox transportMenu = new HBox(10);
        transportMenu.setPadding(new Insets(5,5,5,5));
        transportMenu.getChildren().addAll(refreshButton, shipOneButton, shipAllButton, progressBar, cancelButton);
        transportMenu.setAlignment(Pos.TOP_CENTER);

        VBox vBox = new VBox(10);
        vBox.setPadding(new Insets(5,5,5,5));
        vBox.getChildren().addAll(contentsArea, transportMenu);
        vBox.setAlignment(Pos.TOP_CENTER);


        table.setOnMouseClicked(e -> {
            contentsArea.clear();
            try {data.arrival();} catch (Exception ex) {}
            ObservableList<TransportTableStuff> selectedItems = table.getSelectionModel().getSelectedItems();
            for (TransportTableStuff o : selectedItems) {
                boolean isTruck = (o.getTypeOfTransport().equals("Truck")) ? true : false;
                for (Transport transport : data.getTransports()) {
                    if (o.getWorkingArea().equals(transport.getName()) && (transport instanceof Truck && isTruck || transport instanceof Plane && !isTruck) && transport.isOnWay()) {
                        contentsArea.appendText(transport.getArrivalDepartureTime() + "\n\n");
                        for (Parcel parcel : transport.getContents()) {
                            contentsArea.appendText(parcel.getTrackingNumber() + " - " + parcel.getTypeOfParcel().getName() + " - ");
                            for (Integer i : parcel.getWay()) {
                                contentsArea.appendText(cities.get(i).getCity().getName() + " ");
                            }
                            contentsArea.appendText("\n");
                        }
                    }
                }
            }
        });

        shipOneButton.setOnAction(e -> {
            try {
                data.arrival();
            } catch (Exception ex) {}
            ObservableList<TransportTableStuff> selectedItems = table.getSelectionModel().getSelectedItems();
            LinkedList<Transport> list = new LinkedList<>();
            for (TransportTableStuff o : selectedItems) {
                boolean isTruck = (o.getTypeOfTransport().equals("Truck")) ? true : false;
                for (Transport transport : data.getTransports()) {
                    if (o.getWorkingArea().equals(transport.getName()) && (transport instanceof Truck && isTruck || transport instanceof Plane && !isTruck)) {
                        list.add(transport);
                    }
                }
            }
            try {
                new DepartureThread(progressBar, cancelButton, data, transportTableStuff, list);
            } catch (IOException ex) {}

        });

        shipAllButton.setOnAction(e -> {
            try {
                data.arrival();
            } catch (Exception ex) {}

            try{
                new DepartureThread(progressBar, cancelButton, data, transportTableStuff, data.getTransports());
            } catch (IOException ex) {}
        });

        refreshButton.setOnAction(e -> {
            try {
                refreshTransportTable(data, transportTableStuff, data.getTransports());
            } catch (Exception ex) {}
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(table, vBox);

        Scene scene = new Scene(hBox, 1200, 400);
        window.setScene(scene);
        window.show();

    }

    public static void refreshTransportTable(Data data, ObservableList<TransportTableStuff> transportTableStuff, LinkedList<Transport> list) {
        try {data.arrival();} catch (Exception ex) {}
        for (Transport transport : list) {
            boolean isTruck = (transport instanceof Truck) ? true : false;
            String workingArea = transport.getName();
            for (TransportTableStuff o : transportTableStuff) {
                if (o.getWorkingArea().equals(workingArea) && (o.getTypeOfTransport().equals("Truck") && isTruck || o.getTypeOfTransport().equals("Plane") && !isTruck)) {
                    int index = transportTableStuff.indexOf(o);
                    transportTableStuff.set(index, new TransportTableStuff(transport, data.getCities()));
                }
            }
        }
    }
}
