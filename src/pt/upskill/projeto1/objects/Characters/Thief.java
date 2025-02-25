package pt.upskill.projeto1.objects.Characters;

import pt.upskill.projeto1.rogue.utils.Position;

public class Thief extends Enemies {

    public Thief(Position position) {
        super(position, 2, 1);
    }

    public void thiefMovement(Hero hero) {
        int thiefPositionX = this.getPosition().getX();
        int thiefPositionY = this.getPosition().getY();
        int heroPositionX = hero.getPosition().getX();
        int heroPositionY = hero.getPosition().getY();

        int positionX = Integer.compare(heroPositionX, thiefPositionX);
        int positionY = Integer.compare(heroPositionY, thiefPositionY);

        if (positionX != 0 && positionY != 0) {
            Position newPosition = new Position(thiefPositionX + positionX, thiefPositionY + positionY);

            setPosition(newPosition);
        }
    }

    @Override
    public void move(Hero hero) {
        thiefMovement(hero);
        checkCombat(hero);
    }

    @Override
    public String getName() {
        return "Thief";
    }

    @Override
    public void checkCombat(Hero hero) {
        int thiefPositionX = this.getPosition().getX();
        int thiefPositionY = this.getPosition().getY();
        int heroPositionX = hero.getPosition().getX();
        int heroPositionY = hero.getPosition().getY();

        if (Math.abs(thiefPositionX - heroPositionX) == Math.abs(thiefPositionY - heroPositionY)) {
            System.out.println("Thief attacks the Hero!");
            this.attack(hero);
        }
    }
}