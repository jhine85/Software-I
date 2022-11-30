//javadoc page located in C482 PA file in javadocs folder


package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;


public class Main extends Application {

    /**
     * Start - begins to load the program
     * @param primaryStage
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The main method creates sample data and launches the program.
     * @param args
     * Sample parts need to be added in the section below
     */

    public static void main(String[] args) {

        //Add sample parts below this line
        int partId = Inventory.getNewPartId();
        InHouse keyBoard = new InHouse(partId,"Keyboard", 30.00, 25, 10, 40,
                101);
        partId = Inventory.getNewPartId();
        InHouse mOuse = new InHouse(partId,"Mouse", 10.00, 35, 1, 40,
                102);
        partId = Inventory.getNewPartId();
        InHouse headSet = new InHouse(partId,"Headset", 40.00, 20, 15, 40,
                103);
        partId = Inventory.getNewPartId();
        Outsourced microPhone = new Outsourced(partId, "Microphone",15.00, 55, 10,
                100, "Generic");
        Inventory.addPart(keyBoard);
        Inventory.addPart(mOuse);
        Inventory.addPart(headSet);
        Inventory.addPart(microPhone);

        //Add sample products below this line(not just parts)
        int productId = Inventory.getNewProductId();
        Product gamingSetup = new Product(productId, "Gaming Setup", 95.00, 50, 20,
                100);
        gamingSetup.addAssociatedPart(keyBoard);
        gamingSetup.addAssociatedPart(mOuse);
        gamingSetup.addAssociatedPart(headSet);
        gamingSetup.addAssociatedPart(microPhone);
        Inventory.addProduct(gamingSetup);

        launch(args);
    }
}