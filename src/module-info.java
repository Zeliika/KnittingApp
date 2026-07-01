module KnittingApp {
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;
    requires jdk.xml.dom;
    requires com.google.gson;

    opens de.knittingapp.frontend;
}
