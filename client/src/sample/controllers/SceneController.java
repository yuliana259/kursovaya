package sample.controllers;

import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SceneController {
    private Stage mainStage;
    private Scene mainScene;
    private final static SceneController INSTANCE = new SceneController();

    private SceneController() {}

    public static SceneController getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.mainStage = stage;
    }
    public void setScene(Scene scene)
    {
        this.mainScene=scene;
    }
    public Stage getStage() {
        return this.mainStage;
    }
    public Scene getScene()
    {
        return this.mainScene;
    }

}
