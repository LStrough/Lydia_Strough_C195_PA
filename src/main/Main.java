package main;

import DAO.CustomerDao;
import DAO.CustomerDaoImpl;
import DAO.JDBC;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        stage.setTitle("Appointment Management System");
        stage.setScene(new Scene(root, 1200, 542));
        stage.show();
    }

    public static void main(String[] args) {
        //Locale.setDefault(new Locale("fr"));

        /*
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
         */

        CustomerDao customerDao = new CustomerDaoImpl();
        System.out.println(customerDao.getAllCustomers());
        System.out.println(customerDao.getCustomer(1));
        System.out.println(customerDao.getCustomer(2));
    }
}
