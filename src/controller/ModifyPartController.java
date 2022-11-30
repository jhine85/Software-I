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


public class ModifyPartController implements Initializable {

    /**
     * Part selected in MainScreenController.
     */


    private Part selectedPart;

    /**
     * The machine ID/name for part.
     */


    @FXML
    private Label partIdNameLabel;

    /**
     * In-house radio button.
     */


    @FXML
    private RadioButton inHouseRadioButton;

    /**
     * Toggle group for the radio buttons.
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
     * Inventory level text field.
     */


    @FXML
    private TextField partInventoryText;

    /**
     * Part price text field.
     */


    @FXML
    private TextField partPriceText;

    /**
     * Maximum part text field.
     */


    @FXML
    private TextField partMaxText;

    /**
     * The machine ID/name text field.
     */


    @FXML
    private TextField partIdNameText;

    /**
     * The minimum part text field.
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
     * Sets machine ID/name label to "Company Name".
     * @param event Outsourced radio nutton.
     */

    @FXML
    void outsourcedRadioButtonAction(ActionEvent event) {

        partIdNameLabel.setText("Company Name");
    }

    /**
     * Loads MainScreenController.
     * @param event Save button action.
     * @throws IOException From FXMLLoader.
     */

    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {

        try {
            int id = selectedPart.getId();
            String name = partNameText.getText();
            Double price = Double.parseDouble(partPriceText.getText());
            int stock = Integer.parseInt(partInventoryText.getText());
            int min = Integer.parseInt(partMinText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int machineId;
            String companyName;
            boolean partAddSuccessful = false;

            if (minValid(min, max) && inventoryValid(min, max, stock)) {

                if (inHouseRadioButton.isSelected()) {
                    try {
                        machineId = Integer.parseInt(partIdNameText.getText());
                        InHouse newInHousePart = new InHouse(id, name, price, stock, min, max, machineId);
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
                    Inventory.addPart(newOutsourcedPart);
                    partAddSuccessful = true;
                }

                if (partAddSuccessful) {
                    Inventory.deletePart(selectedPart);
                    returnToMainScreen(event);
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
     * @param min The minimum amount for the part.
     * @param max The maximum amount for the part.
     * @return Boolean indicating if minimum is valid.
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
     * Checks inventory level (x) is min<=x<=max.
     * @param min The minimum amount for part.
     * @param max The maximum amount for part.
     * @param stock The inventory amount for part.
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
     * Shows alerts
     * @param alertType Selects alert type.
     */


    private void displayAlert(int alertType) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        switch (alertType) {
            case 1:
                alert.setTitle("Error");
                alert.setHeaderText("Error Modifying Part");
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
                alert.setContentText("Inventory must be a number equal to or between Min and Max");
                alert.showAndWait();
                break;
        }
    }

    /**
     * Initializes controllers and generates text fields with part selected in MainScreenController.
     * @param location The location used to resolve relative paths for the root object, or null if the location is unknown.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */

    @Override
    public void initialize (URL location, ResourceBundle resources) {

        selectedPart = MainScreenController.getPartToModify();

        if (selectedPart instanceof InHouse) {
            inHouseRadioButton.setSelected(true);
            partIdNameLabel.setText("Machine ID");
            partIdNameText.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
        }

        if (selectedPart instanceof Outsourced){
            outsourcedRadioButton.setSelected(true);
            partIdNameLabel.setText("Company Name");
            partIdNameText.setText(((Outsourced) selectedPart).getCompanyName());
        }

        partIdText.setText(String.valueOf(selectedPart.getId()));
        partNameText.setText(selectedPart.getName());
        partInventoryText.setText(String.valueOf(selectedPart.getStock()));
        partPriceText.setText(String.valueOf(selectedPart.getPrice()));
        partMaxText.setText(String.valueOf(selectedPart.getMax()));
        partMinText.setText(String.valueOf(selectedPart.getMin()));
    }
}
