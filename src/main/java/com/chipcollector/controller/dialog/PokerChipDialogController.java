package com.chipcollector.controller.dialog;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.Country;
import com.chipcollector.domain.Rarity;
import com.chipcollector.model.dashboard.CasinoBean;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.MessagesHelper;
import com.chipcollector.views.node.AutoCompleteComboBoxListener;
import com.chipcollector.views.node.DatePickerFocusListener;
import com.chipcollector.views.node.moneytextfield.MoneyTextField;
import com.google.common.io.Resources;
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
import org.controlsfx.control.PopOver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.chipcollector.util.EventUtils.getWindow;
import static com.chipcollector.util.EventUtils.isDoubleClick;
import static java.util.Objects.nonNull;
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
    public MoneyTextField valueTextField;
    @FXML
    public DatePicker acquisitionDatePicker;
    @FXML
    public TextField tcrTextField;
    @FXML
    public TextArea notesTextArea;
    @FXML
    public ToggleButton obsoleteToggleButton;
    @FXML
    public ToggleButton cancelledToggleButton;
    @FXML
    public MoneyTextField paidTextField;
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

    private final PokerChipCollection pokerChipCollection;

    private PokerChipBean pokerChipBean;

    @Autowired
    private SpringFxmlLoader springFxmlLoader;

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

        frontImageView.setOnMouseClicked(event -> chooseImage(event, image -> frontImageView.setImage(image)));
        backImageView.setOnMouseClicked(event -> chooseImage(event, image -> backImageView.setImage(image)));

        //TODO: classe esterna?
        acquisitionDatePicker.setDayCellFactory(datePicker ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #EEEEEE;");
                        }
                    }
                });
    }

    private void chooseImage(MouseEvent event, Consumer<Image> imageSetter) {
        if (isDoubleClick(event)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(MessagesHelper.getString(IMAGE_FILE_CHOOSER_TITLE));
            final File file = fileChooser.showOpenDialog(getWindow(event));
            if (nonNull(file)) {
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
        this.pokerChipBean = bind(pokerChipBean);
    }

    private PokerChipBean bind(PokerChipBean pokerChipBean) {
        yearTextField.textProperty().bindBidirectional(pokerChipBean.yearProperty());
        cancelledToggleButton.selectedProperty().bindBidirectional(pokerChipBean.cancelledProperty());
        rarityComboBox.valueProperty().bindBidirectional(pokerChipBean.rarityProperty());
        colorComboBox.getEditor().textProperty().bindBidirectional(pokerChipBean.colorProperty());
        moldComboBox.getEditor().textProperty().bindBidirectional(pokerChipBean.moldProperty());
        insertsTextField.textProperty().bindBidirectional(pokerChipBean.insertsProperty());
        inlayComboBox.getEditor().textProperty().bindBidirectional(pokerChipBean.inlayProperty());
        conditionComboBox.valueProperty().bindBidirectional(pokerChipBean.conditionProperty());
        categoryComboBox.valueProperty().bindBidirectional(pokerChipBean.categoryProperty());
        //JavaFX bug doesn't update value if the date is inserted manually, temporary workaround.
        acquisitionDatePicker.valueProperty().bindBidirectional(pokerChipBean.dateOfAcquisitionProperty());
        acquisitionDatePicker.focusedProperty().addListener(new DatePickerFocusListener(acquisitionDatePicker));
        tcrTextField.textProperty().bindBidirectional(pokerChipBean.tcrIdProperty());
        notesTextArea.textProperty().bindBidirectional(pokerChipBean.notesProperty());
        denomComboBox.getEditor().textProperty().bindBidirectional(pokerChipBean.denomProperty());
        obsoleteToggleButton.selectedProperty().bindBidirectional(pokerChipBean.obsoleteProperty());
        issueTextField.textProperty().bindBidirectional(pokerChipBean.issueProperty());

        //TODO: better approach?
        setCasinoContent(pokerChipBean.getCasinoBean());

        frontImageView.imageProperty().bindBidirectional(pokerChipBean.frontImageProperty());
        backImageView.imageProperty().bindBidirectional(pokerChipBean.backImageProperty());

        valueTextField.valueProperty().bindBidirectional(pokerChipBean.amountValueProperty());
        paidTextField.valueProperty().bindBidirectional(pokerChipBean.amountPaidProperty());
        return pokerChipBean;
    }

    private void setCasinoContent(CasinoBean casinoBean) {
        casinoContent.setText(casinoBean.toString());
        Country country = casinoBean.getCountry();
        if (nonNull(country)) {
            casinoCountryFlag.setImage(country.getFlagImage());
            Tooltip.install(casinoCountryFlag, new Tooltip(casinoBean.getCountryName()));
        }
    }

    public void onCancelAction(ActionEvent actionEvent) {
        pokerChipBean.resetDirty();
        closeDialog(actionEvent);
    }

    private void closeDialog(Event event) {
        getWindow(event).hide();
    }

    @FXML
    public void onOkAction(ActionEvent event) throws IOException {
        if (pokerChipBean.isNew()) {
            pokerChipCollection.add(pokerChipBean);
        } else {
            pokerChipCollection.update(pokerChipBean);
        }
        closeDialog(event);
    }

    @FXML
    public void onEditCasino(Event event) throws IOException {
        //TODO: clean up
        String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dialog/CasinoDialog.fxml";
        String DASHBOARD_CSS_FILE_LOCATION = "com/chipcollector/views/dialog/CasinoDialog.css";
        Node node = springFxmlLoader.<Node, CasinoDialogController>load(DASHBOARD_FX_FILE_LOCATION,
                controller -> controller.setCasinoBean(pokerChipBean.getCasinoBean()));
        PopOver popOver = new PopOver();
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popOver.setContentNode(node);
        popOver.getRoot().getStylesheets().clear();
        popOver.getRoot().getStylesheets()
                .add(Resources.getResource(DASHBOARD_CSS_FILE_LOCATION).toExternalForm());
        popOver.show((Node) event.getSource());
        popOver.setOnHiding(hidingEvent -> setCasinoContent(pokerChipBean.getCasinoBean()));
    }

    public static String[] CONDITION_VALUES = new String[]{"Uncirculated", "Slightly Used", "Average", "Well Used", "Poor", "Cancelled"};
    public static String[] CATEGORY_VALUES = new String[]{"Baccarat", "Error", "Faro", "Free Play", "Match Play", "No Cash Value", "No Denomination", "Non-Negotiable", "Poker", "Roulette", "Race and Sport"};

    public static final String IMAGE_ERROR_ALERT_TITLE = "dialog.poker.chip.image.error.alert.title";
    public static final String IMAGE_FILE_CHOOSER_TITLE = "dialog.poker.chip.image.file.chooser.title";
    public static final String IMAGE_ERROR_ALERT_HEADER = "dialog.poker.chip.image.error.alert.header";
    public static final String IMAGE_ERROR_ALERT_CONTENT = "dialog.poker.chip.image.error.alert.content";


    public void onLinkCasino(ActionEvent actionEvent) {

    }

    public void onUnlinkCasino(ActionEvent actionEvent) {

    }
}
