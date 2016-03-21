package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.util.TestAppConfig;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Objects;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

    @Override
    public void start(Stage stage) throws Exception {

        Parent node = loader.load(NODE_FILE_NAME);
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
    public void whenCharactersAreInsertedIn() {
        verifyThat(POKER_CHIP_DIALOG, Node::isVisible);
        clickOn("#cancelButton");
        verify(collection, never()).add(any());
        verifyThat(POKER_CHIP_DIALOG, Objects::isNull);
    }

    @Test
    public void whenANonNumberIsEnteredInYearTextFieldThenDoNotAcceptIt() {
        clickOn(YEAR_TEXT_FIELD).write("hello");
        verifyThat(YEAR_TEXT_FIELD, hasText(""));
        clickOn("#cancelButton");
    }

    private void fillDialogUp() {
        clickOn(YEAR_TEXT_FIELD).write("1990");
    }

    private static final String NODE_FILE_NAME = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
    public static final String YEAR_TEXT_FIELD = "#yearTextField";
    public static final String POKER_CHIP_DIALOG = "#pokerChipDialog";
}