package com.chipcollector.controllers.dashboard;

import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.ScraperEngine;
import com.chipcollector.util.EventUtils;
import com.sun.javafx.event.EventUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.chipcollector.util.EventUtils.isKeyboardEnterPressed;
import static com.chipcollector.util.EventUtils.isMousePrimaryButtonPressed;
import static java.util.Objects.nonNull;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.MouseButton.PRIMARY;

@Controller
public class SearchPokerChipDialogController implements Initializable {

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchSourceCombobox;

    @FXML
    private TableView<CasinoBean> casinosTableView;

    @FXML
    private TableView<PokerChipBean> pokerChipsTableView;

    //TODO: selezione sulla base della combobox
    private ScraperEngine engine;

    private CasinoBean currentlySelectedCasino;

    @Autowired
    public SearchPokerChipDialogController(@Qualifier("theMogh") ScraperEngine engine) {
        this.engine = engine;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onSearchButtonPressed() throws IOException {
        searchCasino();
    }

    @FXML
    public void onCasinoElementClicked(MouseEvent event) throws IOException {
        if (isMousePrimaryButtonPressed(event)) {
            searchPokerChip();
        }
    }

    public void onCasinoTableKeyPressed(KeyEvent event) throws IOException {
        if (isKeyboardEnterPressed(event)) {
            searchPokerChip();
        }
    }

    private void searchPokerChip() throws IOException {
        CasinoBean selectedCasino = casinosTableView.getSelectionModel().getSelectedItem();
        if (nonNull(selectedCasino) && !selectedCasino.equals(currentlySelectedCasino)) {
            currentlySelectedCasino = selectedCasino;
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
        if (isKeyboardEnterPressed(event)) {
            searchCasino();
        }
    }
}
