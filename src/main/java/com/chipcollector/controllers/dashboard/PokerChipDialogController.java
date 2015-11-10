package com.chipcollector.controllers.dashboard;

import com.chipcollector.domain.Casino;
import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class PokerChipDialogController  implements Initializable {

    @FXML
    public Label casinoContent;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    @FXML
    private ComboBox<Casino> casinoComboBox;

    private PokerChipBean pokerChipBean;

    public PokerChipBean pokerChipBean() {
        return pokerChipBean;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onCancelAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getTarget()).getScene().getWindow().hide();
    }
}
