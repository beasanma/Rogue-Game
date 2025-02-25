package pt.upskill.projeto1.objects.Characters;

import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

public abstract class Enemies implements ImageTile {
    private Position position;
    private int health;
    private final int damageAmount;

    public Enemies(Position Position, int health, int damageAmount) {
        this.position = Position;
        this.health = health;
        this.damageAmount = damageAmount;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void moveTowardsHero(Hero hero) {
        int enemyPositionX = this.getPosition().getX();
        int enemyPositionY = this.getPosition().getY();
        int heroPositionX = hero.getPosition().getX();
        int heroPositionY = hero.getPosition().getY();

        if (enemyPositionX < heroPositionX) {
            position = position.plus(Direction.RIGHT.asVector());
        } else if (enemyPositionX > heroPositionX) {
            position = position.plus(Direction.LEFT.asVector());
        } else if (enemyPositionY < heroPositionY) {
            position = position.plus(Direction.DOWN.asVector());
        } else if (enemyPositionY > heroPositionY) {
            position = position.plus(Direction.UP.asVector());
        }
    }

    public void randomMovement() {

        int randomNumb = (int) (Math.random() * 4);
        switch (randomNumb) {
            case 0:
                position = position.plus(Direction.UP.asVector());
                break;
            case 1:
                position = position.plus(Direction.DOWN.asVector());
                break;
            case 2:
                position = position.plus(Direction.LEFT.asVector());
                break;
            case 3:
                position = position.plus(Direction.RIGHT.asVector());
                break;
            default:
                break;
        }
    }

    public void move(Hero hero) {
        int enemyPositionX = this.getPosition().getX();
        int enemyPositionY = this.getPosition().getY();
        int heroPositionX = hero.getPosition().getX();
        int heroPositionY = hero.getPosition().getY();

        boolean inAttackRange = (enemyPositionX >= heroPositionX - 2 && enemyPositionX <= heroPositionX + 2) &&
                (enemyPositionY >= heroPositionY - 2 && enemyPositionY <= heroPositionY + 2);

        if (inAttackRange) {
            moveTowardsHero(hero);
            checkCombat(hero);
        } else {
            randomMovement();
        }
    }

    public void damage(int damage) {
        health = health - damage;
        if (health <= 0) {
            ImageMatrixGUI.getInstance().removeImage(this);
        }
    }

    public void attack(Hero hero) {
        hero.damage(damageAmount);
    }

    public void checkCombat(Hero hero) {
        if (this.getPosition().equals(hero.getPosition())) {
            System.out.println("Enemy attacks the Hero!");
            this.attack(hero);
        }
    }
}
