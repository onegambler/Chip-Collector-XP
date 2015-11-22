package com.chipcollector.controllers.dashboard;

import com.chipcollector.SpringFxmlLoader;
import com.chipcollector.data.AppSettings;
import com.chipcollector.data.Collection;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.chipcollector.util.EventUtils.isMousePrimaryButtonPressed;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static javafx.scene.control.SelectionMode.SINGLE;

@Component
public class MainWindowController implements Initializable {


    private Collection collection;
    private AppSettings settings;
    private SpringFxmlLoader loader;

    private TableView<PokerChipBean> pokerChipsTable;

    @FXML
    private Pagination pagination;

    @FXML
    private TreeView<Object> casinoTreeView;

    @FXML
    private StatsController statsPaneController;

    @FXML
    public VBox statsPane;

    @Autowired
    public MainWindowController(Collection collection, AppSettings configuration, SpringFxmlLoader loader) {
        this.collection = collection;
        this.settings = configuration;
        this.loader = loader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTablePagination();
        setUpPokerChipTable();
        setUpCasinoTreeView();
        loadDatabase();
        statsPaneController.registerViewAllAction(event -> showPagination());
    }

    private void showPagination() {
        statsPane.setVisible(false);
        pagination.setVisible(true);
    }

    private void showStatsPane() {
        statsPane.setVisible(true);
        pagination.setVisible(false);
    }

    private void loadDatabase() {
        settings.getLastUsedDatabase().ifPresent(s ->
        {
            //collection.load();
        });
    }

    private void setUpCasinoTreeView() {
        casinoTreeView.getSelectionModel().setSelectionMode(SINGLE);
        populateCasinoTreeView();
    }

    private void setUpTablePagination() {
        pagination.setPageFactory(this::getPokerChipTablePage);

        int pokerChipCount = collection.getPokerChipCount();
        int numPages = (int) max(1, ceil(pokerChipCount / settings.getPaginationSize())); //we want at least a page

        pagination.setPageCount(numPages);
    }

    private void setUpPokerChipTable() {
        pokerChipsTable = loader.load(TABLE_VIEW_FX_FILE_LOCATION);
        pokerChipsTable.setItems(FXCollections.observableArrayList());
    }

    private void populateCasinoTreeView() {
        List<Casino> allCasinos = collection.getAllCasinos();
        casinoTreeView.setRoot(new CasinoTreeRoot(allCasinos));
    }

    private Node getPokerChipTablePage(int pageIndex) {
        pokerChipsTable.getItems().clear();
        List<PokerChip> pagedPokerChips = collection.getPagedPokerChips(pageIndex, settings.getPaginationSize());
        pagedPokerChips.stream()
                .map(PokerChipBean::new)
                .forEach(pokerChipsTable.getItems()::add);

        return pokerChipsTable;
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        if (isMousePrimaryButtonPressed(event)) {
            TreeItem<Object> selectedItem = casinoTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getValue() instanceof Casino) {
                collection.setCasinoFilter((Casino) selectedItem.getValue());
                setUpTablePagination();
            }
        }
    }

    @FXML
    public void showSearchPokerChipDialog(ActionEvent actionEvent) throws IOException {
        showDialog("Search PokerChip", POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION);
    }

    @FXML
    public void showAddPokerChipDialog(ActionEvent actionEvent) {
        showDialog("Add PokerChip", POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION);
    }

    public void quitApplication(ActionEvent actionEvent) {
        System.exit(0);
    }

    private void showDialog(String title, String fxmlLocation) {
        final BorderPane dialog = loader.load(fxmlLocation);

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        //dialogStage.initOwner(); //TODO: init the owner
        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    public static final String TABLE_VIEW_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipsTableView.fxml";
    public static final String POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/SearchPokerChipDialog.fxml";

    public static final String POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";

    public void onAllPokerChipsTabClicked(Event event) {
        final TitledPane source = (TitledPane) event.getSource();
        showPagination();
        source.setExpanded(true);
    }

    public void onDashboardTabSelected(Event event) {
        final TitledPane source = (TitledPane) event.getSource();
        showStatsPane();
        source.setExpanded(true);
    }


}
