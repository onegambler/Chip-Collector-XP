package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipDAO;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private static final int PAGE_SIZE = 200;

    private PokerChipDAO pokerChipDAO;
    private TableView<PokerChipBean> pokerChipsTable;

    @FXML
    private Pagination pagination;

    public DashboardController(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpTablePagination();
        setUpPokerChipTable(resources);
    }

    private void setUpTablePagination() {
        pagination.setPageFactory(this::getPokerChipTablePage);
        int numPages = (int) Math.ceil(pokerChipDAO.getPokerChipCount() / PAGE_SIZE);
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

    public Node getPokerChipTablePage(int pageIndex) {
        pokerChipsTable.getItems().clear();
        List<PokerChip> pagedPokerChips = pokerChipDAO.getPagedPokerChips(pageIndex, PAGE_SIZE);
        pagedPokerChips.stream()
                .map(PokerChipBean::new)
                .forEach(pokerChipsTable.getItems()::add);

        return pokerChipsTable;
    }

    public static final String TABLE_VIEW_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipsTableView.fxml";
}
