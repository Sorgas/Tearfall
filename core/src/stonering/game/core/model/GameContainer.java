package stonering.game.core.model;

import stonering.enums.materials.MaterialMap;
import stonering.game.objects.Creature;
import stonering.generators.worldgen.WorldMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameContainer {
    private WorldMap worldMap;
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private List<Creature> creatures;
    private MaterialMap materialMap;

    public GameContainer(LocalMap localMap) {
        this.localMap = localMap;
        creatures = new ArrayList<>();
        materialMap = new MaterialMap();
        createLocalTileMap();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public LocalTileMap getLocalTileMap() {
        return localTileMap;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }

    public void performTick() {

    }

    private void createLocalTileMap() {
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        for (int x = 0; x < localMap.getySize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    localTileMap.setTile(x,y,z,localMap.getBlockType(x,y,z),localMap.getMaterial(x,y,z), (byte) 0);
                }
            }
        }
    }
}