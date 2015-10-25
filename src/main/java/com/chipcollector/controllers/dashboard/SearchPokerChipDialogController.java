package com.chipcollector.controllers.dashboard;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.ScraperEngine;
import com.chipcollector.scraper.TheMoghScraperEngine;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.util.Objects.nonNull;
import static javafx.scene.input.KeyCode.ENTER;

public class SearchPokerChipDialogController implements Initializable {

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchSourceCombobox;

    @FXML
    private TableView<CasinoBean> casinosTableView;

    @FXML
    private TableView<PokerChipBean> pokerChipsTableView;

    private ScraperEngine engine;

    private CasinoBean currentCasino;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //TODO: selezione sulla base della combobox
        engine = new TheMoghScraperEngine();
        //pokerChipsTableView.setFixedCellSize(100);
    }

    @FXML
    public void onSearchButtonPressed() throws IOException {
        searchCasino();
    }

    @FXML
    public void onCasinoElementClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            searchPokerChip();
        }
    }

    public void onCasinoTableKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            searchPokerChip();
        }
    }

    private void searchPokerChip() throws IOException {
        CasinoBean selectedCasino = casinosTableView.getSelectionModel().getSelectedItem();
        if (nonNull(selectedCasino) && !selectedCasino.equals(currentCasino)) {
            currentCasino = selectedCasino;
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
