package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.Collection;
import com.chipcollector.domain.Casino;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Location;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Setter;
import org.controlsfx.validation.ValidationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.util.Optional.ofNullable;
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
    @FXML
    private ImageView frontImage;
    @FXML
    private ImageView backImage;
    @Setter
    private PokerChipBean pokerChipBean;

    private Collection collection;

    @Autowired
    public PokerChipDialogController(Collection collection) {
        this.collection = collection;
    }

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
        ofNullable(pokerChipBean.getValue()).map(BigDecimal::toString).ifPresent(valueTextField::setText);
        ofNullable(pokerChipBean.getPaid()).map(BigDecimal::toString).ifPresent(paidTextField::setText);
        dateOfAcquisitionDatePicker.setValue(pokerChipBean.getDateOfAcquisition());
        tcrTextField.setText(pokerChipBean.getTcrId());
        notesTextArea.setText(pokerChipBean.getNotes());
        obsoleteToggleButton.setSelected(pokerChipBean.isObsolete());
        cancelledToggleButton.setSelected(pokerChipBean.isCancelled());
        denomComboBox.setValue(pokerChipBean.getDenom());
        issueTextField.setText(pokerChipBean.getIssue());
        casinoContent.setText(pokerChipBean.getCasinoBean().toString());
        frontImage.setImage(pokerChipBean.getFrontImage());
        backImage.setImage(pokerChipBean.getBackImage());
        Country country = collection.getCountryFromName(pokerChipBean.getCasinoBean().getCountry());
        if (country != null) {
            URL imageUrl = Resources.getResource(String.format(IMAGES_FLAGS_LOCATION, country.getFlagImageName()));
            try {
                casinoCountryFlag.setImage(new Image(imageUrl.openStream()));
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }

    public void onCancelAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getTarget()).getScene().getWindow().hide();
    }

    public void onOkAction(MouseEvent event) {
        final CasinoBean casinoBean = pokerChipBean.getCasinoBean();
        final Optional<Casino> existingCasino = collection.getCasinoFromCasinoBean(casinoBean);
        Casino casino;
        if (!existingCasino.isPresent()) {
            Optional<Location> existingLocation = collection.getLocationFromCasinoBean(casinoBean);
            Location location;
            if (!existingLocation.isPresent()) {
                Country country = collection.getCountryFromCasinoBean(casinoBean);
                location = Location.builder()
                        .city(casinoBean.getCity())
                        .country(country)
                        .build();
            } else {
                location = existingLocation.get();
            }

            casino = Casino.builder()
                    .closeDate(casinoBean.getClosedDate())
                    .openDate(casinoBean.getOpenDate())
                    .name(casinoBean.getName())
                    .type(casinoBean.getType())
                    .status(casinoBean.getStatus())
                    .website(casinoBean.getWebsite())
                    .location(location)
                    .build();
        } else {
            casino = existingCasino.get();
        }

        PokerChip pokerChip = PokerChip.builder()
                .category(categoryComboBox.getValue())
                .acquisitionDate(dateOfAcquisitionDatePicker.getValue())
                //.amountPaid(paidTextField.getText())
                //.
                .casino(casino)
                .build();

        collection.add(pokerChip);
    }

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/%s";

    private static final String[] RARITY_VALUES = new String[]{"R-1", "R-2", "R-3", "R-4", "R-5", "R-6", "R-7", "R-8", "R-9", "R-10", "Unknown"};
}
