package com.codigozerocuatro.chachingproxy.controller;

import com.codigozerocuatro.chachingproxy.views.FormatterUtils;
import com.codigozerocuatro.chachingproxy.views.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private VBox serversContainer;

    @FXML
    private TextField urlField;

    @FXML
    private TextField portField;

    @FXML
    private ScrollPane connectionsContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portField.setTextFormatter(FormatterUtils.numericWithRange(0, 49151));
    }

    @FXML
    public void onNewConnectionButtonClick(ActionEvent actionEvent) {
        String url = urlField.getText();
        String port = portField.getText();
        // agregar valos en un listado
        try {
            FXMLLoader loader = ViewLoader.load("server-view.fxml");
            Parent itemNode = loader.load();

            // Obtén el controlador para pasarle los datos
            ServerItemController controller = loader.getController();
            controller.setUrlAndPort(url, port);

            // Añade el nuevo item al contenedor VBox que actúa como lista
            serversContainer.getChildren().addFirst(itemNode);

            // Limpia campos para nueva entrada
            urlField.clear();
            portField.clear();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
