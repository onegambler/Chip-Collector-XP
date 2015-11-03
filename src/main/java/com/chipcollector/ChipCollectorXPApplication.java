package com.chipcollector;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.chipcollector.config.SpringAppConfig;
import com.chipcollector.data.AppSettings;
import com.chipcollector.data.PokerChipDAO;
import com.chipcollector.domain.*;
import com.chipcollector.util.ImageConverter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.imgscalr.Scalr;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static java.lang.String.format;
import static org.imgscalr.Scalr.THRESHOLD_QUALITY_BALANCED;

public class ChipCollectorXPApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        final ApplicationContext context = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        SpringFxmlLoader loader = context.getBean(SpringFxmlLoader.class);

        final AppSettings settings = context.getBean(AppSettings.class);

        if (settings.getLastUsedDatabase().isPresent()) {
            ServerConfig config = new ServerConfig();
            config.loadFromProperties();
            config.setDefaultServer(true);
            config.getDataSourceConfig().setUrl(format("jdbc:sqlite:%s", settings.getLastUsedDatabase().get()));
            EbeanServer ebeanServer = EbeanServerFactory.create(config);
            Ebean.register(ebeanServer, true);
        }

        Parent root = loader.load(DASHBOARD_FX_FILE_LOCATION);
        Scene scene = new Scene(root, 400, 600);
        primaryStage.setTitle("Chip Collector XP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*@Override
    public void start(Stage primaryStage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.ENGLISH);
        FXMLLoader loader = new FXMLLoader(Resources.getResource(DASHBOARD_FX_FILE_LOCATION), bundle);

        final EbeanServer ebeanServer = Ebean.getDefaultServer();
        PokerChipDAO pokerChipDAO = new PokerChipDAO(ebeanServer);

        final Collection collection = new Collection(pokerChipDAO);
        final AppConfiguration configuration = new AppConfiguration();

        //createDB(pokerChipDAO);
        Parent root = loader.load();

        DashboardController controller = loader.<DashboardController>getController();
        controller.setConfiguration(configuration);
        controller.loadComponentsData();

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setTitle("Chip Collector XP");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

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
