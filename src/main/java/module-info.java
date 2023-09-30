module ca.oakville.studytimer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens ca.oakville.studytimer to javafx.fxml;
    exports ca.oakville.studytimer;
}