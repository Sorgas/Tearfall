package stonering.generators.localgen.generators;

import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov on 14.03.2018.
 *
 * calculates month specific temperature. summer and winter temps are max and min.
 * 0 month is middle spring.
 */
public class LocalTemperatureGenerator {
    private LocalGenContainer container;

    public LocalTemperatureGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        System.out.println("generating temperature");
        Position location = container.getConfig().getLocation();
        float summerTemp = container.getWorld().getWorldMap().getSummerTemperature(location.getX(), location.getY());
        float winterTemp = container.getWorld().getWorldMap().getWinterTemperature(location.getX(), location.getY());
        float yearMiddleTemp = (summerTemp + winterTemp) / 2f;
        for (int i = 0; i < 12; i++) {
            container.getMonthlyTemperatures()[i] = (float) (yearMiddleTemp + Math.sin(Math.PI / 12 * i) * (yearMiddleTemp - winterTemp));
        }
    }
}
