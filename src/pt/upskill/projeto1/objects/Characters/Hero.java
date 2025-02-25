package pt.upskill.projeto1.objects.Characters;

import pt.upskill.projeto1.game.FireBallThread;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Inventory.GoodMeat;
import pt.upskill.projeto1.objects.Inventory.Hammer;
import pt.upskill.projeto1.objects.Inventory.Inventory;
import pt.upskill.projeto1.objects.Inventory.Key;
import pt.upskill.projeto1.objects.Room;
import pt.upskill.projeto1.objects.StatusBar.FireBall;
import pt.upskill.projeto1.objects.StatusBar.StatusBar;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

/**
 * class does this
 *
 */
 public class Hero implements ImageTile {
 private Position position;
    private int health;
    private int damageAmount;
    private StatusBar statusBar;

    public Hero(Position position, int health, StatusBar statusBar) {
        this.position = position;
        this.health = health;
        this.damageAmount = 1;
        this.statusBar = statusBar;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public String getName() {
        return "Hero";
    }

    @Override
    public Position getPosition() {
        return position;
    }
    /**
     * sets damage amount received
     *
     */
    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }
    /**
     * moves hero
     *
     */
    public void move(Direction direction) {
        this.position = this.position.plus(direction.asVector());
    }
    /**
     * updates the status Bar when hero loses lives
     *
     */
    public void damage(int damage) {
        health = health - damage;
        if (health < 0) {
            health = 0;
        }
        statusBar.loseLife(damage);
    }
    /**
     * attack function - enemy receives damage
     *
     */
    public void attack(Enemies enemy) {
        enemy.damage(damageAmount);
    }
    /**
     * hero checks for combat if they're in the same position
     *
     */
    public void checkCombat(Enemies enemy, Position preCombatPosition) {
        if (this.getPosition().equals(enemy.getPosition())) {
            System.out.println("Hero attacks the Enemy!");
            this.attack(enemy);

            if (enemy.getHealth() > 0) {
                this.position = preCombatPosition;
            }
        }
    }
    /**
     * hero picks up item and adds to inventory
     *
     */
    public void pickUpItem(Inventory item) {
        if (item instanceof GoodMeat) {
            System.out.println("Hero picked up " + item.getName() + "\n" + "Health restored by 1 HP!");
            if (health < 4) {
                gainLife(1);
            } else {
                System.out.println("Hero has full health!");
            }
        } else if (item instanceof Hammer) {
            System.out.println("Hero picked up " + item.getName());
            setDamageAmount(2);
        } else {
            if (statusBar.addItem(item)) {
                System.out.println("Hero picked up " + item.getName());
            } else {
                System.out.println("Inventory is full!");
            }
        }
    }
    /**
     * hero drops item and removes it from inventory
     *
     */
    public void dropItem(int index, Room currentRoom) {
        if (index >= 0 && index < statusBar.getInventory().size()) {
            Inventory item = (Inventory) statusBar.getInventory().get(index);
            System.out.println("Hero dropped " + item.getName());

            item.setPosition(this.getPosition());
            currentRoom.getRoomMap().add(item);
            ImageMatrixGUI.getInstance().addImage(item);
            statusBar.dropItem(index);
        }
    }
    /**
     * hero gains life if meat ingested
     *
     */
    public void gainLife(int quantity) {
        health = health + quantity;
        if (health > 4) {
            health = 4;
        }
        statusBar.loseLife(-quantity);
    }
    /**
     * hero uses fireball
     *
     */
    public void useFireball(Direction direction, Room currentRoom) {
        if (statusBar.getFireballs() > 0) {
            FireBall fireball = new FireBall(this.position, direction, currentRoom);

            currentRoom.getRoomMap().add(fireball);
            ImageMatrixGUI.getInstance().addImage(fireball);

            FireBallThread fireBallThread = new FireBallThread(direction, fireball);
            fireBallThread.run();

            statusBar.updateFireballs();

        } else {
            System.out.println("Hero ran out of fireballs!");
        }
    }
    /**
     * hero checks if he has key in inventory
     *
     */
    public boolean hasKey(String keyID) {
        for (ImageTile item : statusBar.getInventory()) {
            if (item instanceof Key key && key.getKeyId().equals(keyID)) {
                return true;
            }
        }
        return false;
    }
}