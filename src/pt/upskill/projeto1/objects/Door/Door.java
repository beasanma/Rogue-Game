package pt.upskill.projeto1.objects.Door;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Characters.Hero;
import pt.upskill.projeto1.rogue.utils.Position;

public class Door implements ImageTile {
    private final Position position;
    private String name;
    private String nextRoom;
    private int doorId;
    private String key;
    private String nextRoomDoor;


    public Door(Position position, String nextRoom, int doorId, String key, String nextRoomDoor) {
        this.position = position;
        this.name = key != null ? "DoorClosed" : "DoorOpen";
        this.nextRoom = nextRoom;
        this.doorId = doorId;
        this.key = key;
        this.nextRoomDoor = nextRoomDoor;
    }

    public String getNextRoom() {
        return nextRoom;
    }

    public int getDoorId() {
        return doorId;
    }

    public String getNextRoomDoor() {
        return nextRoomDoor;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public String getKeyName() {
        return this.key;

    }

    public boolean needsKey() {
        if (this.key != null) {
            return true;
        }
        return false;
    }

    public boolean openDoor(Hero hero, String keyId) {
        if (needsKey()) {
            String keyForDoor = this.key;
            if (hero.hasKey(keyForDoor)) {
                System.out.println("Door was unlocked!");
                this.name = "DoorOpen";
                return true;
            } else {
                System.out.println("Door is locked. Needs " + keyForDoor);
                return false;
            }
        }
        this.name = "DoorOpen";
        return true;
    }

    public boolean inPosition(Position heroPosition) {
        return this.position.equals(heroPosition);
    }
}

