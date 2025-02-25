package pt.upskill.projeto1.game;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Characters.Enemies;
import pt.upskill.projeto1.objects.Characters.Hero;
import pt.upskill.projeto1.objects.Door.Door;
import pt.upskill.projeto1.objects.Inventory.GoodMeat;
import pt.upskill.projeto1.objects.Inventory.Hammer;
import pt.upskill.projeto1.objects.Inventory.Key;
import pt.upskill.projeto1.objects.Room;
import pt.upskill.projeto1.objects.StatusBar.StatusBar;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Engine {

    private Hero hero;
    private Room currentRoom;
    private int currentIndex = 0;
    ArrayList<Room> rooms = new ArrayList<>();
    ArrayList<Enemies> enemies = new ArrayList<>();
    List<ImageTile> bar = new ArrayList<>();
    private StatusBar statusBar;
    private Position preMovePosition;
    private int score;
    private int lostPointsMovement = 1;
    Direction playerDirection = null;

    public void init() {
        ImageMatrixGUI gui = ImageMatrixGUI.getInstance();
        statusBar = new StatusBar();
        statusBar.barTiles();
        bar = statusBar.getBarTiles();

        rooms.add(new Room("rooms/rooms/room0.txt"));

        for (Room room : rooms) {
            room.updateEnemies();
            enemies.addAll(room.getEnemies());
        }

        currentRoom = rooms.get(currentIndex);

        List<ImageTile> tiles = currentRoom.getRoomMap();

        enemies.clear();
        for (ImageTile tile : tiles) {
            if (tile instanceof Enemies) {
                enemies.add((Enemies) tile);
            }
        }
        tiles.addAll(enemies);

        hero = new Hero(new Position(3, 6), 4, statusBar);
        currentRoom.setHero(hero);
        tiles.add(hero);

        gui.setEngine(this);
        gui.newImages(tiles);
        gui.newStatusImages(bar);
        gui.go();

        gui.setStatus("Game start!");

        while (true) {
            gui.update();
        }
    }

    public void notify(int keyPressed) {

        //PROF:  Direction direction = null;

        if (keyPressed == KeyEvent.VK_DOWN) {
            System.out.println("User pressed down key!");
            playerDirection = Direction.DOWN;
        }
        if (keyPressed == KeyEvent.VK_UP) {
            System.out.println("User pressed up key!");
            playerDirection = Direction.UP;
        }
        if (keyPressed == KeyEvent.VK_LEFT) {
            System.out.println("User pressed left key!");
            playerDirection = Direction.LEFT;
        }
        if (keyPressed == KeyEvent.VK_RIGHT) {
            System.out.println("User pressed right key!");
            playerDirection = Direction.RIGHT;
        }
        if (keyPressed == KeyEvent.VK_1) {
            System.out.println("User pressed key 1!");
            hero.dropItem(0, currentRoom);
        }
        if (keyPressed == KeyEvent.VK_2) {
            System.out.println("user pressed key 2!");
            hero.dropItem(1, currentRoom);
        }
        if (keyPressed == KeyEvent.VK_3) {
            System.out.println("user pressed key 3!");
            hero.dropItem(2, currentRoom);
        }
        if (keyPressed == KeyEvent.VK_SPACE) {
            System.out.println("Hero shoots a fireball!");
            hero.useFireball(playerDirection, currentRoom);

            playerDirection = null;
        }
        if (playerDirection != null) {

            if (hero.getHealth() <= 0) {
                System.out.println("Hero was defeated!");
                gameOver();
                currentRoom.getRoomMap().remove(hero);
                ImageMatrixGUI.getInstance().removeImage(hero);

            } else {

                Position newPosition = hero.getPosition().plus(playerDirection.asVector());

                if (!currentRoom.collisionHero(newPosition)) {
                    //PROF: if (nextPosition.getX() >= 0 && nextPosition.getX() < 10) - para o herói não sair
                    //PROF: if (nextPosition.getY() >= 0 && nextPosition.getY() < 10)
                    scoreMovement();

                    preMovePosition = hero.getPosition();
                    hero.move(playerDirection);

                    heroPicksUpItem(newPosition);
                    checkDoors();

                    for (Enemies enemy : enemies) {
                        hero.checkCombat(enemy, preMovePosition);
                    }
                } else {
                    System.out.println("Hero can't move through walls!");
                }
            }
        }
        for (Enemies enemy : enemies) {

            if (enemy.getHealth() <= 0) {
                System.out.println(enemy.getName() + " was defeated!");
                enemies.remove(enemy);
                currentRoom.getRoomMap().remove(enemy);
                ImageMatrixGUI.getInstance().removeImage(enemy);
                scoreDefeatEnemy(enemy.getName());
                break;

            } else {

                Position startPosition = enemy.getPosition();

                enemy.move(hero);

                if (currentRoom.collisionEnemy(enemy.getPosition(), enemy)) {
                    enemy.setPosition(startPosition);

                } else {
                    enemy.checkCombat(hero);
                }
            }
        }
    }

    public void heroPicksUpItem(Position newPosition) {

        List<ImageTile> roomTiles = currentRoom.getRoomMap();

        for (ImageTile tile : roomTiles) {
            if (tile.getPosition().equals(newPosition)) {
                if (tile instanceof GoodMeat) {
                    hero.pickUpItem((GoodMeat) tile);
                    roomTiles.remove(tile);
                    ImageMatrixGUI.getInstance().removeImage(tile);
                    scorePickUpItem("GoodMeat");
                    break;

                } else if (tile instanceof Hammer) {
                    hero.pickUpItem((Hammer) tile);
                    roomTiles.remove(tile);
                    ImageMatrixGUI.getInstance().removeImage(tile);
                    scorePickUpItem("Hammer");
                    break;

                } else if (tile instanceof Key) {
                    hero.pickUpItem((Key) tile);
                    roomTiles.remove(tile);
                    ImageMatrixGUI.getInstance().removeImage(tile);
                    scorePickUpItem("Key");
                    break;
                }
            }
        }
    }

    private void checkDoors() {
        List<Door> doorsList = currentRoom.getDoorsList();
        for (Door door : doorsList) {
            if (door.inPosition(hero.getPosition())) {
                if (door.openDoor(hero, door.getKeyName())) {
                    currentRoom.changeRooms(hero, door);
                    updateRoom();
                }
                break;
            }
        }
    }

    private void updateRoom() {
        enemies.clear();
        currentRoom.updateEnemies();
        enemies.addAll(currentRoom.getEnemies());
        List<ImageTile> tiles = currentRoom.getRoomMap();

        for (ImageTile tile : tiles) {
            if (tile instanceof Enemies) {
                enemies.add((Enemies) tile);
            }
        }
        tiles.addAll(enemies);
        tiles.add(hero);

        ImageMatrixGUI.getInstance().newImages(tiles);
    }

    public void scoreMovement() {
        score = score - lostPointsMovement;
        if (score < 0) {
            score = 0;
        }
    }

    public void scorePickUpItem(String item) {
        int pointsObtained = switch (item) {
            case "GoodMeat" -> 10;
            case "Hammer" -> 25;
            case "Key" -> 5;
            default -> 0;
        };
        score = score + pointsObtained;
    }

    public void scoreDefeatEnemy(String enemy) {
        int pointsObtained = switch (enemy) {
            case "Bat", "BadGuy", "Skeleton" -> 10;
            case "Thief" -> 15;
            default -> 0;
        };
        score = score + pointsObtained;
    }

    public void saveScoreFile() {
        DateTimeFormatter time = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(time);
        try (FileWriter file = new FileWriter("src/pt/upskill/projeto1/objects/Leaderboard.txt", true)) {
            file.write("Score: " + score + " | Date: " + formattedDateTime + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameOver() {
        saveScoreFile();
        ImageMatrixGUI.getInstance().showMessage("you lost!", "GAME OVER!");
        ImageMatrixGUI.getInstance().dispose();
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.init();
    }
}