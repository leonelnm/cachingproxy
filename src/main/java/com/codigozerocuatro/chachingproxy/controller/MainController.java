package com.codigozerocuatro.chachingproxy.controller;

import com.codigozerocuatro.chachingproxy.server.Server;
import com.codigozerocuatro.chachingproxy.server.ServerService;
import com.codigozerocuatro.chachingproxy.views.FormatterUtils;
import com.codigozerocuatro.chachingproxy.views.ViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final ServerService serverService;

    @FXML
    private VBox serversContainer;

    @FXML
    private TextField urlField;

    @FXML
    private TextField portField;

    public MainController() {
        this.serverService = new ServerService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portField.setTextFormatter(FormatterUtils.numericWithRange(0, 49151));
    }

    @FXML
    public void onNewConnectionButtonClick(ActionEvent actionEvent) {
        String url = urlField.getText();
        String port = portField.getText();

        try {
            Server server = serverService.startNewConnection(url, !port.isBlank() ? Integer.parseInt(port): null);

            FXMLLoader loader = ViewLoader.load("server-view.fxml");
            Parent itemNode = loader.load();

            ServerItemController controller = loader.getController();
            controller.setUrlAndPort(server.getOrigin(), String.valueOf(server.getPort()));

            // Añade el nuevo item al contenedor VBox que actúa como lista
            serversContainer.getChildren().addFirst(itemNode);

            // Limpia campos para nueva entrada
            urlField.clear();
            portField.clear();

            serverService.getServerConnections();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close(){
        serverService.stopAllServers();
    }
}
