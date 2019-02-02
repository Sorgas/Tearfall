package stonering.generators.localgen.generators;

import stonering.game.core.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.unit.Unit;

/**
 * @author Alexander Kuzyakov on 03.12.2017.
 *         <p>
 *         Generates wild animals on LoclMap
 */
public class LocalFaunaGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private CreatureGenerator creatureGenerator;

    public LocalFaunaGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.getConfig();
        creatureGenerator = new CreatureGenerator();
    }

    public void execute() {
        LocalMap localMap = container.getLocalMap();
        Unit unit = creatureGenerator.generateUnit("human");
        if (unit != null) {
            unit.setLocalMap(localMap);
            container.getUnits().add(unit);
        }
    }
}
