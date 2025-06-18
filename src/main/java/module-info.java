module com.codigozerocuatro.chachingproxy {
    requires javafx.controls;
    requires javafx.fxml;
    requires undertow.core;
    requires java.net.http;


    opens com.codigozerocuatro.chachingproxy.controller to javafx.fxml;
    exports com.codigozerocuatro.chachingproxy;
}