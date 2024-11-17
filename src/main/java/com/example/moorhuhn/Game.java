package com.example.moorhuhn;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Game implements ShootingHandler{
    //Konstanty
    private final int SECONDS = 120;
    private final int BULLETS_COUNT = 6;
    private final String BACKGROUND_MUSIC_URL = "/background_track.mp3";
    private final String SHOOT_MUSIC_URL = "/gun_shoot.mp3";
    private final String RELOAD_MUSIC_URL = "/gun_reload.mp3";
    private final String FONT_URL = "/font.ttf";
    private final String CURSOR_GAME_URL = "/cursor_game.png";

    //Premenne
    private Timeline bulletAnimation;
    private int bulletCount;
    private boolean isReloading;
    private ArrayList<Monster> monsters;
    private ArrayList<Bullet> bullets;
    private Gun gun;
    private Timeline timer;
    private Scene scene;
    private Label textTime;
    private int seconds;
    private Group root;
    private MediaPlayer playerBackground;
    private GameFinishListener finishListener;

    // CALL-BACKy
    @Override
    public boolean isReloading() {
        return isReloading;
    }

    @Override
    public int getBulletCount() {
        return bulletCount;
    }

    @Override
    public int getMonsterCount() {
        return monsters.size();
    }

    @Override
    public void onMonsterDeath() {
        handleMonsterDeath();
    }

    @Override
    public void removeMonster(Monster monster){
        monsters.remove(monster);
    }

    public Game(Group root, Scene scene, GameFinishListener finishListener) {
        try {
            bulletCount = 0;
            isReloading = false;
            seconds = SECONDS;
            monsters = new ArrayList<>();
            this.scene = scene;
            this.root = root;
            this.finishListener = finishListener;

            initializeFontAndLabel();
            initializeBackgroundMusic();
            initializeCursor();
            initializeBullets();
            initializeGun();
            generateMonsters();
            initializeSounds();
            setupMouseAndKeyboardEvents();

            startTimer();

            root.getChildren().add(textTime);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initializeFontAndLabel() {
        Font customFont = Font.loadFont(getClass().getResourceAsStream(FONT_URL), 18);
        textTime = new Label("Time: " + seconds + "s");
        textTime.setFont(customFont);
        textTime.setTextFill(Color.WHITE);
        textTime.setLayoutX(15);
        textTime.setLayoutY(15);
    }
    private void initializeBackgroundMusic() throws Exception {
        String musicUrl = BACKGROUND_MUSIC_URL;
        Media mediaBackground = new Media(getClass().getResource(musicUrl).toURI().toString());
        playerBackground = new MediaPlayer(mediaBackground);
        playerBackground.setCycleCount(MediaPlayer.INDEFINITE);
        playerBackground.play();
    }
    private void initializeCursor() {
        Image cursorImage = new Image(getClass().getResourceAsStream(CURSOR_GAME_URL));
        ImageCursor customCursor = new ImageCursor(cursorImage);
        scene.setCursor(customCursor);
    }
    private void initializeBullets() {
        bullets = new ArrayList<>();
        for (int i = 0; i < BULLETS_COUNT; i++) {
            Bullet bullet = new Bullet();
            bullet.setLayoutY(420);
            bullet.setLayoutX(550 + i * 35);
            bullets.add(bullet);
            bulletCount++;
            root.getChildren().add(bullet);
        }
    }
    private void initializeGun() {
        gun = new Gun();
        gun.setLayoutX(350);
        gun.setLayoutY(400);
        root.getChildren().add(gun);
    }
    private void generateMonsters() {
        Random random = new Random();
        int count0 = random.nextInt(3, 6);
        int count1 = random.nextInt(3, 6);
        int count2 = 13 - (count0 + count1);

        createMonsters(random, count0, 0, 1.5, 20, 150);
        createMonsters(random, count1, 1, 0.5, 100, 350);
        createMonsters(random, count2, 2, 1.0, 50, 250);
    }
    private void createMonsters(Random random, int count, int type, double speed, int minY, int maxY) {
        for (int i = 0; i < count; i++) {
            double velocity = random.nextDouble(-2, 3);
            velocity += (Math.signum(velocity) == 1 ? speed : -speed);
            int positionY = random.nextInt(minY, maxY);

            Monster monster = new Monster(root, type, velocity, this);
            monster.setLayoutY(positionY);
            monsters.add(monster);
            root.getChildren().add(monster);
        }
    }
    private void initializeSounds() throws Exception {
        String shootUrl = SHOOT_MUSIC_URL;

        MediaPlayer playerShoot = new MediaPlayer(
                new Media(getClass().getResource(shootUrl).toURI().toString())
        );

        String reloadUrl = RELOAD_MUSIC_URL;
        MediaPlayer playerReload = new MediaPlayer(
                new Media(getClass().getResource(reloadUrl).toURI().toString())
        );

        scene.setOnMouseClicked(event -> {
            if (!isReloading) {
                if (bulletCount > 0) {
                    playSound(playerShoot);
                    root.getChildren().remove(bullets.removeLast());
                    bulletCount = bullets.size();
                } else {
                    reloadBullets(playerReload);
                }
            }
        });
    }
    private void playSound(MediaPlayer player) {
        player.stop();
        player.seek(Duration.ZERO);
        player.play();
    }

    private void reloadBullets(MediaPlayer playerReload) {
        isReloading = true;
        playSound(playerReload);
        bulletAnimation = new Timeline(new KeyFrame(Duration.millis(200), e -> addBullet()));
        bulletAnimation.setCycleCount(6);
        bulletAnimation.play();
    }
    private void addBullet() {
        Bullet bullet = new Bullet();
        bullet.setLayoutY(420);
        bullet.setLayoutX(550 + bulletCount * 35);
        bullets.add(bullet);
        root.getChildren().add(bullet);
        bulletCount++;
        if (bulletCount == 6) {
            isReloading = false;
        }
    }
    private void setupMouseAndKeyboardEvents() {
        scene.setOnMouseMoved(event -> rotateGun(event));
        scene.setOnMouseDragged(event -> rotateGun(event));
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                timer.stop();
                finishGame(false);
            }
        });
    }
    private void rotateGun(MouseEvent event){
        double mouseX = event.getSceneX() - 400;
        double mouseY = event.getSceneY();

        int sign = (int) Math.signum(mouseX);
        double deltaX = Math.abs(mouseX);
        double deltaY = Math.abs(mouseY - 450);
        double angle = Math.toDegrees(Math.atan2(deltaX, deltaY)) * sign;

        gun.setRotate(angle);
    };
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            textTime.setText("Time: " + seconds + "s");
            seconds--;
        }));
        timer.setCycleCount(seconds + 1);
        timer.play();
        timer.setOnFinished(event -> finishGame(monsters.isEmpty()));
    }

    public void finishGame(boolean result) {
        playerBackground.stop();
        timer.stop();
        finishListener.onGameFinished(result);
    }


    private void handleMonsterDeath() {
        if(monsters.isEmpty()){
            finishGame(true);
        }
    }
}
