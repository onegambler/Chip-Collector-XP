package com.chipcollector.views.components;

import com.chipcollector.models.dashboard.CasinoBean;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CasinoListCell extends ListCell<CasinoBean> {

    @Override
    protected void updateItem(CasinoBean item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.getChildren().add(new ImageView(item.getCountryFlag()));

            Label casinoNameLabel = new Label();
            casinoNameLabel.setPrefWidth(1000);
            StringBuilder casinoItemDisplayString = new StringBuilder();
            casinoItemDisplayString.append(item.getName());
            casinoItemDisplayString.append("-");
            casinoItemDisplayString.append(item.getCity());
            if (item.getState().isNotNull().get()) {
                casinoItemDisplayString.append(item.getState());
            }


            casinoNameLabel.setText(casinoItemDisplayString.toString());
            hBox.getChildren().add(casinoNameLabel);
            setGraphic(hBox);
        }
    }
}
