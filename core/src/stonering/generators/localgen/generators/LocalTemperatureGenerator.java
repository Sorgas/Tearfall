package stonering.generators.localgen.generators;

import stonering.entity.World;
import stonering.entity.WorldMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

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
        Logger.GENERATION.logDebug("generating temperature");
        Position location = container.config.getLocation();
        WorldMap worldMap = container.model.get(World.class).getWorldMap();
        float summerTemp = worldMap.getSummerTemperature(location.x, location.y);
        float winterTemp = worldMap.getWinterTemperature(location.x, location.y);
        float yearMiddleTemp = (summerTemp + winterTemp) / 2f;
        for (int i = 0; i < 12; i++) {
            container.monthlyTemperatures[i] = (float) (yearMiddleTemp + Math.sin(Math.PI / 12 * i) * (yearMiddleTemp - winterTemp));
        }
    }
}
