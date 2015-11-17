package com.chipcollector.controllers.dashboard;

import com.chipcollector.SpringFxmlLoader;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.scraper.ScraperEngine;
import com.google.common.base.Throwables;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.chipcollector.controllers.dashboard.DashboardController.POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION;
import static com.chipcollector.util.EventUtils.isKeyboardEnterPressed;
import static com.chipcollector.util.EventUtils.isMousePrimaryButtonPressed;
import static java.util.Objects.nonNull;

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
    private final SpringFxmlLoader springFxmlLoader;

    private CasinoBean currentlySelectedCasino;

    @Autowired
    public SearchPokerChipDialogController(@Qualifier("theMogh") ScraperEngine engine, SpringFxmlLoader springFxmlLoader) {
        this.engine = engine;
        this.springFxmlLoader = springFxmlLoader;
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

    public void onPokerChipElementClicked(MouseEvent event) {
        PokerChipBean selectedPokerChip = pokerChipsTableView.getSelectionModel().getSelectedItem();
        if (nonNull(selectedPokerChip)) {
            if (isMousePrimaryButtonPressed(event) && isMouseDoubleClicked(event)) {
                openNewPokerChipDialog(selectedPokerChip);
            }
        }
    }

    private void openNewPokerChipDialog(PokerChipBean selectedPokerChip) {
              springFxmlLoader.<PokerChipDialogController>showDialog(POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION,
                      "Add new Poker Chip", controller -> {
                          controller.setPokerChipBean(selectedPokerChip);
                          controller.update();
                      });
    }

    private boolean isMouseDoubleClicked(MouseEvent event) {
        return event.getClickCount() == 2;
    }
}
