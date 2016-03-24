package com.chipcollector.views.node.moneytextfield;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public class MoneyFilter implements UnaryOperator<TextFormatter.Change> {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\$|€|£|\\d|\\s)\\s*(\\d|,|\\.)*");

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {

        if (ignoreChange(change)) {
            return change;
        }
        return matches(change.getControlNewText()) ? change : null;
    }

    protected boolean matches(String amount) {
        requireNonNull(amount, "Passed amount cannot be null");
        return AMOUNT_PATTERN.matcher(amount).matches();
    }

    private boolean ignoreChange(TextFormatter.Change change) {
        return !change.isContentChange() || change.isDeleted() || change.getCaretPosition() == 0;
    }
}
