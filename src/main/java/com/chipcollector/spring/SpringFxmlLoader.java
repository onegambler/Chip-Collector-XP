package com.chipcollector.spring;

import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static javafx.stage.Modality.APPLICATION_MODAL;

@Component
public class SpringFxmlLoader implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.getDefault());

    public <T extends Node> T load(String url) {
        try {
            return getLoader(url).load();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public <T extends Node, Q> T load(String url, Consumer<Q> controllerConsumer) {
        try {
            FXMLLoader loader = getLoader(url);
            T node = loader.load();
            controllerConsumer.accept(loader.getController());
            return node;
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

    public <T> void showDialog(String fxml, String title, Window owner, boolean resizeable, Consumer<T> controllerConsumer) {
        FXMLLoader loader = getLoader(fxml);
        Stage dialogStage = getStage(loader, title);
        dialogStage.setTitle(title);
        dialogStage.initModality(APPLICATION_MODAL);
        controllerConsumer.accept(loader.getController());
        dialogStage.initOwner(owner);
        dialogStage.setResizable(resizeable);
        dialogStage.showAndWait();
    }

    public void showDialog(String dialogFile, String title, Window dialogOwner, boolean resizeable) {
        showDialog(dialogFile, title, dialogOwner, resizeable, o -> {
        });
    }

    public Stage getStage(String fxmlFile, String title) {
        FXMLLoader loader = getLoader(fxmlFile);
        return getStage(loader, title);
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