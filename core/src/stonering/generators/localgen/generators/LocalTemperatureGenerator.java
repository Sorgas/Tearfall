package stonering.generators.localgen.generators;

import stonering.entity.world.World;
import stonering.entity.world.WorldMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;

/**
 * Calculates month specific temperature. summer and winter temps are max and min.
 * 0 month is middle spring.
 *
 * @author Alexander Kuzyakov on 14.03.2018.
 */
public class LocalTemperatureGenerator extends LocalAbstractGenerator {

    public LocalTemperatureGenerator(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        System.out.println("generating temperature");
        Position location = container.config.getLocation();
        WorldMap worldMap = container.model.get(World.class).getWorldMap();
        float summerTemp = worldMap.getSummerTemperature(location.getX(), location.getY());
        float winterTemp = worldMap.getWinterTemperature(location.getX(), location.getY());
        float yearMiddleTemp = (summerTemp + winterTemp) / 2f;
        for (int i = 0; i < 12; i++) {
            container.monthlyTemperatures[i] = (float) (yearMiddleTemp + Math.sin(Math.PI / 12 * i) * (yearMiddleTemp - winterTemp));
        }
    }
}
