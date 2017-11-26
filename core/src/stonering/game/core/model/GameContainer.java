package stonering.game.core.model;

import stonering.game.core.model.lists.PlantContainer;
import stonering.game.core.model.tilemaps.LocalTileMap;
import stonering.game.core.model.tilemaps.LocalTileMapUpdater;
import stonering.generators.localgen.LocalGenContainer;
import stonering.objects.local_actors.Creature;
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
    private PlantContainer plantContainer;

    public GameContainer(LocalGenContainer container) {
        this.localMap = container.getLocalMap();
        creatures = new ArrayList<>();
        plantContainer = new PlantContainer(container.getTrees(), null);
        localTileMap = new LocalTileMap(localMap.getxSize(), localMap.getySize(), localMap.getzSize());
        createTileMapUpdater();
    }

    private void createTileMapUpdater() {
        LocalTileMapUpdater localTileMapUpdater = new LocalTileMapUpdater(this);
        localMap.setLocalTileMapUpdater(localTileMapUpdater);
        localTileMapUpdater.flushLocalMap();
//        localTileMapUpdater.flushTrees();
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
        for (Creature creature: creatures) {

        }
    }

    public PlantContainer getPlantContainer() {
        return plantContainer;
    }
}