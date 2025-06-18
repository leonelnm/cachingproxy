package com.codigozerocuatro.chachingproxy.views;

import javafx.scene.control.TextFormatter;

public class FormatterUtils {
    public static TextFormatter<String> numeric() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
    }

    public static TextFormatter<String> numericWithRange(int min, int max) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                try {
                    int value = Integer.parseInt(newText);
                    return (value >= min && value <= max) ? change : null;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }
}