package controller;

import DAO.*;
import utilities.ListManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {
    Stage stage;
    Parent scene;

    public TextField nameTxt, addressTxt, postalCodeTxt, phoneTxt;
    public ComboBox<Country> countryComboBx;
    public ComboBox<Division> divisionComboBx;
    public Label nameE, addressE, postalCodeE, phoneE, countryE, divisionE;

    Customer selCustomer = null;
    public int countryId, divisionId;
    public String customerName, address, postalCode, phone;

    public void updateCustomer(Customer selectedCustomer) {
        JDBC.openConnection();
        CountryDao countryDao = new CountryDaoImpl();
        selCustomer = selectedCustomer;

        nameTxt.setText(String.valueOf(selCustomer.getCustomerName()));
        addressTxt.setText(String.valueOf(selCustomer.getAddress()));
        postalCodeTxt.setText(String.valueOf(selCustomer.getPostalCode()));
        phoneTxt.setText(String.valueOf(selCustomer.getPhone()));
        countryComboBx.setItems(countryDao.getAllCountries());
        countryComboBx.getSelectionModel().select(selCustomer.getCountryId() - 1);
        countryId = selCustomer.getCountryId();
        divisionComboBx.setItems(ListManager.getFilteredDivisions(countryId));

        Division div = null;
        for(Division division : ListManager.getFilteredDivisions(countryId)) {
            if(division.getDivisionId() == selectedCustomer.getDivisionId()) {
                div = division;
                break;
            }
        }

        divisionComboBx.getSelectionModel().select(div);
        divisionId = selCustomer.getDivisionId();
    }

    public void onActionUpdateCustomer(ActionEvent actionEvent) {
        System.out.println("Save Button clicked!");

        try {
            CustomerDao customerDao = new CustomerDaoImpl();

            int customerId = selCustomer.getCustomerId();
            customerName = nameTxt.getText();
            address = addressTxt.getText();
            postalCode = postalCodeTxt.getText();
            phone = phoneTxt.getText();
            divisionId = divisionComboBx.getValue().getDivisionId();

            if(customerName.isBlank()) {
                exceptionMessage(1);
            }else if(address.isBlank()) {
                exceptionMessage(2);
            }else if (postalCode.isBlank()){
                exceptionMessage(3);
            }else if (phone.isBlank()){
                exceptionMessage(4);
            }else if (countryComboBx.getSelectionModel() == null){
                exceptionMessage(5);
            }else if (divisionComboBx.getSelectionModel() == null){
                exceptionMessage(6);
            }else {
                customerDao.updateCustomer(customerId, customerName, address, postalCode, phone, divisionId);

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainCustomers.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onActionReturnToCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Cancel Button clicked!");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel \"Update Customer\"");
        alert.setContentText("All changes will be forgotten, do you wish to continue?");
        alert.showAndWait();
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainCustomers.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    public void onActionSelectCountry(ActionEvent actionEvent) {
        countryId = countryComboBx.getValue().getCountryId();
        divisionComboBx.setItems(ListManager.getFilteredDivisions(countryId));
        divisionComboBx.getSelectionModel().selectFirst();
    }

    public void exceptionMessage(int exceptionNum){
        switch (exceptionNum) {
            case 1 -> {
                nameE.setText("Customer \"Name\" cannot be empty!");
            }
            case 2 -> {
                addressE.setText("Customer \"Address\" cannot be empty!");
            }
            case 3 -> {
                postalCodeE.setText("Customer \"Postal Code\" cannot be empty!");
            }
            case 4 -> {
                phoneE.setText("Customer \"Phone Number\" cannot be empty!");
            }
            case 5 -> {
                countryE.setText("You must select a \"Country\".");
            }
            case 6 -> {
                divisionE.setText("You must select a \"Division\".");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Update Customer: I am initialized!");
    }
}
