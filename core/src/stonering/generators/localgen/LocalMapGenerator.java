package stonering.generators.localgen;

import stonering.game.core.model.LocalMap;
import stonering.game.enums.BlockTypesEnum;
import stonering.generators.worldgen.WorldMap;
import stonering.utils.Plane;
import stonering.utils.Position;
import stonering.utils.Vector;

import java.util.Random;

/**
 * factory class for creating LocalMap objects
 */
public class LocalMapGenerator {
    LocalRiverGenerator riverGenerator;

    private LocalMap localMap;
    private WorldMap world;
    private Position location;
    private LocalGenConfig config;
    private Random random = new Random();
    private int perlinModifier = 6;
    int localElevation;
    private static int perm[] = new int[512];
    private static int grad3[][] = {{1, 1, 0}, {-1, 1, 0}, {1, -1, 0}, {-1, -1, 0},
            {1, 0, 1}, {-1, 0, 1}, {1, 0, -1}, {-1, 0, -1},
            {0, 1, 1}, {0, -1, 1}, {0, 1, -1}, {0, -1, -1}};

    private int progress;

    private float[][] poligonTops;

    public LocalMapGenerator() {
        config = new LocalGenConfig();
    }

    public void execute() {
        localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), config.getAreaHight());
        localElevation = world.getElevation(location.getX(), location.getY()) * config.getWorldToLocalElevationModifier();
        for (int i = 0; i < 512; i++) {
            perm[i] = random.nextInt(256);
        }
        generatePerlin();
        riverGenerator = new LocalRiverGenerator(world, localMap, location, localElevation);
        riverGenerator.execute();
    }

    private void generateFlat() {
        System.out.println();
        System.out.print("generating local");
        progress = 0;
        if (validateWorldAndLocation()) {
            for (int x = 0; x < config.getAreaSize(); x++) {
                for (int y = 0; y < config.getAreaSize(); y++) {
                    for (int z = 0; z < config.getAreaHight(); z++) {
                        if (z < localElevation) {
                            localMap.setBlock(x, y, z, BlockTypesEnum.WALL);
                        } else {
                            localMap.setBlock(x, y, z, BlockTypesEnum.SPACE);
                        }
                    }
                }
            }
        }
    }

    private void generatePerlin() {
        float[][] noise = generateOctavedSimplexNoise(config.getAreaSize(), config.getAreaSize(), 4, 0.3f, 0.013f);
        System.out.println();
        System.out.print("generating local");
        progress = 0;
        if (validateWorldAndLocation()) {
            for (int x = 0; x < config.getAreaSize(); x++) {
                for (int y = 0; y < config.getAreaSize(); y++) {
                    if (noise[x][y] < -0.5f) {
                        noise[x][y] = -0.5f;
                    }
                    for (int z = 0; z < config.getAreaHight(); z++) {
                        if (z < (localElevation + noise[x][y] * perlinModifier)) {
                            localMap.setBlock(x, y, z, BlockTypesEnum.WALL);
                        } else {
                            localMap.setBlock(x, y, z, BlockTypesEnum.SPACE);
                        }
                    }
                }
            }
        }
    }

    public float[][] generateOctavedSimplexNoise(int width, int height, int octaves, float roughness, float scale) {
        float[][] totalNoise = new float[width][height];
        float layerFrequency = scale;
        float layerWeight = 1;
        float weightSum = 0;

        for (int octave = 0; octave < octaves; octave++) {
            //Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    totalNoise[x][y] += (float) noise(x * layerFrequency, y * layerFrequency) * layerWeight;
                }
            }

            //Increase variables with each incrementing octave
            layerFrequency *= 2;
            weightSum += layerWeight;
            layerWeight *= roughness;

        }
        return totalNoise;
    }

    public static double noise(double xin, double yin) {
        double n0, n1, n2; // Noise contributions from the three corners
        // Skew the input space to determine which simplex cell we're in
        final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
        double s = (xin + yin) * F2; // Hairy factor for 2D
        int i = fastfloor(xin + s);
        int j = fastfloor(yin + s);
        final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;
        double t = (i + j) * G2;
        double X0 = i - t; // Unskew the cell origin back to (x,y) space
        double Y0 = j - t;
        double x0 = xin - X0; // The x,y distances from the cell origin
        double y0 = yin - Y0;
        // For the 2D case, the simplex shape is an equilateral triangle.
        // Determine which simplex we are in.
        int i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords
        if (x0 > y0) {
            i1 = 1;
            j1 = 0;
        } // lower triangle, XY order: (0,0)->(1,0)->(1,1)
        else {
            i1 = 0;
            j1 = 1;
        } // upper triangle, YX order: (0,0)->(0,1)->(1,1)
        // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
        // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
        // c = (3-sqrt(3))/6
        double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
        double y1 = y0 - j1 + G2;
        double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords
        double y2 = y0 - 1.0 + 2.0 * G2;
        // Work out the hashed gradient indices of the three simplex corners
        int ii = i & 255;
        int jj = j & 255;
        int gi0 = perm[ii + perm[jj]] % 12;
        int gi1 = perm[ii + i1 + perm[jj + j1]] % 12;
        int gi2 = perm[ii + 1 + perm[jj + 1]] % 12;
        // Calculate the contribution from the three corners
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0) n0 = 0.0;
        else {
            t0 *= t0;
            n0 = t0 * t0 * dot(grad3[gi0], x0, y0); // (x,y) of grad3 used for 2D gradient
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0) n1 = 0.0;
        else {
            t1 *= t1;
            n1 = t1 * t1 * dot(grad3[gi1], x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0) n2 = 0.0;
        else {
            t2 *= t2;
            n2 = t2 * t2 * dot(grad3[gi2], x2, y2);
        }
        // Add contributions from each corner to get the final noise value.
        // The result is scaled to return values in the interval [-1,1].
        return 70.0 * (n0 + n1 + n2);
    }

    private static int fastfloor(double x) {
        return x > 0 ? (int) x : (int) x - 1;
    }

    private static double dot(int g[], double x, double y) {
        return g[0] * x + g[1] * y;
    }


    private void generateSurface() {

    }

    private void generateSurfacesafv() {
        int areaSize = config.getAreaSize();
        int elevationModifier = config.getWorldToLocalElevationModifier();
        Position center = new Position(areaSize / 2, areaSize / 2, localElevation);
        Position N = new Position(areaSize / 2, areaSize, (world.getElevation(location.getX(), location.getY() + 1) * elevationModifier + localElevation) / 2);
        Position NE = new Position(areaSize, areaSize, (world.getElevation(location.getX() + 1, location.getY() + 1) * elevationModifier + localElevation) / 2);
        Position E = new Position(areaSize, areaSize / 2, (world.getElevation(location.getX() + 1, location.getY()) * elevationModifier + localElevation) / 2);
        Position SE = new Position(areaSize, 0, (world.getElevation(location.getX() + 1, location.getY() - 1) * elevationModifier + localElevation) / 2);
        Position S = new Position(areaSize / 2, 0, (world.getElevation(location.getX(), location.getY() - 1) * elevationModifier + localElevation) / 2);
        Position SW = new Position(0, 0, (world.getElevation(location.getX() - 1, location.getY() - 1) * elevationModifier + localElevation) / 2);
        Position W = new Position(0, areaSize / 2, (world.getElevation(location.getX() - 1, location.getY()) * elevationModifier + localElevation) / 2);
        Position NW = new Position(0, areaSize, (world.getElevation(location.getX() - 1, location.getY() + 1) * elevationModifier + localElevation) / 2);
        Plane plane = new Plane(center, N, NE);

    }

    private void generatreMap() {
        if (validateWorldAndLocation()) {
            localMap = new LocalMap(config.getAreaSize(), config.getAreaSize(), config.getAreaHight());
            int elevation = world.getElevation(location.getX(), location.getY()) * config.getWorldToLocalElevationModifier() + config.getLocalSeaLevel();
            for (int x = 0; x < config.getAreaSize(); x++) {
                for (int y = 0; y < config.getAreaSize(); y++) {
                    for (int z = 0; z < elevation; z++) {

                    }
                }
            }
        }
    }

    private void countPoligonTops() {
        poligonTops = new float[3][3];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                poligonTops[x * 2][y * 2] = calculateMidElevation(location.getX() - 1 + x, location.getY() - 1 + y);
            }
        }
    }

    private float calculateMidElevation(int x, int y) {
        float midElevation = world.getElevation(x, y);
        midElevation += world.getElevation(x + 1, y);
        midElevation += world.getElevation(x, y + 1);
        return (midElevation + world.getElevation(x + 1, y + 1)) / 4;
    }

    private Vector calculateSlope() {
        int[][] area = new int[3][3];
        Vector vector = new Vector(0, 0, 0, 0);
        for (int x = location.getX() - 1; x < location.getX() + 1; x++) {
            for (int y = location.getY() - 1; y < location.getY() + 1; y++) {
                area[x][y] = world.getElevation(x, y) * 8 + 100;
            }
        }

        return vector;
    }

    private void addRamps() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getxSize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (isGround(new Position(x, y, z))) {
                        Position pos = new Position(x, y, z);
                        placeRamp(pos, observeWalls(pos));
                    }
                }
            }
        }
    }

    private int observeWalls(Position pos) {
        int bitpos = 1;
        int walls = 0;
        for (int yOffset = -1; yOffset < 2; yOffset++) {
            for (int xOffset = -1; xOffset < 2; xOffset++) {
                if ((xOffset != 0) || (yOffset != 0)) {
                    Position pos2 = new Position(pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ());
//                    System.out.println(pos2.toString());
//                    System.out.println(localMap.getCell(pos2).getCellTypeId());
                    if (checkPosition(pos2)) {
//                        if (localMap.getCell(pos2).getCellTypeId() == 1) {
//                            walls |= bitpos;
//                        }
                    }
                    bitpos *= 2;
                }
            }
        }
        return walls;
    }

    private void placeRamp(Position pos, int walls) {
//        if ((walls & 0b00001010) == 0b00001010) {
//            localMap.getCell(pos).setCellTypeId(2);//NW
//        } else if ((walls & 0b01010000) == 0b01010000) {
//            localMap.getCell(pos).setCellTypeId(3);//SE
//        } else if ((walls & 0b00010010) == 0b00010010) {
//            localMap.getCell(pos).setCellTypeId(4);//SW
//        } else if ((walls & 0b01001000) == 0b01001000) {
//            localMap.getCell(pos).setCellTypeId(5);//NE
//
//        } else if ((walls & 0b00010000) != 0) {
//            localMap.getCell(pos).setCellTypeId(6);//E
//        } else if ((walls & 0b01000000) != 0) {
//            localMap.getCell(pos).setCellTypeId(7);//S
//        } else if ((walls & 0b00000010) != 0) {
//            localMap.getCell(pos).setCellTypeId(8);//N
//        } else if ((walls & 0b00001000) != 0) {
//            localMap.getCell(pos).setCellTypeId(9);//W
//
//        } else if ((walls & 0b10000000) != 0) {
//            localMap.getCell(pos).setCellTypeId(10);//se
//        } else if ((walls & 0b00000100) != 0) {
//            localMap.getCell(pos).setCellTypeId(11);//ne
//        } else if ((walls & 0b00100000) != 0) {
//            localMap.getCell(pos).setCellTypeId(12);//sw
//        } else if ((walls & 0b00000001) != 0) {
//            localMap.getCell(pos).setCellTypeId(13);//nw
//        }
    }

    private boolean checkPosition(Position pos) {
        if ((pos.getX() < localMap.getxSize()) && (pos.getX() >= 0)) {
            if ((pos.getY() < localMap.getySize()) && (pos.getY() >= 0)) {
                if ((pos.getZ() < localMap.getzSize()) && (pos.getZ() >= 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isGround(Position pos) {
//        MapCell cell = localMap.getCell(pos);
//        if (cell.getCellTypeId() == 0) {
//            if (pos.getZ() > 0) {
//                if ((localMap.getCell(pos.getX(), pos.getY(), pos.getZ() - 1).getCellTypeId() == 1)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    private boolean validateWorldAndLocation() {
        return world != null && location != null &&
                location.getX() >= 0 && location.getX() < world.getWidth() &&
                location.getY() >= 0 && location.getY() < world.getHeight();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public int getProgress() {
        return progress;
    }
}