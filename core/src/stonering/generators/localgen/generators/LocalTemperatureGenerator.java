package stonering.generators.localgen.generators;

import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 14.03.2018.
 */
public class LocalTemperatureGenerator {
    private LocalGenContainer container;

    public LocalTemperatureGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        Position location = container.getConfig().getLocation();
        float summerTemp = container.getWorldMap().getSummerTemperature(location.getX(), location.getY());
        float winterTemp = container.getWorldMap().getWinterTemperature(location.getX(), location.getY());
        float yearMiddleTemp = (summerTemp + winterTemp) / 2f;
        for (int i = 0; i < 12; i++) {
            container.getMonthlyTemperatures()[i] = (float) (yearMiddleTemp + Math.sin(Math.PI / 12 * i) * (yearMiddleTemp - winterTemp));
        }
    }
}
