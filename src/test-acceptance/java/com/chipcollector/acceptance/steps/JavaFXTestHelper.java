package com.chipcollector.acceptance.steps;

import com.avaje.ebean.EbeanServer;
import com.chipcollector.DatabaseTestUtil;
import com.chipcollector.spring.SpringFxmlLoader;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testfx.framework.junit.ApplicationTest;

public class JavaFXTestHelper extends ApplicationTest {

    private DatabaseTestUtil databaseTestUtil;
    private final SpringFxmlLoader loader;
    private boolean init;

    @Autowired
    private EbeanServer server;

    @Before
    public void setUp() throws Exception {
        if (!init) {
            databaseTestUtil = new DatabaseTestUtil(server, "acceptance-test-ebean.properties");
            databaseTestUtil.deleteDatabaseFile();
            databaseTestUtil.createSchema();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> databaseTestUtil.tearDownDatabase()));
            init = true;
        }
        super.internalBefore();
    }

    @After
    public void tearDown() throws Exception {
        super.internalAfter();
        databaseTestUtil.cleanDatabase();
    }

    @Autowired
    public JavaFXTestHelper(SpringFxmlLoader loader) {
        this.loader = loader;
    }

    @Override
    public void start(Stage stage) throws Exception {
        final FXMLLoader loader = this.loader.getLoader(NODE_FILE_NAME);
        Parent node = loader.load();
        Scene scene = new Scene(node);
        stage.setScene(scene);
        stage.show();
    }

    private static final String NODE_FILE_NAME = "com/chipcollector/views/dashboard/MainWindow.fxml";
}
