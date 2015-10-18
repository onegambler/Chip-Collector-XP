package com.chipcollector;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.controller.main.DashboardController;
import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.*;
import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChipCollectorXPApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("DisplayValues", Locale.ENGLISH);
        //TODO: relative
        URL url = new File("C:\\Users\\PC\\IdeaProjects\\Chip Collector XP\\src\\main\\java\\com\\chipcollector\\view\\main\\DashBoard.fxml").toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url, bundle);

        final EbeanServer ebeanServer = Ebean.getDefaultServer();
        final PokerChipDAO pokerChipDAO = new PokerChipDAO(ebeanServer);
        DashboardController controller = new DashboardController(pokerChipDAO);

        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root, 300, 275);

        primaryStage.setTitle("Chip Collector XP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createDB(PokerChipDAO pokerChipDAO) {
        //TODO: remove
        final Country country = new Country("Name");
        final Location locat = Location.builder()
                .city("city")
                .country(country)
                .state("state")
                .build();
        final Casino casino = Casino.builder()
                .closeDate(LocalDate.now())
                .location(locat)
                .name("name")
                .openDate(LocalDate.now())
                .theme("theme")
                .type("type")
                .website("website")
                .build();
        for (int i = 0; i < 1000; i++) {
            pokerChipDAO.savePokerChip(getPokerChip(casino, i));
        }
        //TODO: remove

    }

    //TODO: remove
    public PokerChip getPokerChip(Casino casino, int idx) {

        BlobImage image_1 = null;
        try {
            image_1 = new BlobImage();
            image_1.setImage(Files.readAllBytes(Paths.get("C:\\Users\\PC\\Desktop\\019402.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PokerChip.builder()
                .acquisitionDate(LocalDate.now())
                .frontImage(image_1)
                .backImage(image_1)
                .color("color")
                .denom("denom")
                .inlay("inlay")
                .inserts("inserts")
                .mold("mold")
                .year("year")
                .tcrID("rober_" + idx)
                .casino(casino).build();
    }
}
