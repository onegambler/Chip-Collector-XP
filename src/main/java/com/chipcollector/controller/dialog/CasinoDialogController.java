package com.chipcollector.controller.dialog;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.Country;
import com.chipcollector.model.dashboard.CasinoBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import static com.chipcollector.util.EventUtils.getWindow;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.scene.control.ContentDisplay.LEFT;

@Controller
public class CasinoDialogController implements Initializable {

    @FXML
    private TextField casinoName;
    @FXML
    private TextField casinoCity;
    @FXML
    private TextField casinoState;
    @FXML
    private ComboBox<Country> casinoCountry;
    @FXML
    private ComboBox<String> casinoTheme;
    @FXML
    private ComboBox<String> casinoType;
    @FXML
    private TextField opened;
    @FXML
    private TextField closed;

    private CasinoBean casinoBean;
    private PokerChipCollection collection;

    @Autowired
    public CasinoDialogController(PokerChipCollection collection) {
        this.collection = collection;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        casinoCountry.setCellFactory(param -> new ImageListCell());
        casinoCountry.setItems(observableArrayList(collection.getAllCountries()));
    }

    public void onOkClicked(ActionEvent actionEvent) {
        casinoBean.setCity(casinoCity.getText());
        casinoBean.setCountry(casinoCountry.getValue());
        casinoBean.setClosedDate(closed.getText());
        casinoBean.setOpenDate(opened.getText());
        casinoBean.setName(casinoName.getText());
        casinoBean.setState(casinoState.getText());
        casinoBean.setType(casinoType.getEditor().getText());
        casinoBean.setTheme(casinoTheme.getEditor().getText());
        closeDialog(actionEvent);
    }

    public void onCancelClicked(ActionEvent actionEvent) {
        closeDialog(actionEvent);
    }

    private void closeDialog(ActionEvent actionEvent) {
        getWindow(actionEvent).hide();
    }

    public void setCasinoBean(CasinoBean casinoBean) {
        this.casinoBean = casinoBean;
        populateUI(casinoBean);
    }

    private void populateUI(CasinoBean casinoBean) {
        casinoName.setText(casinoBean.getName());
        casinoCity.setText(casinoBean.getCity());
        casinoCountry.setValue(casinoBean.getCountry());
        casinoState.setText(casinoBean.getState());
        casinoTheme.getEditor().setText(casinoBean.getTheme());
        casinoType.getEditor().setText(casinoBean.getType());
        opened.setText(casinoBean.getOpenDate());
        closed.setText(casinoBean.getClosedDate());
    }

    private class ImageListCell extends ListCell<Country> {
        private final ImageView view;

        private ImageListCell() {
            setContentDisplay(LEFT);
            view = new ImageView();
        }

        @Override
        protected void updateItem(Country item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
                setText(null);
            } else {
                view.setImage(item.getFlagImage());
                setGraphic(view);
                setText(item.getName());
            }
        }

    }
}
