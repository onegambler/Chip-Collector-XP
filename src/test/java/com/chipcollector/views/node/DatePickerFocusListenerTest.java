package com.chipcollector.views.node;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.StrictAssertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class DatePickerFocusListenerTest {

    @Spy
    private DatePicker datePicker;

    @Mock
    private TextField textField;
    @Mock
    private LocalDateStringConverter converter;

    private JFXPanel panel = new JFXPanel(); // Initialises the JavaFxToolkit
    private DatePickerFocusListener listener;

    @Before
    public void setUp() {
        datePicker = new DatePicker();
        listener = new DatePickerFocusListener(datePicker);
    }

    @Test
    public void whenFocusIsGainedThenDoNothing() {
        final String value = "Text that should make the listener fail";
        datePicker.getEditor().setText(value);
        listener.changed(null, null, true);
        assertThat(datePicker.getEditor().getText()).isEqualTo(value);
        assertThat(datePicker.getValue()).isNull();
    }

    @Test
    public void whenACorrectDateIsTypedAndFocusIsLostThenSetTheParsedDateInTheDatePicker() {
        final String stringDate = "06/10/2001"; //US formatting
        datePicker.getEditor().setText(stringDate);

        final LocalDate expectedLocalDate = LocalDate.of(2001, 6, 10);

        listener.changed(null, null, false);

        assertThat(datePicker.getValue()).isEqualTo(expectedLocalDate);
    }

}