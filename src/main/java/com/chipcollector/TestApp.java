package com.chipcollector;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.data.PokerChipDAO;
import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class TestApp extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.ENGLISH);
        FXMLLoader loader = new FXMLLoader(Resources.getResource(DASHBOARD_FX_FILE_LOCATION), bundle);
        Parent root = loader.load();

        final EbeanServer ebeanServer = Ebean.getDefaultServer();
        PokerChipDAO pokerChipDAO = new PokerChipDAO(ebeanServer);

        Scene scene = new Scene(root, 620, 500);
        primaryStage.setTitle("Chip Collector XP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/PokerChipDialog.fxml";
}
