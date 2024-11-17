package com.example.moorhuhn;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application implements GameFinishListener {
    // Konstanty
    private final String FONT_URL = "/font.ttf";
    private final String BACKGROUND_0_URL = "/background0.png";
    private final String BACKGROUND_1_URL = "/background1.png";
    private final String CURSOR_START_URL = "/cursor_start.png";
    // Premenne
    private Game game;
    private Font customFont;
    private Group root;
    private Scene scene;

    // Modifikatory a settery
    private Label createLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setFont(customFont);
        label.setLayoutX(x);
        label.setLayoutY(y);
        return label;
    }
    private void setCustomCursor(String cursorPath) {
        Image cursorImage = new Image(getClass().getResourceAsStream(cursorPath));
        scene.setCursor(new ImageCursor(cursorImage));
    }

    private void changeBackground(String backgroundPath) {
        scene.setFill(new ImagePattern(new Image(backgroundPath)));
    }

    // Inicializacia
    @Override
    public void start(Stage stage) throws IOException {
        initializeScene(stage);
        setupStartLabel();
        stage.show();
    }
    private void initializeScene(Stage stage) throws IOException {
        root = new Group();
        scene = new Scene(root, 800, 500);
        customFont = Font.loadFont(getClass().getResourceAsStream(FONT_URL), 25);
        scene.setFill(new ImagePattern(new Image(BACKGROUND_1_URL)));
        setCustomCursor(CURSOR_START_URL);
        stage.setScene(scene);
    }
    private void setupStartLabel() {
        Label startLabel = createLabel("Start the game", 290, 190);
        startLabel.setOnMouseClicked(event -> {
            changeBackground(BACKGROUND_0_URL);
            root.getChildren().remove(startLabel);
            startGameWithDelay(200);
        });
        root.getChildren().add(startLabel);
    }
    private void startGameWithDelay(int delay) {
        Label prepareLabel = createLabel("Kill them All!", 290, 190);
        root.getChildren().add(prepareLabel);

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> {
            root.getChildren().remove(prepareLabel);
            startGame();
        }));
        timeline.play();
    }
    private void startGame() {
        game = new Game(root, scene, this);
    }

    // Restart hry
    @Override
    public void onGameFinished(boolean result) {
        root.getChildren().clear();
        resetSceneAfterGame(result);
        setupRestartLabel();
    }

    private void resetSceneAfterGame(boolean result) {
        scene.setOnMouseClicked(null);
        scene.setOnMouseDragged(null);
        scene.setOnKeyPressed(null);
        game = null;
        changeBackground(BACKGROUND_1_URL);
        setCustomCursor(CURSOR_START_URL);

        Label resultLabel = createLabel(result ? "You Won!" : "Game Over", 310, 190);
        root.getChildren().add(resultLabel);
    }
    private void setupRestartLabel() {
        Label restartLabel = createLabel("Click to restart", 250, 240);
        restartLabel.setOnMouseClicked(event -> {
            root.getChildren().clear();
            changeBackground(BACKGROUND_0_URL);
            startGameWithDelay(200);
        });
        root.getChildren().add(restartLabel);
    }

    // Start applikacie
    public static void main(String[] args) {
        launch();
    }
}