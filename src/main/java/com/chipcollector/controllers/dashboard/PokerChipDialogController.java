package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.*;
import com.chipcollector.domain.Location.LocationBuilder;
import com.chipcollector.domain.PokerChip.PokerChipBuilder;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.util.ImageConverter;
import com.chipcollector.util.MessagesHelper;
import com.chipcollector.views.control.AutoCompleteComboBoxListener;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.chipcollector.util.EventUtils.isDoubleClick;
import static com.chipcollector.util.ImageConverter.bufferedImageToRawBytes;
import static com.chipcollector.util.ImageConverter.resizeImage;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Optional.ofNullable;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;
import static javafx.scene.control.Alert.AlertType.ERROR;

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
    public ComboBox<Rarity> rarityComboBox;
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
        rarityComboBox.setItems(observableArrayList(Rarity.values()));
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

        frontImageView.setOnMouseClicked(event -> chooseImage(event, image -> frontImageView.setImage(image)));
        backImageView.setOnMouseClicked(event -> chooseImage(event, image -> backImageView.setImage(image)));
    }

    private void chooseImage(MouseEvent event, Consumer<Image> imageSetter) {
        if (isDoubleClick(event)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(MessagesHelper.getString(IMAGE_FILE_CHOOSER_TITLE));
            final File file = fileChooser.showOpenDialog(((ImageView) event.getSource()).getScene().getWindow());
            if (Objects.nonNull(file)) {
                try {
                    imageSetter.accept(new Image(new FileInputStream(file)));
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(ERROR);
                    alert.setTitle(MessagesHelper.getString(IMAGE_ERROR_ALERT_TITLE));
                    alert.setHeaderText(MessagesHelper.getString(IMAGE_ERROR_ALERT_HEADER));
                    alert.setContentText(MessagesHelper.getString(IMAGE_ERROR_ALERT_CONTENT));
                    alert.showAndWait();
                }
            }
        }
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
        frontImageView.imageProperty().set(pokerChipBean.getFrontImageProperty().get());
        backImageView.imageProperty().set(pokerChipBean.getBackImageProperty().get());
        casinoContent.setText(pokerChipBean.getCasinoBean().toString());
        ofNullable(pokerChipBean.valueProperty().get()).map(MoneyAmount::toString).ifPresent(valueTextField::setText);
        ofNullable(pokerChipBean.paidProperty().get()).map(MoneyAmount::toString).ifPresent(paidTextField::setText);
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
        if (!isNullOrEmpty(paidTextField.getText())) {
            pokerChipBean.paidProperty().set(MoneyAmount.parse(paidTextField.getText()));
        }
        if (!isNullOrEmpty(valueTextField.getText())) {
            pokerChipBean.valueProperty().set(MoneyAmount.parse(valueTextField.getText()));
        }
        pokerChipBean.getFrontImageProperty().set(frontImageView.imageProperty().get());
        pokerChipBean.getBackImageProperty().set(backImageView.imageProperty().get());
    }

    private void createNewPokerChip() throws IOException {
        final CasinoBean casinoBean = pokerChipBean.getCasinoBean();
        Casino casino = pokerChipCollection.getCasinoFromCasinoBean(casinoBean).orElseGet(() -> {
            Location location = pokerChipCollection.getLocationFromCasinoBean(casinoBean)
                    .orElseGet(() -> createNewLocation(casinoBean));
            return createNewCasino(casinoBean, location);
        });

        PokerChipBuilder pokerChipBuilder = PokerChip.builder()
                .category(categoryComboBox.getEditor().getText())
                .acquisitionDate(dateOfAcquisitionDatePicker.getValue())
                .color(colorComboBox.getEditor().getText())
                .inlay(inlayComboBox.getEditor().getText())
                .condition(conditionComboBox.getEditor().getText())
                .denom(denomComboBox.getEditor().getText())
                .inserts(insertsTextField.getText())
                .mold(moldComboBox.getEditor().getText())
                .notes(notesTextArea.getText())
                .obsolete(obsoleteToggleButton.isSelected())
                .rarity(rarityComboBox.getValue())
                .tcrID(tcrTextField.getText())
                .year(yearTextField.getText())
                .issue(issueTextField.getText())
                .casino(casino);

        if (!isNullOrEmpty(paidTextField.getText())) {
            pokerChipBuilder.amountPaid(MoneyAmount.parse(paidTextField.getText()));
        }
        if (!isNullOrEmpty(valueTextField.getText())) {
            pokerChipBuilder.amountValue(MoneyAmount.parse(valueTextField.getText()));
        }

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
    public static String[] CONDITION_VALUES = new String[]{"Uncirculated", "Slightly Used", "Average", "Well Used", "Poor", "Cancelled"};
    public static String[] CATEGORY_VALUES = new String[]{"Baccarat", "Error", "Faro", "Free Play", "Match Play", "No Cash Value", "No Denomination", "Non-Negotiable", "Poker", "Roulette", "Race and Sport"};

    public static final String IMAGE_ERROR_ALERT_TITLE = "dialog.poker.chip.image.error.alert.title";
    public static final String IMAGE_FILE_CHOOSER_TITLE = "dialog.poker.chip.image.file.chooser.title";
    public static final String IMAGE_ERROR_ALERT_HEADER = "dialog.poker.chip.image.error.alert.header";
    public static final String IMAGE_ERROR_ALERT_CONTENT = "dialog.poker.chip.image.error.alert.content";
}
