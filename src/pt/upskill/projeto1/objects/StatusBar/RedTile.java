package pt.upskill.projeto1.objects.StatusBar;

import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.rogue.utils.Position;

public class RedTile implements ImageTile {
    private final Position position;

    public RedTile(Position position) {
        this.position = position;
    }

    @Override
    public String getName() {
        return "Red";
    }

    @Override
    public Position getPosition() {
        return position;
    }
}