package com.chipcollector.controllers.dashboard;

import com.chipcollector.data.PokerChipCollection;
import com.google.common.io.Resources;
import javafx.fxml.FXMLLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.testfx.api.FxToolkit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(MockitoJUnitRunner.class)
public class PokerChipDialogControllerTest {

    @Mock
    private PokerChipCollection collection;

    private PokerChipDialogController controller;

    @Before
    public void setUp() throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(Resources.getResource(POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION),
                null, null, param -> new PokerChipDialogController(collection));
        fxmlLoader.load();
        controller = fxmlLoader.getController();
    }

    @Test
    public void test() {
        System.out.println("controller = " + controller);
    }

    public static final String POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
}