module com.codigozerocuatro.chachingproxy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires io.helidon.webserver;


    opens com.codigozerocuatro.chachingproxy.controller to javafx.fxml;
    exports com.codigozerocuatro.chachingproxy;
}