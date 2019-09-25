package stonering.entity.unit.aspects.needs;

import stonering.entity.Entity;
import stonering.entity.building.Building;
import stonering.entity.building.aspects.RestFurnitureAspect;
import stonering.entity.job.Task;
import stonering.entity.job.action.Action;
import stonering.entity.job.action.RestAction;
import stonering.entity.job.action.TaskTypesEnum;
import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;

import java.util.List;

/**
 * Need for rest. Generates tasks for:
 *  stop activities on medium exhaustion,
 *  sleeping in a bed,
 *  sleeping at safe place,
 *  sleeping an any place.
 *
 *  Tries to stick to day/night cycle.
 *  //TODO night shift
 *
 * @author Alexander on 22.08.2019.
 */
public class RestNeed extends Need {

    @Override
    public int countPriority(Entity entity) {

        return 0;
    }

    /**
     * Returns null, if no bed available on medium exhaustion, Returns task to sleep on the floor on strong exhaustion.
     */
    @Override
    public Task tryCreateTask(Entity entity) {
        List<Building> buildings = GameMvc.instance().getModel().get(BuildingContainer.class).getBuildingsWithAspect(RestFurnitureAspect.class);
        buildings = GameMvc.instance().getModel().get(LocalMap.class).getPassage().filterEntitiesByReachability(buildings, entity.position);
        if(buildings.isEmpty()) return null;
        Action restAction = new RestAction(new EntityActionTarget(buildings.get(0), ActionTarget.EXACT));
        return new Task("sleep", TaskTypesEnum.OTHER, restAction, 1);
    }
}
