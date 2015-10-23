package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.ScraperEngine;
import com.chipcollector.scraper.TheMoghScraperEngine;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.ENTER;

public class SearchDialogController implements Initializable {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox<String> searchSourceCombobox;

    @FXML
    private TableView<CasinoBean> casinosTableView;

    @FXML
    private TableView<PokerChipBean> pokerChipsTableView;

    private ScraperEngine engine;

    public void setPokerChipDao(PokerChipDAO pokerChipDao) {
        engine = new TheMoghScraperEngine(pokerChipDao);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onSearchButtonPressed() throws IOException {
        searchCasino();
    }

    @FXML
    public void onCasinoElementClicked() throws IOException {
        //TODO: do some UI loading stuff
        CasinoBean selectedCasino = casinosTableView.getSelectionModel().getSelectedItem();
        if (selectedCasino != null) {
            List<PokerChipBean> pokerChips = engine.searchPokerChip(selectedCasino);
            pokerChipsTableView.setItems(FXCollections.observableArrayList(pokerChips));
        }
    }

    private void searchCasino() throws IOException {
        List<CasinoBean> casinoBeanList = engine.searchCasinos(searchTextField.getText());
        casinosTableView.setItems(FXCollections.observableArrayList(casinoBeanList));
    }

    @FXML
    public void onKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode() == ENTER) {
            searchCasino();
        }
    }
}
