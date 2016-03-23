package com.chipcollector.acceptance.steps;

import com.chipcollector.controllers.dashboard.PokerChipDialogController;
import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.spring.SpringFxmlLoader;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testfx.framework.junit.ApplicationTest;

public class PokerChipDialogStepHelper extends ApplicationTest {

    private final SpringFxmlLoader loader;
    private final PokerChipCollection collection;

    private PokerChipDialogController controller;

    @Before
    public void setUp() throws Exception {
        super.internalBefore();
    }

    @After
    public void tearDown() throws Exception {
        super.internalAfter();
    }

    @Autowired
    public PokerChipDialogStepHelper(SpringFxmlLoader loader, PokerChipCollection collection) {
        this.loader = loader;
        this.collection = collection;
    }

    @Override
    public void start(Stage stage) throws Exception {
        final FXMLLoader loader = this.loader.getLoader(NODE_FILE_NAME);
        Parent node = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(node);
        stage.setScene(scene);
        stage.show();
    }

    private static final String NODE_FILE_NAME = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
}
