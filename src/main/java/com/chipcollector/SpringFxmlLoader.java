package com.chipcollector;

import com.chipcollector.config.SpringAppConfig;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public class SpringFxmlLoader implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    private final ResourceBundle bundle = ResourceBundle.getBundle("DisplayStrings", Locale.ENGLISH);

    public <T> T load(String url) {
        try {
            return FXMLLoader.load(Resources.getResource(url), bundle, null, applicationContext::getBean);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}