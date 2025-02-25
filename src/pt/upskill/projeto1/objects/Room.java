package pt.upskill.projeto1.objects;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Characters.*;
import pt.upskill.projeto1.objects.Door.Door;
import pt.upskill.projeto1.objects.Inventory.GoodMeat;
import pt.upskill.projeto1.objects.Inventory.Hammer;
import pt.upskill.projeto1.objects.Inventory.Key;
import pt.upskill.projeto1.rogue.utils.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

//PROF: criou um RoomManager (gestão) que guarda as salas, portas e chaves em vez de tudo na Room (construção)

public class Room {
    ArrayList<ImageTile> roomMap = new ArrayList<>();
    private Hero hero;
    ArrayList<Enemies> enemies = new ArrayList<>();

    public Room(String filepath) {
        createRoom(filepath);
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public ArrayList<ImageTile> getRoomMap() {
        return roomMap;
    }

    public List<Enemies> getEnemies() {
        return enemies;
    }

    public void createRoom(String filePath) {
        try {
            Scanner scan = new Scanner(new File(filePath));
            roomMap.clear();
            int i = 0;

            while (scan.hasNextLine()) {
                String row = scan.nextLine();

                if (row.startsWith("#")) {
                    continue;
                }
                String[] characters = row.split("");

                for (int j = 0; j < characters.length; j++) {
                    String letters = characters[j];
                    Position position = new Position(j, i);
                    roomMap.add(new Floor(position));

                    switch (letters) {
                        case "W":
                            roomMap.add(new Wall(position));
                            break;
                        case "0":
                            String[] door0Info = readHeaderDoorInfo(filePath, 0);
                            Door newDoor0 = new Door(position, door0Info[0], 0, door0Info[2], door0Info[1]);
                            roomMap.add(newDoor0);
                            break;
                        case "1":
                            String[] door1Info = readHeaderDoorInfo(filePath, 1);
                            Door newDoor1 = new Door(position, door1Info[0], 1, door1Info[2], door1Info[1]);
                            roomMap.add(newDoor1);
                            break;
                        case "2":
                            String[] door2Info = readHeaderDoorInfo(filePath, 2);
                            Door newDoor2 = new Door(position, door2Info[0], 2, door2Info[2], door2Info[1]);
                            roomMap.add(newDoor2);
                            break;
                        case " ":
                            roomMap.add(new Floor(position));
                            break;
                        case "B":
                            roomMap.add(new Bat(position));
                            break;
                        case "k":
                            String keyId = readHeaderKeyInfo(filePath);
                            roomMap.add(new Key(position, keyId));
                            break;
                        case "S":
                            roomMap.add(new Skeleton(position));
                            break;
                        case "m":
                            roomMap.add(new GoodMeat(position));
                            break;
                        case "h":
                            roomMap.add(new Hammer(position));
                            break;
                        case "G":
                            roomMap.add(new BadGuy(position));
                            break;
                        case "T":
                            roomMap.add(new Thief(position));
                            break;
                        default:
                            System.out.print(" ");
                            break;
                    }
                }
                i++;
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + filePath);
        }
    }

    public boolean collisionHero(Position position) {
        for (ImageTile tile : roomMap) {
            if (tile instanceof Wall && tile.getPosition().equals(position)) {
                return true;
            }
            if (tile instanceof Enemies && tile.getPosition().equals(position)) {
                ((Enemies) tile).checkCombat(hero);
                return false;
            }
            if (tile instanceof Door && tile.getPosition().equals(position)) {
                return false;
            }
        }
        return false;
    }

    public boolean collisionEnemy(Position position, Enemies currentEnemy) {
        for (ImageTile tile : roomMap) {
            if (tile instanceof Wall && tile.getPosition().equals(position)) {
                return true;
            }
            if (tile instanceof Hero && tile.getPosition().equals(position)) {
                return true;
            }
            if (tile instanceof Door && tile.getPosition().equals(position)) {
                return true;
            }
            if (tile instanceof Enemies otherEnemy && tile.getPosition().equals(position)) {
                if (!otherEnemy.equals(currentEnemy)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Door> getDoorsList() {
        List<Door> doorsList = new ArrayList<>();
        for (ImageTile tile : roomMap) {
            if (tile instanceof Door) {
                doorsList.add((Door) tile);
            }
        }
        return doorsList;
    }

    public void changeRooms(Hero hero, Door door) {
        if (!door.openDoor(hero, "key1")) {
            System.out.println("You need a key to open this door!");
            return;
        }
        String nextRoom = door.getNextRoom();
        int nextRoomDoor = Integer.parseInt((door.getNextRoomDoor()));
        createRoom("rooms/rooms/" + nextRoom);

        Position newDoorPosition = doorPosition(nextRoomDoor);
        hero.setPosition(newDoorPosition);
    }

    private Position doorPosition(int doorID) {
        for (ImageTile tile : roomMap) {
            if (tile instanceof Door door) {
                if (door.getDoorId() == doorID) {
                    return door.getPosition();
                }
            }
        }
        return new Position(1, 1);
    }

    public String[] readHeaderDoorInfo(String filePath, int doorId) {
        try (Scanner scan = new Scanner(new File(filePath))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();

                if (line.startsWith("# " + doorId)) {
                    String[] parts = line.split("\\s+");

                    if (parts.length >= 4) {
                        String nextRoomFile = parts[3];
                        String nextRoomDoor = parts[4];

                        String doorKey = (parts.length > 5) ? parts[5] : null;
                        return new String[]{nextRoomFile, nextRoomDoor, doorKey};
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
        return null;
    }

    public String readHeaderKeyInfo(String filePath) {
        try (Scanner scan = new Scanner(new File(filePath))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();

                if (line.startsWith("# k")) {
                    String[] parts = line.split(" ");

                    if (parts.length >= 2) {
                        return parts[2];
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
        return null;
    }

    public void updateEnemies() {
        enemies.clear();
        for (ImageTile tile : getRoomMap()) {
            if (tile instanceof Enemies) {
                enemies.add((Enemies) tile);
            }
        }
    }
}