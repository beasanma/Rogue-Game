package pt.upskill.projeto1.objects.StatusBar;

import pt.upskill.projeto1.gui.FireTile;
import pt.upskill.projeto1.gui.ImageMatrixGUI;
import pt.upskill.projeto1.gui.ImageTile;
import pt.upskill.projeto1.objects.Characters.Enemies;
import pt.upskill.projeto1.objects.Room;
import pt.upskill.projeto1.rogue.utils.Direction;
import pt.upskill.projeto1.rogue.utils.Position;

public class FireBall implements ImageTile, FireTile {
    private Position position;
    private final Direction direction;
    private final Room currentRoom;

    public FireBall(Position position, Direction direction, Room currentRoom) {
        this.position = position;
        this.direction = direction;
        this.currentRoom = currentRoom;
    }

    @Override
    public String getName() {
        return "Fire";
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position newPosition) {
        this.position = newPosition;

    }

    @Override
    public boolean validateImpact() {
        for (ImageTile tile : currentRoom.getRoomMap()) {
            if (tile instanceof Enemies && tile.getPosition().equals(this.position)) {
                System.out.println("Enemy was struck by fireball!");
                ((Enemies) tile).damage(1);
                ImageMatrixGUI.getInstance().removeImage(tile);
                return false;
            }
        }
        if (currentRoom.collisionHero(this.position)) {
            return false;
        }
        return true;
    }
}