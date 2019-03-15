package stonering.generators.localgen.generators;

import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.unit.Unit;

/**
 * Generates wild animals on LocalMap.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
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
        Unit unit = creatureGenerator.generateUnit("human");
        if (unit != null) container.getUnits().add(unit);
    }
}
