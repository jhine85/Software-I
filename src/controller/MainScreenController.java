package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreenController implements Initializable {

    /**
     * Part selected in the table view.
     */

    private static Part partToModify;

    /**
     * Product selected in the table view.
     */


    private static Product productToModify;

    /**
     * Text field for part search.
     */


    @FXML
    private TextField partSearchText;

    /**
     * Table view for part table.
     */


    @FXML
    private TableView<Part> partTableView;

    /**
     * ID column for part table.
     */


    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    /**
     * Part name column for part table.
     */


    @FXML
    private TableColumn<Part, String> partNameColumn;

    /**
     * Inventory column for part table.
     */


    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    /**
     * Price column for part table.
     */


    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    /**
     * Table text field for product search.
     */


    @FXML
    private TextField productSearchText;


    @FXML
    private TableView<Product> productTableView;

    /**
     * ID column for product table.
     */

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    /**
     * Name column for product table.
     */

    @FXML
    private TableColumn<Product, String> productNameColumn;

    /**
     * Inventory column for product table.
     */


    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;

    /**
     * Price column for product table.
     */


    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    /**
     * Selects part in part table.
     * @return A part, null if no part is selected.
     */


    public static Part getPartToModify() {
        return partToModify;
    }

    /**
     * Gets product selected in product table.
     *
     * @return A product, null if no product is selected.
     */

    public static Product getProductToModify() {
        return productToModify;
    }

    /**
     * Exits program.
     *
     * @param event Exit button action event.
     */

    @FXML
    void exitButtonAction(ActionEvent event) {

        System.exit(0);
    }

    /**
     * Loads AddPartController.
     * @param event Add button event.
     * @throws IOException From FXMLLoader.
     */

    @FXML
    void partAddAction(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("../view/AddPartScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes part selected in  part table.
     * @param event Part delete button event.
     */


    @FXML
    void partDeleteAction(ActionEvent event) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayAlert(3);
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(selectedPart);
            }
        }
    }

/**
 * Loads ModifyPartController.
 * Shows error message if no part is selected.
 * @param event Part modify button event.
 * @throws IOException From FXMLLoader.
 */



    @FXML
    void partModifyAction(ActionEvent event) throws IOException {

        partToModify = partTableView.getSelectionModel().getSelectedItem();

        //Example of correcting a runtime error by preventing null from being passed
        // to the ModifyPartController.
        if (partToModify == null) {
            displayAlert(3);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/ModifyPartScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Starts a search based on value in parts search text field.
     * @param event Part search button event.
     */

    @FXML
    void partSearchBtnAction(ActionEvent event) {

        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        String searchString = partSearchText.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().contains(searchString)) {
                partsFound.add(part);
            }
        }

        partTableView.setItems(partsFound);

        if (partsFound.size() == 0) {
            displayAlert(1);
        }
    }

    /**
     * Updates part table view to show all parts.
     * @param event Parts search text field key is pressed.
     */

    @FXML
    void partSearchTextKeyPressed(KeyEvent event) {

        if (partSearchText.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }

    }

    /**
     * Loads AddProductController
     * @param event Add product button event.
     * @throws IOException From FXMLLoader.
     */


    @FXML
    void productAddAction(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("../view/AddProductScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes selected product in product table.
     * @param event Product delete button event.
     */

    @FXML
    void productDeleteAction(ActionEvent event) {

        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            displayAlert(4);
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                ObservableList<Part> assocParts = selectedProduct.getAllAssociatedParts();

                if (assocParts.size() >= 1) {
                    displayAlert(5);
                } else {
                    Inventory.deleteProduct(selectedProduct);
                }
            }
        }
    }

    /**
     * Loads ModifyProductController.
     * @param event Product modify button event.
     * @throws IOException From FXMLLoader.
     */

    @FXML
    void productModifyAction(ActionEvent event) throws IOException {

        productToModify = productTableView.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            displayAlert(4);
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/ModifyProductScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Starts a search based on value in products search text field.
     * @param event Part search button event.
     */

    @FXML
    void productSearchBtnAction(ActionEvent event) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        String searchString = productSearchText.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(searchString) ||
                    product.getName().contains(searchString)) {
                productsFound.add(product);
            }
        }

        productTableView.setItems(productsFound);

        if (productsFound.size() == 0) {
            displayAlert(2);
        }
    }

    /**
     * Updates product table view to show all products.
     * @param event Products search text field key pressed.
     */

    @FXML
    void productSearchTextKeyPressed(KeyEvent event) {

        if (productSearchText.getText().isEmpty()) {
            productTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * Prompts alert messages.
     *
     * @param alertType Selects alert type.
     */

    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Alert alertError = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Information");
                alert.setHeaderText("Part not found");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Information");
                alert.setHeaderText("Product not found");
                alert.showAndWait();
                break;
            case 3:
                alertError.setTitle("Error");
                alertError.setHeaderText("Part not selected");
                alertError.showAndWait();
                break;
            case 4:
                alertError.setTitle("Error");
                alertError.setHeaderText("Product not selected");
                alertError.showAndWait();
                break;
            case 5:
                alertError.setTitle("Error");
                alertError.setHeaderText("Parts Associated");
                alertError.setContentText("All parts must be removed from product before deletion.");
                alertError.showAndWait();
                break;
        }
    }

    /**
     * Initializes controller and populates table views.
     * @param location The location used to resolve relative paths for the root object, or null if the location is unknown.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Populate parts table view
        partTableView.setItems(Inventory.getAllParts());
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Populate products table view
        productTableView.setItems(Inventory.getAllProducts());
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
