module Graphic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;

    opens sample.view to javafx.fxml;
    exports sample.view;
}