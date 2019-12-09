package stonering.generators.worldgen.generators.elevation;

import stonering.entity.world.WorldMap;
import stonering.generators.worldgen.WorldGenContainer;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 24.04.2017.
 */
public class DiamondSquareGenerator {
    private WorldGenContainer container;
    private WorldMap map;
    private WorldMap map2;
    private float[][] elevationBuffer;
    private Random random;
    private int bufferScale = 32;
    private int resultMapScale = 6;
    private int numX;
    private int numY;

    public DiamondSquareGenerator(WorldGenContainer container) {
        this.container = container;
    }

    public void execute() {
        init();
        while (bufferScale > 1) {
            performSquare();
            performDiamond();
            bufferScale /= 2;
            numX *= 2;
            numY *= 2;
        }
        System.out.println(bufferScale);
        fillBuffer();
//        container.setMap2(map2);
    }

    private void init() {
        map = container.getMap();
        random = container.random;
        int bufferWidth = (map.getWidth() - 1) * bufferScale + 1;
        int bufferHeigth = (map.getHeight() - 1) * bufferScale + 1;
        int resultMapWidth = (map.getWidth() - 1) * bufferScale * resultMapScale + 1;
        int resultMapHeigth = (map.getHeight() - 1) * bufferScale * resultMapScale + 1;
        map2 = new WorldMap(resultMapWidth, resultMapHeigth);
        elevationBuffer = new float[bufferWidth][bufferHeigth];
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                elevationBuffer[x * bufferScale][y * bufferScale] = map.getElevation(x, y);
            }
        }
        numX = map.getWidth() - 1;
        numY = map.getHeight() - 1;
        System.out.println(map2.getWidth());
    }

    private void performSquare() {
        for (int x = 0; x < numX; x++) {
            for (int y = 0; y < numY; y++) {
                float midValue = elevationBuffer[x * bufferScale][y * bufferScale];
                midValue += elevationBuffer[(x + 1) * bufferScale][y * bufferScale];
                midValue += elevationBuffer[x * bufferScale][(y + 1) * bufferScale];
                midValue += elevationBuffer[(x + 1) * bufferScale][(y + 1) * bufferScale];
                elevationBuffer[x * bufferScale + bufferScale / 2][y * bufferScale + bufferScale / 2] = midValue / 4;
            }
        }
    }

    private void performDiamond() {
        for (int x = 0; x < numX + 1; x++) {
            for (int y = 0; y < numY + 1; y++) {
                if (x < numX) {
                    float midValue = elevationBuffer[x * bufferScale][y * bufferScale];
                    midValue += elevationBuffer[(x + 1) * bufferScale][y * bufferScale];
                    int count = 2;
                    if (y > 0) {
                        midValue += elevationBuffer[x * bufferScale + bufferScale / 2][y * bufferScale - bufferScale / 2];
                        count++;
                    }
                    if (y < numY - 1) {
                        midValue += elevationBuffer[x * bufferScale + bufferScale / 2][y * bufferScale + bufferScale / 2];
                        count++;
                    }
                    elevationBuffer[x * bufferScale + bufferScale / 2][y * bufferScale] = midValue / count;
                }
                if (y < numY) {
                    float midValue = elevationBuffer[x * bufferScale][y * bufferScale];
                    midValue += elevationBuffer[x * bufferScale][(y + 1) * bufferScale];
                     int count = 2;
                    if (x > 0) {
                        midValue += elevationBuffer[x * bufferScale - bufferScale / 2][y * bufferScale + bufferScale / 2];
                        count++;
                    }
                    if (x < numX - 1) {
                        midValue += elevationBuffer[x * bufferScale + bufferScale / 2][y * bufferScale + bufferScale / 2];
                        count++;
                    }
                    elevationBuffer[x * bufferScale][y * bufferScale + bufferScale / 2] = midValue / count;
                }
            }
        }
    }

    private void fillBuffer() {
        for (int x = 0; x < elevationBuffer.length; x++) {
            for (int y = 0; y < elevationBuffer[0].length; y++) {
                float lt = elevationBuffer[x + 1][y];
                float ld = elevationBuffer[x][y];
                float rt = elevationBuffer[x + 1][y + 1];
                float rd = elevationBuffer[x][y + 1];

                for (int x2 = x * bufferScale; x2 < (x + 1) * bufferScale; x2++) {
                    elevationBuffer[x2][y * bufferScale] = ld + (rd - ld) * (x2 - (x * bufferScale)) / bufferScale;
                    elevationBuffer[x2][(y + 1) * bufferScale] = lt + (rt - lt) * (x2 - (x * bufferScale)) / bufferScale;
                    for(int y2 = y * bufferScale; y2 < (y + 1) * bufferScale; y2++) {
                        float bot = elevationBuffer[x2][y * bufferScale];
                        float top = elevationBuffer[x2][(y + 1) * bufferScale];
                        elevationBuffer[x2][y2] = bot + (top - bot) * (y2 - (y  * bufferScale)) / bufferScale;
                    }
                }
            }
        }
    }

    public void setContainer(WorldGenContainer container) {
        this.container = container;
    }
}