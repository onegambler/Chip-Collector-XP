package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.AppConfiguration;
import com.chipcollector.data.Collection;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Math.ceil;
import static java.lang.Math.max;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.input.MouseButton.PRIMARY;

public class DashboardController implements Initializable {

    @Setter
    private Collection collection;
    private TableView<PokerChipBean> pokerChipsTable;

    @FXML
    private Pagination pagination;

    @FXML
    private TreeView<Object> casinoTreeView;

    private ResourceBundle resources;
    private AppConfiguration configuration;

    public void setConfiguration(AppConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        this.casinoTreeView.getSelectionModel().setSelectionMode(SINGLE);
    }

    public void loadComponentsData() {
        setUpTablePagination();
        setUpPokerChipTable(resources);
        setUpCasinoTreeView();
    }

    private void setUpCasinoTreeView() {
        List<Casino> allCasinos = collection.getAllCasinos();
        casinoTreeView.setRoot(new CasinoTreeRoot(allCasinos));
    }

    private void setUpTablePagination() {
        pagination.setPageFactory(this::getPokerChipTablePage);

        int pokerChipCount = collection.getPokerChipCount();
        int numPages = (int) max(1, ceil(pokerChipCount / configuration.getPaginationSize())); //we want at least a page

        pagination.setPageCount(numPages);
    }

    private void setUpPokerChipTable(ResourceBundle resources) {
        try {
            pokerChipsTable = new FXMLLoader(Resources.getResource(TABLE_VIEW_FX_FILE_LOCATION), resources).load();
            pokerChipsTable.setItems(FXCollections.observableArrayList());
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    private Node getPokerChipTablePage(int pageIndex) {
        pokerChipsTable.getItems().clear();
        List<PokerChip> pagedPokerChips = collection.getPagedPokerChips(pageIndex, configuration.getPaginationSize());
        pagedPokerChips.stream()
                .map(PokerChipBean::new)
                .forEach(pokerChipsTable.getItems()::add);

        return pokerChipsTable;
    }

    @FXML
    public void onMouseClick(MouseEvent event) {
        if (event.getButton() == PRIMARY) {
            TreeItem<Object> selectedItem = casinoTreeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.getValue() instanceof Casino) {
                collection.setCasinoFilter((Casino) selectedItem.getValue());
                setUpTablePagination();
            }
        }
    }

    @FXML
    public void showSearchPokerChipDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(Resources.getResource(POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION), resources);
        final BorderPane searchPokerChipDialog = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Search PokerChip");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        //dialogStage.initOwner(); //TODO: init the owner
        Scene scene = new Scene(searchPokerChipDialog);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();
    }

    public static final String TABLE_VIEW_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipsTableView.fxml";
    public static final String POKER_CHIP_SEARCH_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/SearchPokerChipDialog.fxml";
}
