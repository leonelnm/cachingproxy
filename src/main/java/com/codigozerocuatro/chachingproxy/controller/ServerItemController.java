package com.codigozerocuatro.chachingproxy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerItemController {

    @FXML
    private TextField urlField;
    @FXML
    private TextField portField;

    public void setUrlAndPort(String url, String port) {
        if (url != null) {
            urlField.setText(url);
        }
        if (port != null) {
            portField.setText(port);
        }

    }

}
