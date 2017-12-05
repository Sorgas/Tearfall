package stonering.game.core.model.lists;

import stonering.objects.local_actors.units.Unit;

import java.util.ArrayList;

/**
 * Created by Alexander on 03.12.2017.
 */
public class CreatureList {
    private ArrayList<Unit> creatures;

    public void turn() {
        for (Unit unit:creatures) {
            unit.turn();
        }
    }
}
