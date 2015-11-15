package com.chipcollector.controllers.dashboard;

import com.chipcollector.models.dashboard.PokerChipBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.controlsfx.validation.ValidationSupport;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.collections.FXCollections.observableArrayList;

@Controller
public class PokerChipDialogController implements Initializable {

    private final ValidationSupport validationSupport = new ValidationSupport();

    @FXML
    public TextField yearTextField;
    @FXML
    public ComboBox<String> colorComboBox;
    @FXML
    public ComboBox<String> moldComboBox;
    @FXML
    public ComboBox<String> inlayComboBox;
    @FXML
    public TextField insertsTextField;
    @FXML
    public ComboBox<String> conditionComboBox;
    @FXML
    public ComboBox<String> categoryComboBox;
    @FXML
    public ComboBox<String> rarityComboBox;
    @FXML
    public TextField valueTextField;
    @FXML
    public DatePicker dateOfAcquisitionDatePicker;
    @FXML
    public TextField tcrTextField;
    @FXML
    public TextArea notesTextArea;
    @FXML
    public ToggleButton obsoleteToggleButton;
    @FXML
    public ToggleButton cancelledToggleButton;
    @FXML
    public TextField paidTextField;
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
        rarityComboBox.setItems(observableArrayList(RARITY_VALUES));
        denomComboBox.setItems(null);
    }

    public void update() {
        cancelledToggleButton.setSelected(pokerChipBean.getCancelled().getValue());
        //TODO:
        //casinoContent.setText(pokerChipBean.getCasinoBean().);
        //casinoCountryFlag.setImage(pokerChipBean.getCasinoBean().getCountryFlag());
        rarityComboBox.setValue(pokerChipBean.getRarity());
        yearTextField.setText(pokerChipBean.getYear());
        colorComboBox.setValue(pokerChipBean.getColor());
        moldComboBox.setValue(pokerChipBean.getMold());
        inlayComboBox.setValue(pokerChipBean.getInlay());
        insertsTextField.setText(pokerChipBean.getInserts());
        conditionComboBox.setValue(pokerChipBean.getCondition());
        categoryComboBox.setValue(pokerChipBean.getCategory());
        valueTextField.setText(String.valueOf(pokerChipBean.getValue()));
        paidTextField.setText(String.valueOf(pokerChipBean.getPaid()));
        dateOfAcquisitionDatePicker.setValue(pokerChipBean.getDateOfAcquisition());
        tcrTextField.setText(pokerChipBean.getTcrId());
        notesTextArea.setText(pokerChipBean.getNotes());
        obsoleteToggleButton.setSelected(pokerChipBean.isObsolete());
        cancelledToggleButton.setSelected(pokerChipBean.isCancelled());
        denomComboBox.setValue(pokerChipBean.getDenom());
        issueTextField.setText(pokerChipBean.getIssue());
        casinoContent.setText(pokerChipBean.getCasinoBean().toString());

    }

    public void onCancelAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getTarget()).getScene().getWindow().hide();
    }

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/%s";

    private static final String[] RARITY_VALUES = new String[]{"R-1", "R-2", "R-3", "R-4", "R-5", "R-6", "R-7", "R-8", "R-9", "R-10", "Unknown"};
}
