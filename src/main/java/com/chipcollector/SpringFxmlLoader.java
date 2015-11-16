package com.chipcollector;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Component
public class SpringFxmlLoader implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.ENGLISH);

    public <T> T load(String url) {
        try {
            return getLoader(url).load();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public FXMLLoader getLoader(String url) {
        return new FXMLLoader(Resources.getResource(url), bundle, null, applicationContext::getBean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> void showDialog(String dialogFile, String title, Consumer<T> consumer) {
            FXMLLoader loader = getLoader(dialogFile);
            Stage dialogStage = getStage(loader, title);
            dialogStage.setTitle(title);
            consumer.accept(loader.getController());
            //dialogStage.initOwner(); //TODO: init the owner
            dialogStage.showAndWait();
    }

    public void show(String fxmlFile, String title) {
        FXMLLoader loader = getLoader(fxmlFile);
        getStage(loader, title).show();
    }

    private Stage getStage(FXMLLoader loader, String title) {
        try {
            Parent node = loader.load();
            Scene scene = new Scene(node);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            return stage;
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}