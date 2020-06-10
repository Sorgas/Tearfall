package stonering.game.model.system.building;

import stonering.entity.building.Building;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityStream;

import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Alexander on 03.02.2020.
 */
public class BuildingsStream extends EntityStream<Building> {
    private BuildingContainer container = container();
    public Stream<Building> stream;

    public BuildingsStream filterByType(String type) {
        stream = stream.filter(building -> building.type.building.equals(type));
        return this;
    }

    public BuildingsStream filterByTypes(List<String> types) {
        stream = stream.filter(building -> types.contains(building.type.building));
        return this;
    }

    @Override
    protected BuildingContainer container() {
        return GameMvc.model().get(BuildingContainer.class);
    }
}
