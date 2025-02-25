package pt.upskill.projeto1.objects.Inventory;

import pt.upskill.projeto1.rogue.utils.Position;

public class Key extends Inventory {
    private String keyId;

    public Key(Position position, String keyId) {
        super("Key", position);
        this.keyId = keyId;
    }

    public String getKeyId() {
        return keyId;
    }
}
