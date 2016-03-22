package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.domain.PokerChip;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.TestAppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Objects;
import java.util.Optional;

import static com.chipcollector.test.util.PokerChipTestUtil.createTestPokerChip;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class PokerChipDialogControllerIT extends ApplicationTest {

    @Autowired
    private SpringFxmlLoader loader;

    @Autowired
    private PokerChipCollection collection;

    private PokerChipDialogController controller;

    @Override
    public void start(Stage stage) throws Exception {

        final FXMLLoader loader = this.loader.getLoader(NODE_FILE_NAME);
        Parent node = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(node);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void whenCancelIsClickedThenTheDialogIsClosed() {
        verifyThat(POKER_CHIP_DIALOG, Node::isVisible);
        clickOn("#cancelButton");
        verify(collection, never()).add(any());
        verifyThat(POKER_CHIP_DIALOG, Objects::isNull);
    }

    @Test
    public void whenFieldsAreFilledAndOKIsClickedThenSavePokerChip() {
        final PokerChip testPokerChip = createTestPokerChip();
        final PokerChipBean pokerChipBean = new PokerChipBean();
        when(collection.getCountryFromName(any())).thenReturn(Optional.empty());
        when(collection.getCasinoFromCasinoBean(pokerChipBean.getCasinoBean()))
                .thenReturn(Optional.of(testPokerChip.getCasino()));

        controller.setPokerChipBean(pokerChipBean);
        fillDialogUp(testPokerChip);
        clickOn("#okButton");
        verifyThat(POKER_CHIP_DIALOG, Objects::isNull);

        ArgumentCaptor<PokerChip> pokerChipArgumentCaptor = ArgumentCaptor.forClass(PokerChip.class);
        verify(collection).add(pokerChipArgumentCaptor.capture());

        assertThat(pokerChipArgumentCaptor.getValue()).isEqualTo(testPokerChip);
    }

    @Test
    public void whenANonNumberIsEnteredInYearTextFieldThenDoNotAcceptIt() {
        clickOn(YEAR_TEXT_FIELD).write("hello");
        verifyThat(YEAR_TEXT_FIELD, hasText(""));
        clickOn("#cancelButton");
    }

    private void fillDialogUp(PokerChip pokerChip) {
        clickOn(YEAR_TEXT_FIELD).write(pokerChip.getYear());
        clickOn("#denomComboBox").write(pokerChip.getDenom());
        clickOn("#issueTextField").write(pokerChip.getIssue());
        clickOn("#colorComboBox").write(pokerChip.getColor());
        clickOn("#moldComboBox").write(pokerChip.getMold());
        clickOn("#inlayComboBox").write(pokerChip.getInlay());
        clickOn("#insertsTextField").write(pokerChip.getInserts());
        clickOn("#conditionComboBox").clickOn(pokerChip.getCondition());
        clickOn("#categoryComboBox").clickOn(pokerChip.getCategory());
        clickOn("#rarityComboBox").clickOn(pokerChip.getRarity().getDisplayName());
        clickOn("#valueTextField").write(pokerChip.getAmountValue().toString());
        clickOn("#dateOfAcquisitionDatePicker").type(KeyCode.BACK_SPACE, 15).write(pokerChip.getAcquisitionDate().toString());
        clickOn("#tcrTextField").write(pokerChip.getTcrID());
        clickOn("#notesTextArea").write(pokerChip.getNotes());
        clickOn("#paidTextField").write(pokerChip.getAmountPaid().toString());
        if (pokerChip.isObsolete()) {
            clickOn("#obsoleteToggleButton");
        }
        if (pokerChip.isCancelled()) {
            clickOn("#cancelledToggleButton");
        }

    }

    @After
    public void tearDown() {
        reset(collection);
    }

    private static final String NODE_FILE_NAME = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
    public static final String YEAR_TEXT_FIELD = "#yearTextField";
    public static final String POKER_CHIP_DIALOG = "#pokerChipDialog";
}