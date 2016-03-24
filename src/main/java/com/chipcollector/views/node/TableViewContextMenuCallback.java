package com.chipcollector.views.node;

import com.chipcollector.data.PokerChipCollection;
import com.chipcollector.model.dashboard.PokerChipBean;
import com.chipcollector.util.MessagesHelper;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import static javafx.beans.binding.Bindings.isNotNull;
import static javafx.beans.binding.Bindings.when;

public class TableViewContextMenuCallback implements Callback<TableView<PokerChipBean>, TableRow<PokerChipBean>> {

    private TableView<PokerChipBean> pokerChipsTable;
    private PokerChipCollection collection;

    public TableViewContextMenuCallback(TableView<PokerChipBean> pokerChipsTable, PokerChipCollection collection) {
        this.pokerChipsTable = pokerChipsTable;
        this.collection = collection;
    }

    @Override
    public TableRow<PokerChipBean> call(TableView<PokerChipBean> param) {
        final TableRow<PokerChipBean> row = new TableRow<>();
        final ContextMenu rowMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem(MessagesHelper.getString(BUTTON_DELETE));
        removeItem.setOnAction(event -> {
            collection.deletePokerChip(row.getItem().getPokerChip());
            pokerChipsTable.getItems().remove(row.getItem());
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
