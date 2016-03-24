package com.chipcollector.util;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

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

    public static Window getWindow(Event event) {
        return ((Node) event.getSource()).getScene().getWindow();
    }
}
