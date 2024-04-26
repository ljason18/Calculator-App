module project {
    requires transitive java.logging;
    requires transitive javafx.controls;

    opens project to javafx.fxml;
    exports project;
}
