package com.chipcollector.controllers.dashboard;

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
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.input.MouseButton.PRIMARY;

public class DashboardController implements Initializable {

    private static final int PAGE_SIZE = 200;

    @Setter
    private Collection collection;
    private TableView<PokerChipBean> pokerChipsTable;

    @FXML
    private Pagination pagination;

    @FXML
    private TreeView<Object> casinoTreeView;

    private ResourceBundle resources;

    public DashboardController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
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
        int numPages = (int) Math.ceil(collection.getPokerChipCount() / PAGE_SIZE);
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
        List<PokerChip> pagedPokerChips = collection.getPagedPokerChips(pageIndex, PAGE_SIZE);
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

    public static final String TABLE_VIEW_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipsTableView.fxml";
}
