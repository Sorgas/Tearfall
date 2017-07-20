package stonering.game.core.model;

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
    private List<Creature> creatures;

    public GameContainer(LocalMap localMap) {
        this.localMap = localMap;
        creatures = new ArrayList<>();

    }

    public LocalMap getLocalMap() {
        return localMap;
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