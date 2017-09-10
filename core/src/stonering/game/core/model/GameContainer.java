package stonering.game.core.model;

import stonering.enums.materials.MaterialMap;
import stonering.game.objects.Creature;
import stonering.generators.worldgen.WorldMap;
import stonering.global.FileLoader;

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
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        createTileMapUpdater();
    }

    private void createTileMapUpdater() {
        LocalTileMapUpdater localTileMapUpdater = new LocalTileMapUpdater();
        localMap.setLocalTileMapUpdater(localTileMapUpdater);
        localTileMapUpdater.setLocalMap(localMap);
        localTileMapUpdater.setLocalTileMap(localTileMap);
        localTileMapUpdater.setMaterialMap(materialMap);
        localTileMapUpdater.flushLocalMap();
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
}