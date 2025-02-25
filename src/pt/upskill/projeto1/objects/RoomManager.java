/*
import java.util.HashMap;
import java.util.Map;


public class RoomManager {
    private Map<String, Room> rooms = new HashMap<>();
    private String currentRoom;

    public RoomManager(String initialRoom) {
        this.currentRoom = initialRoom;
    }

    File[] roomFiles = new File("rooms").listFiles();
    for(File file : roomFiles){
        rooms.put(file.getName(), new Room(file));
    }
}

NOTAS:
ROOM: criar Floor com um for each antes do try-catch do switch-case
 for(int x = 0; x < 10; x++)

Criar interface para a colisão: Wall extends GameObject implements Impassable

public void changeRoom (String nextRoom, String nextDoorId)
getCurrentRoom().getTiles().remove(Engine.getInstance().getHero());
this.currentRoom = nextRoom;
gui = gui.getInstance();
gui.clearImages();
List <ImageTiles> tiles = getCurrentRoom().getTiles();
tiles.add(Engine.getInstance().getHero();
gui.newImages(tiles);

ROOM:
private Map <String, Door> doors = new HashMap<>();
private Map <String, Key> keys = new HashMap<>();

no try-catch que lê a sala

if (line.startWith("#")){
String[] split = line.split(" ");
if (split.length > 3) {
String doorId = split[1];
String doorType = split[2];
String nextLevel = split[3];
String nextDoorId = split[4];
String key = null;
if (split.length > 5){
key = split [5];
}
doors.put(doorId, new Door(doorId, doorType, nextLevel, nextDoorId, key));
} else if (split.length == #){
String keyId = split[1];
String keyName = split[2];
key.put(new Key (keyId, keyName);
}

antes do swith-case dos tiles:

if(doors.containsKey(split[x]){
Door door = doors.get(split[x]);
door.setPosition(position);
tiles.add(door);
}
if (key.containsKey(split[x])
key k = keys.get(split[x]);
k.setPosition(position);
tiles.add(k);
}



if (tile instance of Door){
Door doors = (Door) tile;
if(doors.isClosed())
Room room = Engine.getInstance().getRoomManager().getCurrentRoom()
key key = room.getKey(door.getKey());
if(getInventorio().contains(key)){
door.open();
getInventorio().remove(key);
statusBar.updateStatusBar;
}
} else {
Engine.getInstance(),getRoomManager().changeRoom(door.getNextLevel(), door.getNextDoorId());
}
if (tile instance of Item){
Room rooms = Engine.getInstance().getRoomManager().getCurrentRoom();
room.getTiles().remove(tile);
giu.removeImage(tile);



inventorio2.add((Item) tile);
for (int i = 0; i < 3; i++){
if (inventorio[i] == null){
inventorio[i] = (Item) tile;
inserted = true;
}
//Clonar lista dos objetos da sala, guarda os detalhes alterados quando se volta para a sala anterior

List<ImageTile> clone = new ArrayList<>(roomManager.getCurrentRoom().getTiles());

 */