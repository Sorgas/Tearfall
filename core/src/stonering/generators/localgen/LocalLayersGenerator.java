package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;

import java.util.Random;

/**
 * Created by Alexander on 01.08.2017.
 */
public class LocalLayersGenerator {
    private LocalMap map;
    private int surfaceLevel;
    private int soilThickness;
    private int sedimentariLayer;
    private int metamorficLayer;
    private int magmaticLayer;

    public void execute() {

    }

    private void generateLayers() {
        countLayers();
        for (int x = 0; x < map.getzSize(); x++) {
            for (int y = 0; y < map.getySize(); y++) {
                for (int z = 0; z < map.getzSize(); z++) {
                    
                }
            }
        }
    }

    private void countLayers() {
        soilThickness = 16 - surfaceLevel / 25;
        magmaticLayer = surfaceLevel - 150;
        metamorficLayer = surfaceLevel / 2 + 50;
        sedimentariLayer = surfaceLevel + surfaceLevel / 25 - 16;
    }

    public void setSurfaceLevel(int surfaceLevel) {
        this.surfaceLevel = surfaceLevel;
    }
}
