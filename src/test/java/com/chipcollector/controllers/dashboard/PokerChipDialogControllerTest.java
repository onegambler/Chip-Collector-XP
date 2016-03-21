package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.spring.SpringFxmlLoader;
import com.chipcollector.test.util.TestAppConfig;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
@ActiveProfiles("test")
public class PokerChipDialogControllerTest extends ApplicationTest {

    private static final String NODE_FILE_NAME = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";

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
        verifyThat("#pokerChipDialog", Node::isVisible);
        clickOn("#cancelButton");
        verify(collection, never()).add(any());
        verifyThat("#pokerChipDialog", Objects::isNull);
    }
}