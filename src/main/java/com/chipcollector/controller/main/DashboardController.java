package com.chipcollector.controller.main;

import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.*;
import com.chipcollector.model.main.PokerChipBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    private ResourceBundle resources;
    private PokerChipDAO pokerChipDAO;

    private static final int PAGE_SIZE = 100;

    @FXML
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
    }

    public Node createPage(int pageIndex) {

        TableView<PokerChipBean> pokerChipsTable = createTable();
        ObservableList<PokerChipBean> observablePokerChipList = FXCollections.observableArrayList();

        pokerChipDAO.getPagedPokerChips(pageIndex, PAGE_SIZE)
                .stream()
                .map(PokerChipBean::new)
                .forEach(observablePokerChipList::add);

        pokerChipsTable.setItems(observablePokerChipList);
        return new BorderPane(pokerChipsTable);
    }

    //TODO: cambiare
    private TableView<PokerChipBean> createTable() {

        if (pokerChipsTable == null) {
            try {
                URL url = new File("C:\\Users\\PC\\IdeaProjects\\Chip Collector XP\\src\\main\\java\\com\\chipcollector\\view\\main\\TableView.fxml").toURI().toURL();
                pokerChipsTable = new FXMLLoader(url).load();
                System.out.println();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pokerChipsTable;
    }

}
