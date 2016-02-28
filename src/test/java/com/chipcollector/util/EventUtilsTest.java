package com.chipcollector.util;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.junit.Test;

import static com.chipcollector.util.EventUtils.*;
import static javafx.scene.input.KeyCode.ASTERISK;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import static org.assertj.core.api.Assertions.assertThat;


public class EventUtilsTest {

    @Test
    public void whenKeyPressedIsEnterThenIsKeyboardEnterPressedReturnsTrue() throws Exception {
        KeyEvent keyEvent = new KeyEvent(null, null, null, ENTER, false, false, false, false);
        assertThat(isKeyboardEnterPressed(keyEvent)).isTrue();
    }

    @Test
    public void whenKeyPressedIsNotEnterThenIsKeyboardEnterPressedReturnsFalse() throws Exception {
        KeyEvent keyEvent = new KeyEvent(null, null, null, ASTERISK, false, false, false, false);
        assertThat(isKeyboardEnterPressed(keyEvent)).isFalse();
    }

    @Test
    public void whenButtonPressedIsPrimaryThenIsMousePrimaryButtonPressedReturnsTrue() throws Exception {
        MouseEvent mouseEvent = new MouseEvent(null, 0, 0, 0, 0, PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null);
        assertThat(isMousePrimaryButtonPressed(mouseEvent)).isTrue();
    }

    @Test
    public void whenButtonPressedIsNotPrimaryThenIsMousePrimaryButtonPressedReturnsFalse() throws Exception {
        MouseEvent mouseEvent = new MouseEvent(null, 0, 0, 0, 0, SECONDARY, 0, false, false, false, false, false, false, false, false, false, false, null);
        assertThat(isMousePrimaryButtonPressed(mouseEvent)).isFalse();
    }

    @Test
    public void whenEventClickCountIsTwoThenIsDoubleClickReturnsTrue() throws Exception {
        MouseEvent mouseEvent = new MouseEvent(null, 0, 0, 0, 0, PRIMARY, 2, false, false, false, false, false, false, false, false, false, false, null);
        assertThat(isDoubleClick(mouseEvent)).isTrue();
    }

    @Test
    public void whenEventClickCountIsNotTwoThenIsMousePrimaryButtonPressedReturnsFalse() throws Exception {
        MouseEvent mouseEvent = new MouseEvent(null, 0, 0, 0, 0, PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, null);
        assertThat(isDoubleClick(mouseEvent)).isFalse();
    }
}