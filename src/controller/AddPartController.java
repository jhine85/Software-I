package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddPartController implements Initializable {

    /**
     * ID/name label for part.
     */

    @FXML
    private Label partIdNameLabel;

    /**
     * In-house radio button.
     */

    @FXML
    private RadioButton inHouseRadioButton;

    /**
     *Toggle group for the radio buttons.
     */

    @FXML
    private ToggleGroup togglePartType;

    /**
     * Outsourced radio button.
     */


    @FXML
    private RadioButton outsourcedRadioButton;

    /**
     * Part ID text field.
     */


    @FXML
    private TextField partIdText;

    /**
     * Part name text field.
     */


    @FXML
    private TextField partNameText;

    /**
     * Part inventory text field.
     */


    @FXML
    private TextField partInventoryText;

    /**
     * Part price text field.
     */


    @FXML
    private TextField partPriceText;

    /**
     * Part maximum inventory text field.
     */


    @FXML
    private TextField partMaxText;

    /**
     * The machine ID/name text field for the part.
     */


    @FXML
    private TextField partIdNameText;

    /**
     * Part minimum level text field.
     */


    @FXML
    private TextField partMinText;

    /**
     * Loads MainScreenController.
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
     * Sets machine ID/name to "Machine ID".
     * @param event In-house radio button event.
     */


    @FXML
    void inHouseRadioButtonAction(ActionEvent event) {

        partIdNameLabel.setText("Machine ID");
    }

    /**
     * Sets machine ID/name to "Company Name".
     * @param event Outsourced radio button event.
     */

    @FXML
    void outsourcedRadioButtonAction(ActionEvent event) {

        partIdNameLabel.setText("Company Name");
    }

    /**
     * Adds new part and loads MainScreenController.
     * @param event Save button event.
     * @throws IOException From FXMLLoader.
     */

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {

        try {
            int id = 0;
            String name = partNameText.getText();
            Double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

            if (name.isEmpty()) {
                displayAlert(5);
            } else {
                if (minValid(min, max) && inventoryValid(min, max, stock)) {

                    if (inHouseRadioButton.isSelected()) {
                        try {
                            machineId = Integer.parseInt(partIdNameText.getText());
                            InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
                            newInHousePart.setId(Inventory.getNewPartId());
                            Inventory.addPart(newInHousePart);
                            partAddSuccessful = true;
                        } catch (Exception e) {
                            displayAlert(2);
                        }
                    }

                    if (outsourcedRadioButton.isSelected()) {
                        companyName = partIdNameText.getText();
                        Outsourced newOutsourcedPart = new Outsourced(id, name, price, stock, min, max,
                                companyName);
                        newOutsourcedPart.setId(Inventory.getNewPartId());
                        Inventory.addPart(newOutsourcedPart);
                        partAddSuccessful = true;
                    }

                    if (partAddSuccessful) {
                        returnToMainScreen(event);
                    }
                }
            }
        } catch(Exception e) {
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
     * Validates that inventory level (x) is min>=x<=max.
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
     * Prompts alerts.
     * @param alertType Selects alert type.
     */


    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Adding Part");
                alert.setContentText("Form contains blank fields or invalid values.");
                alert.showAndWait();
                break;
            case 2:
                alert.setTitle("Error");
                alert.setHeaderText("Invalid value for Machine ID");
                alert.setContentText("Machine ID may only contain numbers.");
                alert.showAndWait();
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
                alert.setContentText("Inventory must be a number equal to or between Min and Max.");
                alert.showAndWait();
                break;
            case 5:
                alert.setTitle("Error");
                alert.setHeaderText("Name Empty");
                alert.setContentText("Name cannot be empty.");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Initializes controller and sets in-house radio button to true.
     * @param location The location used to resolve relative paths for the root object, or null if the location is unknown.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        inHouseRadioButton.setSelected(true);
    }
}
