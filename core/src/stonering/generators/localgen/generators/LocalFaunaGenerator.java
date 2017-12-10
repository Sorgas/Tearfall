package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;
import stonering.objects.local_actors.unit.Unit;

import java.util.Random;

/**
 * Created by Alexander on 03.12.2017.
 *
 * Generates wild animals on LoclMap
 */
public class LocalFaunaGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private LocalMap localMap;
    private CreatureGenerator creatureGenerator;

    public LocalFaunaGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.getConfig();
        localMap = container.getLocalMap();
        creatureGenerator = new CreatureGenerator();
    }

    public void execute() {
        for (int i = 0; i < 100; i++) {
            Unit unit = creatureGenerator.generateWildAnimal("dog");
            unit.setLocalMap(localMap);
            unit.getAspects().forEach(((s, aspect) -> aspect.init()));
            container.getUnits().add(unit);
            System.out.println("dog");
        }
    }


}
