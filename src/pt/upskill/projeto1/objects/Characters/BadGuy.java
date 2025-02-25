package pt.upskill.projeto1.objects.Characters;

import pt.upskill.projeto1.rogue.utils.Position;

public class BadGuy extends Enemies {

    public BadGuy(Position position) {
        super(position, 1, 1);
    }

    @Override
    public String getName() {
        return "BadGuy";
    }
}
