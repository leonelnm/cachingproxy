package com.codigozerocuatro.chachingproxy;

import com.codigozerocuatro.chachingproxy.views.ViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = ViewLoader.load("main-view.fxml");
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Proxy Cache");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}