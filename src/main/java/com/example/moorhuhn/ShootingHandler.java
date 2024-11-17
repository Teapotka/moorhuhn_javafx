package com.example.moorhuhn;

public interface ShootingHandler {
    boolean isReloading();

    int getBulletCount();
    int getMonsterCount();
    void removeMonster(Monster monster);

    void onMonsterDeath();
}
