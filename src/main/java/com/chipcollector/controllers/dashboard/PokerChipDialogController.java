package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.*;
import com.chipcollector.domain.Location.LocationBuilder;
import com.chipcollector.domain.PokerChip.PokerChipBuilder;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.util.ImageConverter;
import com.chipcollector.views.control.AutoCompleteComboBoxListener;
import com.chipcollector.views.control.moneyfield.MoneyField;
import com.chipcollector.views.control.moneyfield.MoneyFilter;
import com.chipcollector.views.control.moneyfield.MoneyStringConverter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.chipcollector.util.ImageConverter.bufferedImageToRawBytes;
import static com.chipcollector.util.ImageConverter.resizeImage;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;

@Controller
public class PokerChipDialogController implements Initializable {

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
    private ImageView frontImageView;
    @FXML
    private ImageView backImageView;

    private PokerChipBean pokerChipBean;

    private PokerChipCollection pokerChipCollection;

    @Autowired
    public PokerChipDialogController(PokerChipCollection pokerChipCollection) {
        this.pokerChipCollection = pokerChipCollection;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rarityComboBox.setItems(observableArrayList(RARITY_VALUES));
        categoryComboBox.setItems(observableArrayList(CATEGORY_VALUES));
        conditionComboBox.setItems(observableArrayList(CONDITION_VALUES));

        inlayComboBox.setItems(observableList(pokerChipCollection.getInlayAutocompleteValues()));
        colorComboBox.setItems(observableList(pokerChipCollection.getColorAutocompleteValues()));
        moldComboBox.setItems(observableList(pokerChipCollection.getMoldAutocompleteValues()));
        denomComboBox.setItems(observableList(pokerChipCollection.getDenomAutocompleteValues()));
        new AutoCompleteComboBoxListener<>(inlayComboBox);
        new AutoCompleteComboBoxListener<>(colorComboBox);
        new AutoCompleteComboBoxListener<>(moldComboBox);
        new AutoCompleteComboBoxListener<>(denomComboBox);

        paidTextField.setTextFormatter(new TextFormatter<>(new MoneyStringConverter(), null, new MoneyFilter()));
        valueTextField.setTextFormatter(new TextFormatter<>(new MoneyStringConverter(), null, new MoneyFilter()));
    }

    public void setPokerChipBean(PokerChipBean pokerChipBean) {
        this.pokerChipBean = pokerChipBean;
        fillUpFields(pokerChipBean);
    }

    private void fillUpFields(PokerChipBean pokerChipBean) {
        yearTextField.textProperty().set(pokerChipBean.yearProperty().get());
        cancelledToggleButton.selectedProperty().set(pokerChipBean.cancelledProperty().get());
        rarityComboBox.valueProperty().set(pokerChipBean.rarityProperty().get());
        colorComboBox.getEditor().textProperty().set(pokerChipBean.colorProperty().get());
        moldComboBox.getEditor().textProperty().set(pokerChipBean.moldProperty().get());
        insertsTextField.textProperty().set(pokerChipBean.insertsProperty().get());
        inlayComboBox.getEditor().textProperty().set(pokerChipBean.inlayProperty().get());
        conditionComboBox.valueProperty().set(pokerChipBean.conditionProperty().get());
        categoryComboBox.valueProperty().set(pokerChipBean.categoryProperty().get());
        dateOfAcquisitionDatePicker.valueProperty().set(pokerChipBean.dateOfAcquisitionProperty().get());
        tcrTextField.textProperty().set(pokerChipBean.tcrIdProperty().get());
        notesTextArea.textProperty().set(pokerChipBean.notesProperty().get());
        denomComboBox.getEditor().textProperty().set(pokerChipBean.denomProperty().get());
        obsoleteToggleButton.selectedProperty().set(pokerChipBean.obsoleteProperty().get());
        issueTextField.textProperty().set(pokerChipBean.issueProperty().get());
        casinoContent.setText(pokerChipBean.getCasinoBean().toString());
        frontImageView.imageProperty().set(pokerChipBean.getFrontImage().get());
        backImageView.imageProperty().set(pokerChipBean.getBackImage().get());
        casinoContent.setText(pokerChipBean.getCasinoBean().toString());
        //TODO:
        //ofNullable(pokerChipBean.getValue()).map(BigDecimal::toString).ifPresent(valueTextField::setText);
        //ofNullable(pokerChipBean.getPaid()).map(BigDecimal::toString).ifPresent(paidTextField::setText);
        Optional<Country> country = pokerChipCollection.getCountryFromName(pokerChipBean.getCasinoBean().getCountry());
        country.ifPresent(value -> {
            casinoCountryFlag.setImage(value.getFlagImage());
            Tooltip.install(casinoCountryFlag, new Tooltip(pokerChipBean.getCasinoBean().getCountry()));
        });
    }

    public void onCancelAction(ActionEvent actionEvent) {
        closeDialog(actionEvent);
    }

    private void closeDialog(Event event) {
        ((Node) event.getTarget()).getScene().getWindow().hide();
    }

    public void onOkAction(ActionEvent event) throws IOException {
        if (pokerChipBean.isNew()) {
            createNewPokerChip();
        } else {
            updateExistingPokerChip();
        }
        closeDialog(event);
    }

    private void updateExistingPokerChip() {
        updatePokerChipBeanFromUIProperties();
        pokerChipCollection.update(pokerChipBean.getPokerChip());
    }

    private void updatePokerChipBeanFromUIProperties() {
        pokerChipBean.yearProperty().set(yearTextField.textProperty().get());
        pokerChipBean.cancelledProperty().set(cancelledToggleButton.selectedProperty().get());
        pokerChipBean.rarityProperty().set(rarityComboBox.valueProperty().get());
        pokerChipBean.colorProperty().set(colorComboBox.getEditor().textProperty().get());
        pokerChipBean.moldProperty().set(moldComboBox.getEditor().textProperty().get());
        pokerChipBean.insertsProperty().set(insertsTextField.textProperty().get());
        pokerChipBean.inlayProperty().set(inlayComboBox.getEditor().textProperty().get());
        pokerChipBean.conditionProperty().set(conditionComboBox.valueProperty().get());
        pokerChipBean.categoryProperty().set(categoryComboBox.valueProperty().get());
        pokerChipBean.dateOfAcquisitionProperty().set(dateOfAcquisitionDatePicker.valueProperty().get());
        pokerChipBean.tcrIdProperty().set(tcrTextField.textProperty().get());
        pokerChipBean.notesProperty().set(notesTextArea.textProperty().get());
        pokerChipBean.denomProperty().set(denomComboBox.getEditor().textProperty().get());
        pokerChipBean.obsoleteProperty().set(obsoleteToggleButton.selectedProperty().get());
        pokerChipBean.issueProperty().set(issueTextField.textProperty().get());
        //TODO:casino
        pokerChipBean.getFrontImage().set(frontImageView.imageProperty().get());
        pokerChipBean.getBackImage().set(backImageView.imageProperty().get());
    }

    private void createNewPokerChip() throws IOException {
        final CasinoBean casinoBean = pokerChipBean.getCasinoBean();
        Casino casino = pokerChipCollection.getCasinoFromCasinoBean(casinoBean).orElseGet(() -> {
            Location location = pokerChipCollection.getLocationFromCasinoBean(casinoBean)
                    .orElseGet(() -> createNewLocation(casinoBean));
            return createNewCasino(casinoBean, location);
        });

        PokerChipBuilder pokerChipBuilder = PokerChip.builder()
                .category(categoryComboBox.getValue())
                .acquisitionDate(dateOfAcquisitionDatePicker.getValue())
                .color(colorComboBox.getValue())
                .condition(conditionComboBox.getValue())
                .denom(denomComboBox.getValue())
                .inserts(insertsTextField.getText())
                .mold(moldComboBox.getValue())
                .notes(notesTextArea.getText())
                .obsolete(obsoleteToggleButton.isSelected())
                .rarity(rarityComboBox.getValue())
                .tcrID(tcrTextField.getText())
                .year(yearTextField.getText())
                .issue(issueTextField.getText())
                //TODO: amounts
                //.amountPaid(paidTextField.getText())
                //.value()
                .casino(casino);

        Image frontImage = frontImageView.getImage();
        BlobImage frontBlobImage = null;
        if (frontImage != null) {
            frontBlobImage = getBlobImageFromImage(frontImage);
            pokerChipBuilder.frontImage(frontBlobImage);
        }

        Image backImage = backImageView.getImage();
        if (backImage != null) {
            if (frontImage == backImage) {
                pokerChipBuilder.backImage(frontBlobImage);
            } else {
                pokerChipBuilder.backImage(getBlobImageFromImage(backImage));
            }
        }
        pokerChipCollection.add(pokerChipBuilder.build());
    }

    private Casino createNewCasino(CasinoBean casinoBean, Location location) {
        return Casino.builder()
                .closeDate(casinoBean.getClosedDate())
                .openDate(casinoBean.getOpenDate())
                .name(casinoBean.getName())
                .type(casinoBean.getType())
                .status(casinoBean.getStatus())
                .website(casinoBean.getWebsite())
                .location(location)
                .build();
    }

    private Location createNewLocation(CasinoBean casinoBean) {
        Optional<Country> country = pokerChipCollection.getCountryFromCasinoBean(casinoBean);
        LocationBuilder locationBuilder = Location.builder()
                .city(casinoBean.getCity())
                .state(casinoBean.getState());
        country.ifPresent(locationBuilder::country);
        return locationBuilder.build();
    }

    private BlobImage getBlobImageFromImage(Image image) throws IOException {
        BufferedImage originalImage = ImageConverter.imageToBufferedImage(image);
        byte[] thumbnail = bufferedImageToRawBytes(resizeImage(originalImage, 90), PNG_FORMAT);
        byte[] resizedImage = bufferedImageToRawBytes(resizeImage(originalImage, 120), PNG_FORMAT);
        BlobImage blobImage = new BlobImage();
        blobImage.setImage(resizedImage);
        blobImage.setThumbnail(thumbnail);
        return blobImage;
    }

    public static final String PNG_FORMAT = "png";
    private static final String[] RARITY_VALUES = new String[]{"R-1", "R-2", "R-3", "R-4", "R-5", "R-6", "R-7", "R-8", "R-9", "R-10", "Unknown"};
    public static String[] CONDITION_VALUES = new String[]{"Uncirculated", "Slightly Used", "Average", "Well Used", "Poor", "Cancelled"};
    public static String[] CATEGORY_VALUES = new String[]{"Baccarat", "Error", "Faro", "Free Play", "Match Play", "No Cash Value", "No Denomination", "Non-Negotiable", "Poker", "Roulette", "Race and Sport"};

}
