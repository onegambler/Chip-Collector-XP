package com.chipcollector.controllers.dashboard;

import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

@Controller
public class PokerChipDialogController implements Initializable {

    private final ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    private ComboBox<String> denomComboBox;

    @FXML
    private TextField issueTextField;

    @FXML
    private Label casinoContent;

    @FXML
    private ImageView casinoCountryFlag;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

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

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/%s";
}
