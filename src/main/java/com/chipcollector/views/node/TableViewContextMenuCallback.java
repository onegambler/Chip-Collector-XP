package com.chipcollector.views.node;

import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.util.MessagesHelper;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.function.Consumer;

import static javafx.beans.binding.Bindings.isNotNull;
import static javafx.beans.binding.Bindings.when;

public class TableViewContextMenuCallback implements Callback<TableView<PokerChipBean>, TableRow<PokerChipBean>> {

    private Consumer<PokerChipBean> deleteAction;

    public TableViewContextMenuCallback(Consumer<PokerChipBean> deleteAction) {
        this.deleteAction = deleteAction;
    }

    @Override
    public TableRow<PokerChipBean> call(TableView<PokerChipBean> param) {
        final TableRow<PokerChipBean> row = new TableRow<>();
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem(MessagesHelper.getString(BUTTON_DELETE));
        removeItem.setOnAction(event -> {
            deleteAction.accept(row.getItem());
        });
        rowMenu.getItems().addAll(removeItem);

        // only display context menu for non-null items:
        row.contextMenuProperty().bind(when(isNotNull(row.itemProperty()))
                .then(rowMenu)
                .otherwise((ContextMenu) null));
        return row;
    }

    public static final String BUTTON_DELETE = "button.delete";
}
