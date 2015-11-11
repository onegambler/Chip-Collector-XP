package com.chipcollector.controllers.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class PokerChipDialogController implements Initializable {

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/AD.png";
    @FXML
    private Label casinoContent;

    @FXML
    private ImageView casinoCountryFlag;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    @FXML
    private ComboBox<Casino> casinoComboBox;

    @Setter
    private PokerChipBean pokerChipBean;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void update() {
        casinoCountryFlag.setImage(pokerChipBean.getCasinoBean().getCountryFlag());
    }

    public void onCancelAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getTarget()).getScene().getWindow().hide();
    }
}
