package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.*;
import com.chipcollector.domain.PokerChip.PokerChipBuilder;
import com.chipcollector.models.dashboard.CasinoBean;
import com.chipcollector.models.dashboard.PokerChipBean;
import com.chipcollector.util.ImageConverter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Setter;
import org.controlsfx.validation.ValidationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.chipcollector.util.ImageConverter.bufferedImageToRawBytes;
import static com.chipcollector.util.ImageConverter.resizeImage;
import static java.lang.Integer.parseInt;
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
    private ImageView frontImageView;
    @FXML
    private ImageView backImageView;
    @Setter
    private PokerChipBean pokerChipBean;

    private PokerChipCollection pokerChipCollection;

    @Autowired
    public PokerChipDialogController(PokerChipCollection pokerChipCollection) {
        this.pokerChipCollection = pokerChipCollection;
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
        frontImageView.setImage(pokerChipBean.getFrontImage());
        backImageView.setImage(pokerChipBean.getBackImage());
        Country country = pokerChipCollection.getCountryFromName(pokerChipBean.getCasinoBean().getCountry());
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
        closeDialog(actionEvent);
    }

    private void closeDialog(Event event) {
        ((Node) event.getTarget()).getScene().getWindow().hide();
    }

    public void onOkAction(ActionEvent event) throws IOException {
        final CasinoBean casinoBean = pokerChipBean.getCasinoBean();
        final Optional<Casino> existingCasino = pokerChipCollection.getCasinoFromCasinoBean(casinoBean);
        Casino casino;
        if (!existingCasino.isPresent()) {
            Optional<Location> existingLocation = pokerChipCollection.getLocationFromCasinoBean(casinoBean);
            Location location;
            if (!existingLocation.isPresent()) {
                Country country = pokerChipCollection.getCountryFromCasinoBean(casinoBean);
                location = Location.builder()
                        .city(casinoBean.getCity())
                        .state(casinoBean.getState())
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
                //.amountPaid(paidTextField.getText())
                //.value()
                .casino(casino);

        String issueText = issueTextField.getText();
        if (!Strings.isNullOrEmpty(issueText)) {
            pokerChipBuilder.issue(parseInt(issueText));
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

        closeDialog(event);
    }

    private BlobImage getBlobImageFromImage(Image image) throws IOException {
        BufferedImage originalImage = ImageConverter.imageToBufferedImage(image);
        byte[] thumbnail = bufferedImageToRawBytes(resizeImage(originalImage, 90), "png");
        byte[] resizedImage = bufferedImageToRawBytes(resizeImage(originalImage, 120), "png");
        BlobImage blobImage = new BlobImage();
        blobImage.setImage(resizedImage);
        blobImage.setThumbnail(thumbnail);
        return blobImage;
    }

    public static final String IMAGES_FLAGS_LOCATION = "images/flags/%s";

    private static final String[] RARITY_VALUES = new String[]{"R-1", "R-2", "R-3", "R-4", "R-5", "R-6", "R-7", "R-8", "R-9", "R-10", "Unknown"};
}
