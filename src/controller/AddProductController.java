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


public class AddProductController implements Initializable {

    /**
     * List with associated parts.
     */


    private ObservableList<Part> assocParts = FXCollections.observableArrayList();

    /**
     * Associated parts table view.
     */


    @FXML
    private TableView<Part> assocPartTableView;

    /**
     * Part ID column for associated parts table.
     */


    @FXML
    private TableColumn<Part, Integer> assocPartIdColumn;

    /**
     * Part name column for associated parts table.
     */


    @FXML
    private TableColumn<Part, String> assocPartNameColumn;

    /**
     * Inventory amount column for associated parts table.
     */


    @FXML
    private TableColumn<Part, Integer> assocPartInventoryColumn;

    /**
     * Price column for associated parts table.
     */


    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;

    /**
     * Shows all parts.
     */


    @FXML
    private TableView<Part> partTableView;

    /**
     * Part ID column for all parts table.
     */


    @FXML
    private TableColumn<Part, Integer> partIdColumn;

    /**
     * Name column for all parts table.
     */


    @FXML
    private TableColumn<Part, String> partNameColumn;

    /**
     * Inventory amount column for all parts table.
     */


    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;

    /**
     * Price column for all parts table.
     */


    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    /**
     * Part search text field.
     */


    @FXML
    private TextField partSearchText;

    /**
     * Product ID text field.
     */


    @FXML
    private TextField productIdText;

    /**
     * Product name text field.
     */


    @FXML
    private TextField productNameText;

    /**
     * Product inventory level text field.
     */


    @FXML
    private TextField productInventoryText;

    /**
     * Product price text field.
     */


    @FXML
    private TextField productPriceText;

    /**
     * Product maximum level text field.
     */


    @FXML
    private TextField productMaxText;

    /**
     * Product minimum level text field.
     */


    @FXML
    private TextField productMinText;

    /**
     * Adds part selected in the all parts table to associated parts table.
     * Shows error message if no part is selected.
     * @param event Add button event.
     */


    @FXML
    void addButtonAction(ActionEvent event) {

        Part selectedPart = partTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayAlert(5);
        } else {
            assocParts.add(selectedPart);
            assocPartTableView.setItems(assocParts);
        }
    }

    /**
     * Displays confirmation dialog and loads MainScreenController.
     * @param event Cancel button event.
     * @throws IOException From FXMLLoader.
     */


    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            returnToMainScreen(event);
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
     * @param event Parts search text field key pressed.
     */


    @FXML
    void partSearchKeyPressed(KeyEvent event) {

        if (partSearchText.getText().isEmpty()) {
            partTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * @param event Remove button action.
     */


    @FXML
    void removeButtonAction(ActionEvent event) {

        Part selectedPart = assocPartTableView.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            displayAlert(5);
        } else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTableView.setItems(assocParts);
            }
        }
    }

    /**
     * Adds new product to inventory and loads MainScreenController.
     * @param event Save button action.
     * @throws IOException From FXMLLoader.
     */


    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {

        try {
            int id = 0;
            String name = productNameText.getText();
            Double price = Double.parseDouble(productPriceText.getText());
            int stock = Integer.parseInt(productInventoryText.getText());
            int min = Integer.parseInt(productMinText.getText());
            int max = Integer.parseInt(productMaxText.getText());

            if (name.isEmpty()) {
                displayAlert(7);
            } else {
                if (minValid(min, max) && inventoryValid(min, max, stock)) {

                    Product newProduct = new Product(id, name, price, stock, min, max);

                    for (Part part : assocParts) {
                        newProduct.addAssociatedPart(part);
                    }

                    newProduct.setId(Inventory.getNewProductId());
                    Inventory.addProduct(newProduct);
                    returnToMainScreen(event);
                }
            }
        } catch (Exception e){
            displayAlert(1);
        }
    }

    /**
     * Loads MainScreenController.
     * @param event Passed from parent method.
     * @throws IOException From FXMLLoader.
     */


    private void returnToMainScreen(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validates that min>0 and min<max.
     *
     * @param min The minimum price for the part.
     * @param max The maximum price for the part.
     * @return Boolean indicating if min is valid.
     */


    private boolean minValid(int min, int max) {

        boolean isValid = true;

        if (min <= 0 || min >= max) {
            isValid = false;
            displayAlert(3);
        }

        return isValid;
    }

    /**
     * Validates that inventory level is equal too or between min and max.
     *
     * @param min The minimum price for the part.
     * @param max The maximum price for the part.
     * @param stock The inventory amount for the part.
     * @return Boolean indicating if inventory is valid.
     */


    private boolean inventoryValid(int min, int max, int stock) {

        boolean isValid = true;

        if (stock < min || stock > max) {
            isValid = false;
            displayAlert(4);
        }

        return isValid;
    }

    /**
     * Shows alerts.
     * @param alertType Selects alert type.
     */


    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Product");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alertInfo.setTitle("Information");
                alertInfo.setHeaderText("Part not found");
                alertInfo.showAndWait();
                break;
            case 3:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Min");
                alert.setContentText("Min must be a number greater than 0 and less than Max.");
                alert.showAndWait();
                break;
            case 4:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Inventory");
                alert.setContentText("Inventory must be a number equal to or between Min and Max");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Part not selected");
                alert.showAndWait();
                break;
            case 7:
                alert.setTitle("Error");
                alert.setHeaderText("Name Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Initializes controller & creates table view.
     * @param location The location used to resolve relative paths for the root object, or null if the location is unknown.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.setItems(Inventory.getAllParts());

        assocPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
