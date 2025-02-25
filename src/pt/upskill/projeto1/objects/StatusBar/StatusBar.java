package pt.upskill.projeto1.objects.StatusBar;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Inventory.Inventory;
import pt.upskill.projeto1.objects.Room;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

import java.util.ArrayList;
import java.util.List;

public class StatusBar {
    private final List<ImageTile> bar = new ArrayList<>();
    private int health = 4;
    private final List<ImageTile> inventory = new ArrayList<>();
    private int fireballs = 3;
    private Direction fireballDirection;
    private Room currentRoom;


    public void barTiles() {
        bar.clear();

        for (int x = 0; x < 10; x++) {
            bar.add(new BlackTile(new Position(x, 0)));
        }
        //fireball
        for (int x = 0; x < 3; x++) {
            if (x < fireballs) {
                bar.add(new FireBall(new Position(x, 0), fireballDirection, currentRoom));
            } else {
                bar.add(new BlackTile(new Position(x, 0)));
            }
        }

        for (int x = 3; x < 7; x++) {
            if (x < 3 + health) {
                bar.add(new GreenTile(new Position(x, 0)));
            } else {
                bar.add(new RedTile(new Position(x, 0)));
            }
        }

        for (int x = 7; x < 10; x++) {
            int inventoryIndex = x - 7;
            if (inventoryIndex < inventory.size()) {
                Inventory item = (Inventory) inventory.get(inventoryIndex);
                item.setPosition(new Position(x, 0));
                bar.add(item);
            } else {
                bar.add(new BlackTile(new Position(x, 0)));
            }
            ImageMatrixGUI.getInstance().newStatusImages(bar);
        }
    }

    public List<ImageTile> getInventory() {
        return inventory;
    }

    public boolean addItem(Inventory item) {
        if (inventory.size() < 3) {
            inventory.add(item);
            barTiles();
            return true;
        }
        return false;
    }

    public void dropItem(int index) {
        if (index >= 0 && index < inventory.size()) {
            inventory.remove(index);
            barTiles();
            ImageMatrixGUI.getInstance().newStatusImages(bar);
        }
    }

    public void loseLife(int quantity) {
        health = health - quantity;
        if (health < 0) {
            health = 0;
        }
        bar.clear();
        barTiles();
        ImageMatrixGUI.getInstance().newStatusImages(bar);
    }

    public int getFireballs() {
        return fireballs;
    }

    public void updateFireballs() {
        if (fireballs > 0) {
            fireballs--;
            barTiles();
        }
    }

    public List<ImageTile> getBarTiles() {
        return bar;
    }
}


