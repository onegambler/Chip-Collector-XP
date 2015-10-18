package com.chipcollector.controller.main;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.main.PokerChipBean;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private static final int PAGE_SIZE = 200;

    private ResourceBundle resources;
    private PokerChipDAO pokerChipDAO;


    private TableView<PokerChipBean> pokerChipsTable;

    @FXML
    private Pagination pagination;

    public DashboardController(PokerChipDAO pokerChipDAO) {
        this.pokerChipDAO = pokerChipDAO;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        pagination.setPageFactory(this::createPage);
        int numPages = (int) Math.ceil(pokerChipDAO.getPokerChipCount() / PAGE_SIZE);
        pagination.setPageCount(numPages);
        pokerChipsTable = createTable();
        pokerChipsTable.setItems(FXCollections.observableArrayList());
    }

    public Node createPage(int pageIndex) {
        pokerChipsTable.getItems().clear();
        List<PokerChip> pagedPokerChips = pokerChipDAO.getPagedPokerChips(pageIndex, PAGE_SIZE);
        pagedPokerChips.stream()
                .map(PokerChipBean::new)
                .forEach(pokerChipsTable.getItems()::add);

        return pokerChipsTable;
    }

    //TODO: cambiare
    private TableView<PokerChipBean> createTable() {

        if (pokerChipsTable == null) {
            try {
                URL url = new File("C:\\Users\\PC\\IdeaProjects\\Chip Collector XP\\src\\main\\java\\com\\chipcollector\\view\\main\\TableView.fxml").toURI().toURL();
                pokerChipsTable = new FXMLLoader(url, resources).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pokerChipsTable;
    }

}
