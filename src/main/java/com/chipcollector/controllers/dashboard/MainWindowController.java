package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.ApplicationProperties;
import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.spring.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.chipcollector.util.EventUtils.isDoubleClick;
import static com.chipcollector.util.EventUtils.isMousePrimaryButtonPressed;
import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javafx.scene.control.SelectionMode.SINGLE;

@Component
public class MainWindowController implements Initializable {

    private PokerChipCollection pokerChipCollection;
    private ApplicationProperties settings;
    private SpringFxmlLoader loader;

    @FXML
    private DashboardController dashboardController;
    @FXML
    private TableView<PokerChipBean> pokerChipsTable;
    @FXML
    private Pagination pokerChipsPaginatedTable;
    @FXML
    private TreeView<Object> casinoTreeView;
    @FXML
    private VBox dashboard;
    @FXML
    private TitledPane dashboardTitledPane;
    @FXML
    private TitledPane showAllPokerChipTitledPane;

    @Autowired
    public MainWindowController(PokerChipCollection pokerChipCollection, ApplicationProperties configuration, SpringFxmlLoader loader) {
        this.pokerChipCollection = pokerChipCollection;
        this.settings = configuration;
        this.loader = loader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTablePagination();
        setUpPokerChipTable();
        setUpCasinoTreeView();
        loadDatabase();
        dashboardController.registerViewAllAction(event -> showAllPokerChipsPane());
        pokerChipCollection.addUpdateListener(this::updateWindow);
        showDashboardPane();
    }

    private void updateWindow() {
        populateCasinoTreeView();
        setUpTablePagination();
        dashboardController.updateWindow();

    }

    private void loadDatabase() {
        settings.getLastUsedDatabase().ifPresent(s ->
        {
            //pokerChipCollection.load();
        });
    }

    private void setUpCasinoTreeView() {
        casinoTreeView.getSelectionModel().setSelectionMode(SINGLE);
        populateCasinoTreeView();
    }

    private void setUpTablePagination() {
        pokerChipsPaginatedTable.setPageFactory(this::getPokerChipTablePage);

        int pokerChipCount = pokerChipCollection.getFilteredPokerChipCount();
        int numPages = (int) max(1, ceil(pokerChipCount / settings.getPaginationSize())); //we want at least a page

        pokerChipsPaginatedTable.setPageCount(numPages);
    }

    private void setUpPokerChipTable() {
        pokerChipsTable = loader.load(TABLE_VIEW_FX_FILE_LOCATION);
        pokerChipsTable.setItems(FXCollections.observableArrayList());
        pokerChipsTable.setOnMouseClicked(this::onPokerChipTableElementClicked);
    }

    private void populateCasinoTreeView() {
        List<Casino> allCasinos = pokerChipCollection.getAllCasinos();
        casinoTreeView.setRoot(new CasinoTreeRoot(allCasinos));
    }

    private Node getPokerChipTablePage(int pageIndex) {
        pokerChipsTable.getItems().clear();
        List<PokerChip> pagedPokerChips = pokerChipCollection.getPagedPokerChips(pageIndex, settings.getPaginationSize());
        pagedPokerChips.stream()
                .map(PokerChipBean::new)
                .forEach(pokerChipsTable.getItems()::add);

        return pokerChipsTable;
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        if (isMousePrimaryButtonPressed(event)) {
            TreeItem<Object> selectedItem = casinoTreeView.getSelectionModel().getSelectedItem();
            if (nonNull(selectedItem)) {
                if (isNull(selectedItem.getParent())) {
                    pokerChipCollection.resetCasinoFilter();
                    setUpTablePagination();
                } else if (selectedItem.isLeaf()) {
                    pokerChipCollection.setCasinoFilter((Casino) selectedItem.getValue());
                    setUpTablePagination();
                }
            }
        }
    }

    @FXML
    public void showSearchPokerChipDialog(Event event) throws IOException {
        Window parent = ((Node) event.getSource()).getScene().getWindow();
        loader.showDialog(POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION, "Search PokerChip", true, parent);
    }

    @FXML
    public void onShowAddPokerChipDialog(Event event) {
        Window parent = ((Node) event.getSource()).getScene().getWindow();
        showAddPokerChipDialog(parent, null);
    }

    public void onPokerChipTableElementClicked(MouseEvent event) {
        if (isMousePrimaryButtonPressed(event) && isDoubleClick(event)) {
            Window parent = ((Node) event.getSource()).getScene().getWindow();
            final PokerChipBean selectedItem = pokerChipsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                showAddPokerChipDialog(parent, selectedItem);
            }
        }
    }

    public void showAddPokerChipDialog(Window parent, PokerChipBean pokerChipBean) {
        loader.<PokerChipDialogController>showDialog(POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION,
                "Add PokerChip",
                parent,
                false, controller -> controller.setPokerChipBean(pokerChipBean));
    }

    public void quitApplication() {
        System.exit(0);
    }

    public void showAllPokerChipsPane() {
        dashboard.setVisible(false);
        pokerChipsPaginatedTable.setVisible(true);
        showAllPokerChipTitledPane.setExpanded(true);
    }

    public void showDashboardPane() {
        dashboard.setVisible(true);
        pokerChipsPaginatedTable.setVisible(false);
        dashboardTitledPane.setExpanded(true);
    }

    public static final String TABLE_VIEW_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipsTableView.fxml";
    public static final String POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/SearchPokerChipDialog.fxml";

    public static final String POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
}
