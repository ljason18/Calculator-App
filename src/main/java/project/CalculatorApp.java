package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class CalculatorApp extends Application {

    private Stage stage;
    private Scene scene;
    private HBox root;

    public CalculatorApp() {
        this.stage = null;
        this.scene = null;
        this.root = new HBox();
    } // CalculatorApp

    public void init() {
        root.setPrefSize(300, 550);
        Setup setup = new Setup();
        this.root.getChildren().add(setup);
    } // init

    public void start(Stage stage) throws IOException {
        this.stage = stage;
        this.scene = new Scene(this.root);
        this.stage.setOnCloseRequest(event-> Platform.exit());
        this.stage.setTitle("Calculator");
        this.stage.setScene(this.scene);
        this.stage.sizeToScene();
        this.stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));
    } // start

    public static void main(String[] args) {
        launch();
    }

}