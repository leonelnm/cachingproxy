package com.codigozerocuatro.chachingproxy;

import com.codigozerocuatro.chachingproxy.controller.MainController;
import com.codigozerocuatro.chachingproxy.views.ViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    private MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = ViewLoader.load("main-view.fxml");
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        stage.setTitle("Proxy Cache");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (mainController != null) {
            mainController.close();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}