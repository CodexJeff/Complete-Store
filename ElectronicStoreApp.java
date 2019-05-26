import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;

public class ElectronicStoreApp extends Application {
    private int aSale = 1;
    private double price, revenue;
    private ElectronicStore model = ElectronicStore.createStore();
    public void start(Stage primaryStage) {

        Pane aPane = new Pane();

        Label summaryLabel = new Label("Store Summary:");
        summaryLabel.relocate(50, 10);
        summaryLabel.setPrefSize(100, 30);

        Label salesLabel = new Label("# Sales:");
        salesLabel.relocate(30, 40);
        salesLabel.setPrefSize(100, 20);

        Label revenueLabel = new Label("Revenue:");
        revenueLabel.relocate(20, 70);
        revenueLabel.setPrefSize(100, 20);

        Label saleLabel = new Label("$ / Sale:");
        saleLabel.relocate(30, 100);
        saleLabel.setPrefSize(100, 20);

        Label popularItemsLabel = new Label("Most Popular Items:");
        popularItemsLabel.relocate(40, 130);
        popularItemsLabel.setPrefSize(125, 20);

        Label storeStockLabel = new Label("Store Stock:");
        storeStockLabel.relocate(310, 10);
        storeStockLabel.setPrefSize(100, 30);

        Label currentCartLabel = new Label("Current Cart ($0.00):");
        currentCartLabel.relocate(580, 10);
        currentCartLabel.setPrefSize(200, 30);

        TextField salesField = new TextField();
        salesField.setText("0");
        salesField.relocate(90, 40);
        salesField.setPrefSize(100, 20);

        TextField revenueField = new TextField();
        revenueField.setText("0.00");
        revenueField.relocate(90, 70);
        revenueField.setPrefSize(100, 20);

        TextField saleField = new TextField();
        saleField.setText("N/A");
        saleField.relocate(90, 100);
        saleField.setPrefSize(100, 20);

        ListView<String> popularItemField = new ListView<String>();
        popularItemField.relocate(10, 160);
        popularItemField.setPrefSize(180, 170);

        ListView<Product> storeStockField = new ListView<Product>();
        storeStockField.setItems(FXCollections.observableArrayList(model.getList()));
        storeStockField.relocate(200, 40);
        storeStockField.setPrefSize(290, 290);

        ListView<String> currentCartField = new ListView<String>();
        currentCartField.relocate(500, 40);
        currentCartField.setPrefSize(290, 290);

        Button resetButton = new Button("Reset Store");
        resetButton.relocate(30, 340);
        resetButton.setPrefSize(145, 50);

        Button addButton = new Button("Add to Cart");
        addButton.setDisable(true);
        addButton.relocate(275, 340);
        addButton.setPrefSize(140, 50);

        Button removeButton = new Button("Remove from Cart");
        removeButton.setDisable(true);
        removeButton.relocate(500, 340);
        removeButton.setPrefSize(140, 50);

        Button completeSaleButton = new Button("Complete Sale");
        completeSaleButton.setDisable(true);
        completeSaleButton.relocate(650, 340);
        completeSaleButton.setPrefSize(140, 50);

        aPane.getChildren().addAll(summaryLabel, salesLabel, revenueLabel, saleLabel, popularItemsLabel, storeStockLabel, currentCartLabel, salesField, revenueField, saleField, popularItemField, storeStockField, currentCartField, resetButton, addButton, removeButton, completeSaleButton);
        primaryStage.setTitle("Electronic Store Application - " + model.getName());
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane, 800, 400));
        primaryStage.show();

        storeStockField.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                addButton.setDisable(false);
            }
        });

        addButton.setOnAction(new EventHandler<ActionEvent>() {
            private int a = model.getList().length;
            public void handle(ActionEvent actionEvent) {
                int index = storeStockField.getSelectionModel().getSelectedIndex();
                if (model.stock[index].getQuantity() != 0) {
                    currentCartField.getItems().add(String.valueOf(model.stock[index]));
                    model.stock[index].sellUnits(1);
                    price = price + model.stock[index].getPrice();
                    currentCartLabel.setText("Current Cart ($" + String.format("%.2f", price) + "):");
                    completeSaleButton.setDisable(false);
                }
                if (model.stock[index].getQuantity() == 0){
                    for (int i = index; i < model.getList().length - 1 ; i++ ) {
                        model.stock[i] = model.stock[index + 1];
                        index++;
                    }
                    a = a - 1;
                    storeStockField.setItems(FXCollections.observableArrayList(model.getList()));
                    storeStockField.getItems().remove(a, model.getList().length);
                    storeStockField.setItems(FXCollections.observableArrayList(storeStockField.getItems()));
                }
            }
        });

        currentCartField.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                removeButton.setDisable(false);
            }
        });

        completeSaleButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                completeSaleButton.setDisable(true);
                revenue = price + revenue;
                salesField.setText("" + aSale);
                saleField.setText("" + String.format("%.2f", revenue / aSale));
                currentCartField.getItems().clear();
                revenueField.setText("" + String.format("%.2f", revenue));
                price = 0;
                currentCartLabel.setText("Current Cart ($0.00):");
                aSale++;
                for (int i = 0; i < model.getList().length; i++){
                    for (int j = 1; j < 10; j++) {
                        if (model.stock[i].getQuantity() == j && popularItemField.getItems().size() < 3 && !(popularItemField.getItems().contains(String.valueOf(model.stock[i])))) {
                            popularItemField.getItems().add(String.valueOf(model.stock[i]));
                        }
                    }
                }
            }
        });

        removeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                int index = currentCartField.getSelectionModel().getSelectedIndex();
                int index2 = storeStockField.getSelectionModel().getSelectedIndex();
                if (currentCartField.getItems().size() > 0) {
                    currentCartField.getItems().remove(index);
                    model.stock[index2].sellUnits2(1);
                    System.out.println(model.stock[index2].getQuantity());
                }
                if (currentCartField.getItems().size() <= 0) {
                    removeButton.setDisable(true);
                }
            }
        });

        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent __) {
                primaryStage.close();
                Platform.runLater(new Runnable() {
                    public void run() {
                        new ElectronicStoreApp().start(new Stage());
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}