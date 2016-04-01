package com.chipcollector.views.node;

import com.chipcollector.util.MessagesHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public class DatePickerFocusListener implements ChangeListener<Boolean> {

    private static final String DATE_PICKER_INVALID_FORMAT_MESSAGE_ID = "validation.field.datepicker";
    private final DatePicker datePicker;
    private final ValidationPopOver validationPopOver;

    DatePickerFocusListener(@NotNull DatePicker datePicker, @NotNull ValidationPopOver validationPopOver) {
        this.datePicker = datePicker;
        this.validationPopOver = validationPopOver;
    }

    public DatePickerFocusListener(@NotNull DatePicker datePicker) {
        this(datePicker, new ValidationPopOver());
    }

    @Override
    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        if (!newValue) {
            try {
                final LocalDate value = datePicker.getConverter().fromString(datePicker.getEditor().getText());
                datePicker.setValue(value);
            } catch (Exception e) {
                datePicker.setValue(null);
                final String string = MessagesHelper.getString(DATE_PICKER_INVALID_FORMAT_MESSAGE_ID, "dd/MM/YYYY");
                validationPopOver.setValidationMessage(string);
                validationPopOver.show(datePicker);
            }
        }
    }
}
