package com.codigozerocuatro.chachingproxy.views;

import javafx.fxml.FXMLLoader;

public class ViewLoader {

    public static FXMLLoader load(String nameView) {
        var resource = ViewLoader.class.getResource(nameView);

        if (resource == null) {
            throw new IllegalArgumentException("FXML no encontrado: " + nameView);
        }

        return new FXMLLoader(resource);
    }

}
