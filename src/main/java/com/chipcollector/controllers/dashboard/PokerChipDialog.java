package com.chipcollector.controllers.dashboard;

import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PokerChipDialog {

    @FXML
    public Button cancelButton;
    @FXML
    public Button okButton;

    private PokerChipBean pokerChipBean;

    public PokerChipBean pokerChipBean() {
        return pokerChipBean;
    }
}
