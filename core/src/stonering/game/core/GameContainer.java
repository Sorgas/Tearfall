package stonering.game.core;

import stonering.game.objects.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameContainer {
    private LocalMap map;
    private List<Creature> creatures;

    public GameContainer(LocalMap map) {
        this.map = map;
        creatures = new ArrayList<>();

    }

    public LocalMap getMap() {
        return map;
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public void setMap(LocalMap map) {
        this.map = map;
    }
}