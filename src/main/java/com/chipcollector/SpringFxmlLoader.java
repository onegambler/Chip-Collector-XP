package com.chipcollector;

import com.chipcollector.controllers.dashboard.PokerChipDialogController;
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
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.chipcollector.controllers.dashboard.DashboardController.POKER_CHIP_ADD_DIALOG_FX_FILE_LOCATION;

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
        Parent dialog = null;
        try {
            dialog = loader.load();
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        consumer.accept(loader.getController());

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        //dialogStage.initOwner(); //TODO: init the owner
        Scene scene = new Scene(dialog);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }
}