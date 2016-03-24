package com.chipcollector.controller.dashboard;

import com.chipcollector.controller.dialog.PokerChipDialogController;
import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.chipcollector.util.EventUtils.getWindow;
import static java.util.Objects.nonNull;

@Component
public class DashboardController implements Initializable {

    private PokerChipCollection pokerChipCollection;
    private SpringFxmlLoader loader;

    @FXML
    private Text numPokerChipsValue;

    @FXML
    private Text numPokerChipsBoughtInLast7DaysValue;

    @FXML
    private Text numPokerChipsBoughtInLastMonthValue;

    @FXML
    private Text numDifferentCasinosValue;

    private Consumer<Event> viewAllActionConsumer;

    @Autowired
    public DashboardController(PokerChipCollection pokerChipCollection, SpringFxmlLoader loader) {
        this.pokerChipCollection = pokerChipCollection;
        this.loader = loader;
    }

    public void registerViewAllAction(Consumer<Event> eventConsumer) {
        viewAllActionConsumer = eventConsumer;
    }

    public void updateWindow() {
        numPokerChipsValue.setText(String.valueOf(pokerChipCollection.getAllPokerChipsCount()));
        numPokerChipsBoughtInLast7DaysValue.setText(String.valueOf(pokerChipCollection.getPokerChipCountForLast7Days()));
        numPokerChipsBoughtInLastMonthValue.setText(String.valueOf(pokerChipCollection.getPokerChipCountForLastMonth()));
        numDifferentCasinosValue.setText(String.valueOf(pokerChipCollection.getAllCasinosCount()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateWindow();
    }

    @FXML
    public void onViewAllButtonPressed(Event event) throws IOException {
        if (nonNull(viewAllActionConsumer)) {
            viewAllActionConsumer.accept(event);
        }
    }

    @FXML
    public void onSearchAddButtonPressed(Event event) {
        openSearchPokerChipDialog(getWindow(event));
    }

    @FXML
    public void onManualAddMenuItemPressed(ActionEvent actionEvent) {
        final Window window = ((MenuItem) actionEvent.getSource()).getParentPopup().getOwnerWindow();
        loader.<PokerChipDialogController>showDialog(POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION,
                "All Poker Chips", window, true, controller -> controller.setPokerChipBean(new PokerChipBean()));
    }

    @FXML
    public void onSearchAddMenuItemPressed(ActionEvent actionEvent) {
        final MenuItem source = (MenuItem) actionEvent.getSource();
        openSearchPokerChipDialog(source.getParentPopup().getOwnerWindow());
    }

    private void openSearchPokerChipDialog(Window window) {
        loader.showDialog(POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION, "All Poker Chips", window, true);
    }

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/Dashboard.fxml";
    public static final String POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dialog/PokerChipDialog.fxml";
    public static final String POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dialog/SearchPokerChipDialog.fxml";
}
