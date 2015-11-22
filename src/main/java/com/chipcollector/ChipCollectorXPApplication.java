package com.chipcollector;

import com.chipcollector.config.SpringAppConfig;
import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.*;
import com.chipcollector.util.DatabaseUtil;
import com.chipcollector.util.ImageConverter;
import javafx.application.Application;
import javafx.stage.Stage;
import org.imgscalr.Scalr;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static com.avaje.ebean.Ebean.getDefaultServer;
import static org.imgscalr.Scalr.THRESHOLD_QUALITY_BALANCED;

public class ChipCollectorXPApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        final ApplicationContext context = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        new DatabaseUtil(getDefaultServer()).tryDatabaseUpdate();

        SpringFxmlLoader loader = context.getBean(SpringFxmlLoader.class);
        loader.show(DASHBOARD_FX_FILE_LOCATION, "Chip Collector XP");
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
                .closeDate(LocalDate.now().toString())
                .location(locat)
                .name("name")
                .openDate(LocalDate.now().toString())
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

    public static final String STATS_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/Dashboard.fxml";
    public static final String DASHBOARD_FX_FILE_LOCATION = "com/chipcollector/views/dashboard/MainWindow.fxml";
    public static final int THUMBNAIL_IMAGE_SIZE = 60;
}
