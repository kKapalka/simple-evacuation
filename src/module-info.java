module simple.evacuation2 {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    exports sample;
    opens sample to javafx.graphics;
}