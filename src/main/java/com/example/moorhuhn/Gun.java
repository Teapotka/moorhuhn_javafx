package com.example.moorhuhn;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Gun extends ImageView{
    private final String GUN_URL = "/gun0.png";
    public Gun(){
        super();
        try {
            String gunUrl = GUN_URL;
            Image original = new Image(getClass().getResource(gunUrl).toURI().toString(),
                    100, 100, false, false);
            setImage(original);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
