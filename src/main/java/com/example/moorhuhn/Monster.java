package com.example.moorhuhn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Monster extends ImageView {
    private List<Image> frames;
    private int currentFrame = 0;
    private Timeline animation;
    private Timeline movement;
    private double speed;
    private int monsterType;
    private int direction;
    private boolean isDying;
    private Group root;
    private ShootingHandler shootingHandler;

    public Monster(Group root, int monsterType, double speed, ShootingHandler shootingHandler){
        this.frames = new ArrayList<>();
        this.monsterType = monsterType;
        this.speed = speed;
        this.root = root;
        this.shootingHandler = shootingHandler;

        this.isDying = false;
        this.direction = (int) Math.signum(speed);

        setLayoutX(-79);
        loadFrames(0);

        setImage(frames.isEmpty() ? null : frames.get(0));
        setScaleX(direction);

        startAnimation();
        startMovement();

        setOnMouseClicked(event -> dying());
    }

    private void loadFrames(int type) {
        try {
            frames.clear();
            String name = type == 0 ? "Fly" : "Dying";
            for (int i = 0; i < 18; i++) {
                String path = "/monster"+monsterType+"/"+name+"/0_Monster_"+name+"_0" + (i < 10 ? "0":"") + i + ".png";
                Image frame = new Image(getClass().getResource(path).toURI().toString(),100, 100, false, false);
                frames.add(frame);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(100), e -> nextFrame()));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void startMovement() {
        movement = new Timeline(new KeyFrame(Duration.millis(20), e -> move()));
        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();
    }

    private void nextFrame() {
        if (!frames.isEmpty()) {
            currentFrame = (currentFrame + 1) % frames.size();
            setImage(frames.get(currentFrame));
        }
    }

    private void move() {
        setLayoutX(getLayoutX() + speed);
        if (getLayoutX() >= 800) {
            setLayoutX(-80);
        } else if (getLayoutX() < -80) {
            setLayoutX(800);
        }
    }
    public void dying() {
        if (!isDying && !shootingHandler.isReloading() && shootingHandler.getBulletCount() > 0) {
            isDying = true;
            movement.stop();
            animation.stop();
            shootingHandler.removeMonster(this);

            loadFrames(1);
            currentFrame = 0;
            animation = new Timeline(new KeyFrame(Duration.millis(50), e -> nextFrame()));
            animation.setCycleCount(frames.size() - 1);
            animation.play();
            playDeathSound();
            animation.setOnFinished(event ->{
                root.getChildren().remove(this);
                shootingHandler.onMonsterDeath();
            });
        }
    }

    private void playDeathSound() {
        String dyingUrl = String.format("/monster"+monsterType+"/Death.wav");
        Media media = new Media(getClass().getResource(dyingUrl).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.play();
    }
}
