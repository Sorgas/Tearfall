package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.world.Mountain;
import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.List;
import java.util.Random;

/**
 * @author Alexander Kuzyakov on 11.03.2017.
 */
public class HillGenerator extends WorldGenerator {
    private Random random;
    private int width;
    private int height;
    private int hillDensity;
    private float hillMargin;


    @Override
    public void set(WorldGenContainer container) {
        random = container.random;
        width = container.config.width;
        height = container.config.height;
        hillDensity = container.config.hillDensity;
        hillMargin = container.config.hillMargin;
    }

    @Override
    public void run() {
        System.out.println("generating hills");
        int num = width * height / hillDensity;
        int widthMargin = (int) (width * hillMargin);
        int heightMargin = (int) (height * hillMargin);
//		List<Mountain> hills = container.getHills();
        for (int i = 0; i < num; i++) {
            Mountain hill = createHill(widthMargin + random.nextInt(width - 2 * widthMargin), heightMargin + random.nextInt(width - 2 * heightMargin));
//			hills.add(hill);
        }
    }

    private Mountain createHill(int x, int y) {
        Mountain Hill = new Mountain();
        Hill.setTop(new Position(x, y, random.nextInt(3) + 2));
        int slopeCount = random.nextInt(2) + 6 + Hill.getTop().z / 39;
        int[] slopeAngles = new int[slopeCount];
        int spinAngle = random.nextInt(360);
        for (int i = 0; i < slopeCount; i++) {
            slopeAngles[i] = random.nextInt(30) - 15 + 360 / slopeCount * i;
            slopeAngles[i] += spinAngle;
            slopeAngles[i] %= 360;
        }
        for (int i = 0; i < slopeCount; i++) {
            int height = Hill.getTop().z;
            int hillRadius = height > 0 ? height * 3 + random.nextInt(height) : 3;
            Vector vector = new Vector(Hill.getTop().x, Hill.getTop().y, (float) slopeAngles[i], hillRadius);
            Hill.addCorner(vector.getEndPoint());
        }
        return Hill;
    }
}