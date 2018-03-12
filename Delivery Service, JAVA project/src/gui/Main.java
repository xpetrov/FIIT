package gui;

import execution.Data;
import execution.MyException;
import execution.Shipping;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import parcel.Parcel;

import java.io.IOException;
import java.text.ParseException;


public class Main extends Application{

    Stage window;
    Scene mainScene, scene1, scene2, scene3;
    public Data data;

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException, ParseException {
        //Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
        data = new Data();
        Button button1, button2, btnToMainScene1, btnToMainScene2;

        window = primaryStage;
        window.setTitle("Delivery Service");

        /*
         * The main scene
         */
        Label label = new Label("Welcome to \"Delivery Service!\"");
        button1 = new Button("New parcel");
        button1.setPrefWidth(130);
        button1.setOnAction(e -> window.setScene(scene1));
        button2 = new Button("Track Up Your Parcel");
        button2.setPrefWidth(130);
        button2.setOnAction(e -> window.setScene(scene2));

        Button parcelTableButton = new Button("Parcels");
        parcelTableButton.setPrefWidth(130);
        Button transportTableButton = new Button("Transport");
        transportTableButton.setPrefWidth(130);

        HBox userMenu = new HBox(10);
        userMenu.setPadding(new Insets(5,5,5,5));
        userMenu.getChildren().addAll(new Label("   User Menu:"), button1, button2);
        userMenu.setAlignment(Pos.CENTER);

        HBox adminMenu = new HBox(10);
        adminMenu.setPadding(new Insets(5,5,5,5));
        adminMenu.getChildren().addAll(new Label("Admin Menu:"), parcelTableButton, transportTableButton);
        adminMenu.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(5,5,5,5));
        mainLayout.getChildren().addAll(label, userMenu, adminMenu);
        mainLayout.setAlignment(Pos.CENTER);
        mainScene = new Scene(mainLayout, 450, 300);
        /*
         * New parcel
         */
        ChoiceBox<String> box1 = new ChoiceBox<>();
        fillChoiceBox(box1);
        box1.setValue("Athens");

        ChoiceBox<String> box2 = new ChoiceBox<>();
        fillChoiceBox(box2);
        box2.setValue("Berlin");

        HBox topMenu = new HBox(10);
        topMenu.setPadding(new Insets(5,5,5,5));
        btnToMainScene1 = new Button("Home");
        btnToMainScene1.setOnAction(e -> window.setScene(mainScene));
        topMenu.getChildren().add(btnToMainScene1);
        topMenu.setAlignment(Pos.BASELINE_LEFT);

        HBox layout1scene1 = new HBox(10);
        layout1scene1.setPadding(new Insets(5,5,5,5));
        layout1scene1.getChildren().addAll(new Label("From:"), box1, new Label("To:"), box2);
        layout1scene1.setAlignment(Pos.CENTER);

        HBox layout2scene1 = new HBox(10);
        layout2scene1.setPadding((new Insets(5,5,5,5)));
        ChoiceBox<String> box3 = new ChoiceBox<>();
        box3.getItems().addAll("by Truck", "by Plane");
        box3.setValue("by Truck");
        layout2scene1.getChildren().addAll(new Label("Choose type of delivery:"), box3);
        layout2scene1.setAlignment(Pos.CENTER);

        HBox layout3scene1 = new HBox(10);
        layout3scene1.setPadding(new Insets(5,5,5,5));
        CheckBox isEnvelope = new CheckBox("Would you like to send an envelope?");
        isEnvelope.setSelected(true);
        layout3scene1.getChildren().add(isEnvelope);
        layout3scene1.setAlignment(Pos.CENTER);

        HBox layout4scene1 = new HBox(10);
        TextField weightField = new TextField(), lengthField = new TextField(), widthField = new TextField(), heightField = new TextField();
        weightField.setPromptText("Weight (kg)");
        lengthField.setPromptText("Length (cm)");
        widthField.setPromptText("Width (cm)");
        heightField.setPromptText("Height (cm)");
        layout4scene1.setPadding(new Insets(5,5,5,5));
        layout4scene1.getChildren().addAll(weightField, lengthField, widthField, heightField);
        layout4scene1.setAlignment(Pos.CENTER);
        layout4scene1.setVisible(false);

        isEnvelope.setOnAction(e -> {
            if (isEnvelope.isSelected()) layout4scene1.setVisible(false);
            else layout4scene1.setVisible(true);
        });

        Button btnGo = new Button("Go!");
        box1.setOnAction(e -> {
            if (box1.getValue().equals(box2.getValue())) btnGo.setDisable(true);
            else btnGo.setDisable(false);
        });
        box2.setOnAction(e -> {
            if (box1.getValue().equals(box2.getValue())) btnGo.setDisable(true);
            else btnGo.setDisable(false);
        });

        btnGo.setOnAction(e ->{
            try {
                if (isEnvelope.isSelected()) {
                    new Shipping(box1.getValue(), box2.getValue(), box3.getValue(), data);
                    PaddingWindows.displayAddedParcel("Parcel has been successfully added!", data.getAllParcels().get(data.getAllParcels().size()-1).getTrackingNumber(), data.getAllParcels().get(data.getAllParcels().size()-1).getPrice());
                }
                else {
                    try {
                        new Shipping(box1.getValue(), box2.getValue(), box3.getValue(), Integer.parseInt(weightField.getText()), Integer.parseInt(lengthField.getText()), Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()), data);
                        PaddingWindows.displayAddedParcel("Parcel has been successfully added!", data.getAllParcels().get(data.getAllParcels().size()-1).getTrackingNumber(), data.getAllParcels().get(data.getAllParcels().size()-1).getPrice());
                    } catch (NumberFormatException ex) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Error");
                        a.setContentText("Type a number");
                        a.showAndWait();
                    }
                }
            } catch (IOException ex) {}
            catch (MyException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle(ex.getMessage());
                a.setContentText("Available parameters:\nMax weight is 150 kg\nMax length is 300 cm");
                a.showAndWait();
            }
        });


        VBox layoutScene1 = new VBox(10);
        layoutScene1.setPadding(new Insets(5,5,5,5));
        layoutScene1.getChildren().addAll(topMenu, layout1scene1, layout2scene1, layout3scene1, layout4scene1, btnGo);
        layoutScene1.setAlignment(Pos.TOP_CENTER);
        scene1 = new Scene(layoutScene1, 450, 300);

        /*
         * Tracking
         */
        TextField numberInput = new TextField();
        numberInput.setPromptText("Enter tracking number");
        Button btnTrackUp = new Button("Find");

        btnToMainScene2 = new Button("Home");

        HBox layout1scene2 = new HBox(10);
        layout1scene2.setPadding(new Insets(5,5,5,5));
        layout1scene2.getChildren().add(btnToMainScene2);
        layout1scene2.setAlignment(Pos.BASELINE_LEFT);

        HBox layout2scene2 = new HBox(10);
        layout2scene2.setPadding(new Insets(5,5,5,5));
        layout2scene2.getChildren().addAll(numberInput, btnTrackUp);
        layout2scene2.setAlignment(Pos.CENTER);

        HBox layout3scene2 = new HBox(10);
        layout3scene2.setPadding(new Insets(5,5,5,5));
        TextArea trackingField = new TextArea();
        trackingField.setEditable(false);
        layout3scene2.getChildren().add(trackingField);
        layout3scene2.setAlignment(Pos.CENTER);

        btnToMainScene2.setOnAction(e -> {
            window.setScene(mainScene);
            trackingField.clear();
        });
        btnTrackUp.setOnAction(e -> {
            try {
                data.arrival();
            } catch (IOException ex) {}
            catch (ParseException ex) {}

            try {
                trackingField.clear();
                try {
                    Parcel parcel = data.findParcel(Integer.parseInt(numberInput.getText()));
                    trackingField.appendText(parcel.getTrackingNumber() + "\n\n");
                    trackingField.appendText(parcel.getTypeOfParcel().getName() + " from " + data.getCities().get(parcel.getCodeOfDpt()-1).getCity().getName() + " to ");
                    trackingField.appendText(data.getCities().get(parcel.getCodeOfDst()-1).getCity().getName() + "\n");
                    trackingField.appendText("Delivery " + parcel.getTypeOfDelivery() + "\n");
                    trackingField.appendText("Price: €" + String.format("%.2f", parcel.getPrice()) + "\n\n");
                    for (String status : parcel.getStatusList()) trackingField.appendText(status + "\n");
                }
                catch (NullPointerException ex) {
                    trackingField.appendText("Not found\n");
                }
            }
            catch (NumberFormatException ex) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setContentText("Type a number");
                a.showAndWait();
            }
        });

        VBox layoutScene2 = new VBox(10);
        layoutScene2.setPadding(new Insets(5,5,5,5));
        layoutScene2.getChildren().addAll(layout1scene2, layout2scene2, layout3scene2);
        layoutScene2.setAlignment(Pos.TOP_CENTER);
        scene2 = new Scene(layoutScene2, 450, 300);


        /*
         * Table
         */

        parcelTableButton.setOnAction(e -> {
            try {
                data.arrival();
            } catch (IOException ex) {}
            catch (ParseException ex) {}
            PaddingWindows.displayParcelTable(data.getAllParcels(), data.getCities());
        });

        /*
         * Transport Table
         */
        transportTableButton.setOnAction(e -> {
            try {
                data.arrival();
            } catch (IOException ex) {}
            catch (ParseException ex) {}
            try {
                PaddingWindows.displayTransportTable(data.getTransports(), data.getCities(), data);
            } catch (Exception ex) {}
        });



        window.setScene(mainScene);
        window.show();
    }


    private void fillChoiceBox(ChoiceBox<String> choiceBox) {
        choiceBox.getItems().addAll("Athens", "Berlin", "Helsinki", "Istanbul", "Kiev");
        choiceBox.getItems().addAll("Lisbon", "London", "Madrid", "Minsk", "Moscow");
        choiceBox.getItems().addAll("Oslo", "Paris", "Prague", "Rome", "Stockholm");
        choiceBox.getItems().addAll("Vienna", "Warsaw");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
