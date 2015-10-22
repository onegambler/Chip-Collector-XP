package com.chipcollector;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.chipcollector.controllers.dashboard.DashboardController;
import com.chipcollector.data.Collection;
import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.*;
import com.chipcollector.util.ImageConverter;
import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.imgscalr.Scalr.THRESHOLD_QUALITY_BALANCED;

public class ChipCollectorXPApplication extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.ENGLISH);
        FXMLLoader loader = new FXMLLoader(Resources.getResource(DASHBOARD_FX_FILE_LOCATION), bundle);

        final EbeanServer ebeanServer = Ebean.getDefaultServer();
        PokerChipDAO pokerChipDAO = new PokerChipDAO(ebeanServer);
        final Collection collection = new Collection(pokerChipDAO);
        //createDB(pokerChipDAO);

        Parent root = loader.load();

        DashboardController controller = loader.<DashboardController>getController();
        controller.setCollection(collection);
        controller.loadComponentsData();

        Scene scene = new Scene(root, 400, 600);
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
            Path imagePath = Paths.get("C:\\Users\\PC\\IdeaProjects\\Chip Collector XP\\src\\test-integration\\resources\\images\\java_logo.png");
            BufferedImage resizedImage = Scalr.resize(ImageIO.read(imagePath.toUri().toURL()), THUMBNAIL_IMAGE_SIZE, THRESHOLD_QUALITY_BALANCED);
            image_1.setThumbnail(ImageConverter.bufferedImageToRawBytes(resizedImage, "png"));
            image_1.setImage(Files.readAllBytes(imagePath));
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

    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/Dashboard.fxml";
    public static final int THUMBNAIL_IMAGE_SIZE = 60;
}
