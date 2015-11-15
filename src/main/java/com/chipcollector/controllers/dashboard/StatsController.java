package com.chipcollector.controllers.dashboard;

import com.chipcollector.SpringFxmlLoader;
import com.chipcollector.data.AppSettings;
import com.chipcollector.data.Collection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class StatsController implements Initializable{

    private Collection collection;
    private SpringFxmlLoader loader;

    @FXML
    private Text numPokerChipsValue;

    @FXML
    private Text numPokerChipsBoughtInLast7DaysValue;

    @FXML
    private Text numPokerChipsBoughtInLastMonthValue;

    @FXML
    private Text numDifferentCasinosValue;

    public static final String STATS_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/StatsDialog.fxml";

    @Autowired
    public StatsController(Collection collection, SpringFxmlLoader loader) {
        this.collection = collection;
        this.loader = loader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        numPokerChipsValue.setText(String.valueOf(collection.getPokerChipCount()));
        numPokerChipsBoughtInLast7DaysValue.setText(String.valueOf(collection.getPokerChipCountForLast7Days()));
        numPokerChipsBoughtInLastMonthValue.setText(String.valueOf(collection.getPokerChipCountForLastMonth()));
        numDifferentCasinosValue.setText(String.valueOf(collection.getNumDifferentCasinos()));

    }

    @FXML
    public void onViewAllButtonPressed() throws IOException {
        loader.<DashboardController>showDialog(DASHBOARD_FX_FILE_LOCATION, "All Poker Chips", o -> {});
    }

    @FXML
    public void onAddButtonPressed() throws IOException {
        loader.<SearchPokerChipDialogController>showDialog(POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION, "All Poker Chips", o -> {});
    }

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/Dashboard.fxml";
    public static final String POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/SearchPokerChipDialog.fxml";
}
