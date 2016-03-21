package com.chipcollector.controllers.dashboard;

import com.chipcollector.model.dashboard.CasinoBean;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.scraper.ScraperEngine;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.MessagesHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.chipcollector.controllers.dashboard.MainWindowController.POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION;
import static com.chipcollector.scraper.ScraperEngine.DEFAULT_ENGINE;
import static com.chipcollector.util.EventUtils.isKeyboardEnterPressed;
import static com.chipcollector.util.EventUtils.isMousePrimaryButtonPressed;
import static java.util.Objects.nonNull;
import static javafx.collections.FXCollections.observableArrayList;

@Controller
public class SearchPokerChipDialogController implements Initializable {

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<ScraperEngine> searchSourceCombobox;

    @FXML
    private TableView<CasinoBean> casinosTableView;

    @FXML
    private TableView<PokerChipBean> pokerChipsTableView;

    private final List<ScraperEngine> engines;

    private SimpleObjectProperty<ScraperEngine> selectedEngine = new SimpleObjectProperty<>();

    private final SpringFxmlLoader springFxmlLoader;

    private CasinoBean currentlySelectedCasino;

    @Autowired
    public SearchPokerChipDialogController(List<ScraperEngine> engines, SpringFxmlLoader springFxmlLoader) {
        this.engines = engines;
        this.springFxmlLoader = springFxmlLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        casinosTableView.setPlaceholder(new Label());
        pokerChipsTableView.setPlaceholder(new Label());
        searchSourceCombobox.setItems(observableArrayList(engines));
        searchSourceCombobox.valueProperty().bindBidirectional(selectedEngine);
        searchSourceCombobox.setValue(engines.get(DEFAULT_ENGINE));
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
            List<PokerChipBean> pokerChips = selectedEngine.get().searchPokerChip(selectedCasino);
            pokerChipsTableView.setItems(observableArrayList(pokerChips));
        }
    }

    private void searchCasino() throws IOException {
        List<CasinoBean> casinoBeanList = selectedEngine.get().searchCasinos(searchTextField.getText());
        if (casinoBeanList.isEmpty()) {
            casinosTableView.setPlaceholder(new Label(MessagesHelper.getString("search.table.noresults")));
        }
        casinosTableView.setItems(observableArrayList(casinoBeanList));
    }

    @FXML
    public void onKeyPressed(KeyEvent event) throws IOException {
        if (isKeyboardEnterPressed(event)) {
            searchTextField.setDisable(true);
            searchCasino();
            searchTextField.setDisable(false);
        }
    }

    public void onPokerChipElementClicked(MouseEvent event) {
        PokerChipBean selectedPokerChip = pokerChipsTableView.getSelectionModel().getSelectedItem();
        if (nonNull(selectedPokerChip)) {
            if (isMousePrimaryButtonPressed(event) && isMouseDoubleClicked(event)) {
                openNewPokerChipDialog(((Node) event.getSource()).getScene().getWindow(), selectedPokerChip);
            }
        }
    }

    private void openNewPokerChipDialog(Window owner, PokerChipBean selectedPokerChip) {
        springFxmlLoader.<PokerChipDialogController>showDialog(POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION,
                "Add new Poker Chip", owner, false, controller -> controller.setPokerChipBean(selectedPokerChip));
    }

    private boolean isMouseDoubleClicked(MouseEvent event) {
        return event.getClickCount() == 2;
    }
}
