/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bench;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Muhammad
 */
public class FXMLDocumentController implements Initializable {

    List<TransactionalData> transactionalDataList;

    @FXML
    private Label totalPageLabel;

    @FXML
    private Label totalCountLabel;

    @FXML
    private Label TotalBalanceLabel;

    @FXML
    private TableView<Transaction> vendorNamesTable;

    @FXML
    private TableColumn<Transaction, String> vendorNamesTableCol;

    @FXML
    private TableView<Transaction> transcationTableView;

    @FXML
    private TableColumn<Transaction, String> companyTableCol;

    @FXML
    private TableColumn<Transaction, String> dataTableCol;

    @FXML
    private TableColumn<Transaction, String> ledgerTableCol;

    @FXML
    private TableColumn<Transaction, Double> amountTableCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            transactionalDataList = new ArrayList<>();
            getJsonData();
            addFillDetailTab();
            getVenderName();
            getTranscation();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //get the total balance 
    public Double getTotalBalance() {
        Double total = new Double(0);
        for (TransactionalData transactionalData : transactionalDataList) {
            for (Transaction transactions : transactionalData.getTransactions()) {
                total = total + transactions.getAmount();
            }
        }
        return total;
    }

    //show the detail on screen
    private void addFillDetailTab() {
        totalPageLabel.setText(Integer.toString(transactionalDataList.get(transactionalDataList.size() - 1).getPage()));
        totalCountLabel.setText(Integer.toString(transactionalDataList.get(transactionalDataList.size() - 1).getTotalCount()));
        TotalBalanceLabel.setText(Double.toString(getTotalBalance()));
    }

    //remove the unwant text from vender names and show them in a table
    public void getVenderName() {
        ArrayList<Transaction> trans = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        for (TransactionalData transactionalData : transactionalDataList) {
            for (Transaction transaction : transactionalData.getTransactions()) {
                String company = transaction.getCompany().replaceAll("[^a-zA-Z ]", "");
                transaction.setCompany(company.replaceAll("xx", ""));
                if (!temp.contains(transaction.getCompany())) {
                    trans.add(transaction);
                    temp.add(transaction.getCompany());
                }
            }
        }

        ObservableList<Transaction> data
                = FXCollections.observableArrayList(trans);
        vendorNamesTableCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        vendorNamesTable.setItems(data);
    }

    //remove duplicate transcations and show them in table
    public void getTranscation() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        for (TransactionalData transactionalData : transactionalDataList) {
            for (Transaction transaction : transactionalData.getTransactions()) {
                boolean similar = false;
                //to check transcation exist or not
                for (Transaction transaction1 : transactions) {
                    if (transaction1.getDate().equalsIgnoreCase(transaction.getDate())
                            && transaction1.getCompany().equalsIgnoreCase(transaction.getCompany())) {
                        similar = true;
                        break;
                    }
                }
                if (!similar) {
                    transactions.add(transaction);
                }
            }
        }

        ObservableList<Transaction> data
                = FXCollections.observableArrayList(transactions);
        companyTableCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        amountTableCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dataTableCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        ledgerTableCol.setCellValueFactory(new PropertyValueFactory<>("ledger"));
        transcationTableView.setItems(data);

    }

//pARSE THE JSON DATA    
    public void parseJson(InputStream data) throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(data));

        String urlString = "";
        String current;
        while ((current = in.readLine()) != null) {
            urlString += current;
        }

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement root = parser.parse(urlString);
        TransactionalData returned = gson.fromJson(root, TransactionalData.class);
        transactionalDataList.add(returned);
    }

    //make contection AND GET DATA
    public void getJsonData() throws MalformedURLException, IOException {
        for (int i = 1; Boolean.TRUE; i++) {
            String sUrl = ("http://resttest.bench.co/transactions/" + i + ".json");
            URL url = new URL(sUrl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if (urlConnection instanceof HttpURLConnection) {
                connection = (HttpURLConnection) urlConnection;
            }

            if (connection.getResponseCode() == 200) {
                parseJson(connection.getInputStream());
            } else {
                break;
            }

        }

    }

}
