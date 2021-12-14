module com.betterweather.betterweather {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires unirest.java;
    requires json;
    requires org.slf4j;
    requires java.desktop;

    opens com.betterweather.betterweather to javafx.fxml;
    exports com.betterweather.betterweather;
}