package com.chipcollector.util;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.MouseButton.PRIMARY;

public class EventUtils {

    public static boolean isKeyboardEnterPressed(KeyEvent e) {
        return e.getCode() == ENTER;
    }

    public static boolean isMousePrimaryButtonPressed(MouseEvent event) {
        return event.getButton() == PRIMARY;
    }

    public static boolean isDoubleClick(MouseEvent event) {
        return event.getClickCount() == 2;
    }
}
