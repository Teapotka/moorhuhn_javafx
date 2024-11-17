# Moorhuhn Game <img src="https://clipart-library.com/img/1979787.png" width=50/>

A JavaFX implementation of the classic Moorhuhn (Shoot 'em up) game. This project includes custom animations, interactive gameplay mechanics, sound effects, and timing functionality.

## Features

- **Interactive Shooting**: Shoot flying monsters to score points.
- **Custom Animations**: Smooth sprite animations for monsters and bullets.
- **Realistic Physics**: Bullets follow realistic trajectories.
- **Sound Effects**: Gunshot sound and background music enhance the gaming experience.
- **Time System**: Pass the level within the time limit.

## Prerequisites

- Java Development Kit (JDK) 21 or later
- JavaFX libraries (version 21)

## Project structure

```
src/
├── main/
    ├── java/
        ├── moorhuhn/
        │   ├── Game.java                // Main game engine
        │   ├── Monster.java             // Monster sprite and movement logic
        │   ├── Bullet.java              // Bullet sprite logic
        │   ├── Gun.java                 // Gun sprite logic
        │   ├── ShootingHandler.java     // Shooting logic, Monster dying and reloading callback
        │   ├── GameFinishListener.java  // Game finishing callback
        └── resources/                   // Sprites for monsters and bullets, Gunshot and background music

```

Controls
- Mouse Click: Shoot bullets at flying monsters.


