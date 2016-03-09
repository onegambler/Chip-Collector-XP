package com.chipcollector.views.control.moneyfield;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MoneyFilter implements UnaryOperator<TextFormatter.Change> {

//    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^(\\$|)?([1-9]\\d{0,2}(,\\d{3})*|([1-9]\\d*))(\\.\\d{2})?$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\$|€|£|\\d|\\s)\\s*(\\d|,|.)*");

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {

        if (ignoreChange(change)) {
            return change;
        }

        final String newText = change.getControlNewText();

        return AMOUNT_PATTERN.matcher(newText).matches() ? change : null;
    }

    private boolean ignoreChange(TextFormatter.Change change) {
        return !change.isContentChange() || change.isDeleted() || change.getCaretPosition() == 0;
    }
}
