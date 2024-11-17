package com.example.moorhuhn;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bullet extends ImageView {
    private final String BULLET_URL = "/bullet.png";
    public Bullet(){
        super();
        try {
            String bulletUrl = BULLET_URL;
            Image bulletImage = new Image(getClass().getResource(bulletUrl).toURI().toString(),
                    30, 50, false, false);
            setImage(bulletImage);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
