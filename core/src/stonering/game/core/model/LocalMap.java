package stonering.game.core.model;

import com.badlogic.gdx.math.Vector2;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.global.utils.Position;
import stonering.objects.local_actors.building.BuildingBlock;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.unit.UnitBlock;

import java.util.ArrayList;

/**
 * Contains blocks, and physical parameters, and proxies to objects.
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class LocalMap {
    private int[][][] material;
    private byte[][][] blockType;
    private byte[][][] designatedBlockType;
    private byte[][][] flooding;
    private byte[][][] temperature;
    private byte[][][] lightlevel;
    private byte[][][] area;
    private PlantBlock[][][] plantBlocks;
    private BuildingBlock[][][] buildingBlocks;
    private UnitBlock[][][] unitBlocks;
    private ArrayList<Byte> areaNumbers;

    private LocalTileMapUpdater localTileMapUpdater;

    private int xSize;
    private int ySize;
    private int zSize;

    public LocalMap(int xSize, int ySize, int zSize) {
        material = new int[xSize][ySize][zSize];
        blockType = new byte[xSize][ySize][zSize];
        designatedBlockType = new byte[xSize][ySize][zSize];
        plantBlocks = new PlantBlock[xSize][ySize][zSize];
        buildingBlocks = new BuildingBlock[xSize][ySize][zSize];
        unitBlocks = new UnitBlock[xSize][ySize][zSize];
        area = new byte[xSize][ySize][zSize];
        flooding = new byte[xSize][ySize][zSize];
        temperature = new byte[xSize][ySize][zSize];
        lightlevel = new byte[xSize][ySize][zSize];
        areaNumbers = new ArrayList<>();
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public void setBlock(int x, int y, int z, byte type, int materialId) {
        blockType[x][y][z] = type;
        material[x][y][z] = materialId;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public void updateBlock(int x, int y, int z) {
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public boolean checkAvailability(Position target) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (inMap(target.getX() + x, target.getY() + y, target.getZ()) &&
                        isWalkPassable(target.getX() + x, target.getY() + y, target.getZ()))
                    return true;
            }
        }
        return false;
    }

    public void initAreas() {
        byte areaNum = 1;
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                for (int z = 0; z < zSize; z++) {
                    if (isWalkPassable(x, y, z) && area[x][y][z] == 0) { // not wall
                        floodFill(x, y, z, areaNum);
                        areaNumbers.add(areaNum);
                        areaNum++;
                    }
                }
            }
        }
    }

    private void floodFill(int sx, int sy, int sz, byte number) {
        System.out.println("filling" + " " + sx + " " + sy + " " + sz);
        ArrayList<Position> openList = new ArrayList<>();
        ArrayList<Position> closedList = new ArrayList<>();
        openList.add(new Position(sx, sy, sz));

        while (!openList.isEmpty()) {
            Position current = openList.remove(0);
            closedList.add(current);
            int x = current.getX();
            int y = current.getY();
            int z = current.getZ();
            area[x][y][z] = number;
            for (int dx = -1; dx < 2; dx++) {
                for (int dy = -1; dy < 2; dy++) {
                    for (int dz = -1; dz < 2; dz++) {
                        if ((dx != 0 || dy != 0 || dz != 0) && hasPathBetween(x, y, z, x + dx, y + dy, z + dz)) {
                            Position pos = new Position(x + dx, y + dy, z + dz);
                            if (!openList.contains(pos) && !closedList.contains(pos)) {
                                openList.add(pos);
//                                System.out.println("adding: " + number);
//                                System.out.println("to  : " + pos.toString() + " " + blockType[x + dx][y + dy][z + dz]);
//                                System.out.println("from: " + current.toString() + " " + blockType[x][y][z]);
                            }
                        }
                    }
                }
            }
        }
    }

    //sells should be adjacent
    private boolean hasPathBetween(int x, int y, int z, int x2, int y2, int z2) {
        if (inMap(x, y, z) && inMap(x2, y2, z2) && isWalkPassable(x, y, z) && isWalkPassable(x2, y2, z2)) {
            if (z == z2) {
                return true;
            } else if (x == x2 && y == y2) {
                return isWorkingStair(x, y, Math.min(z, z2));
            } else {
                if (z < z2) {
                    return isWorkingRamp(x, y, z);
                } else {
                    return isWorkingRamp(x2, y2, z2);
                }
            }
        }
        return false;
    }

    private boolean isWorkingRamp(int x, int y, int z) {
        return blockType[x][y][z] == BlockTypesEnum.RAMP.getCode()
                && blockType[x][y][z + 1] == BlockTypesEnum.SPACE.getCode();
    }

    private boolean isWorkingStair(int x, int y, int z) {
        return blockType[x][y][z] == BlockTypesEnum.STAIRS.getCode()
                && (blockType[x][y][z + 1] == BlockTypesEnum.STAIRS.getCode()
                || blockType[x][y][z + 1] == BlockTypesEnum.STAIRFLOOR.getCode());
    }

    public boolean inMap(int x, int y, int z) {
        return !(x < 0 || y < 0 || z < 0 || x >= xSize || y >= ySize || z >= zSize);
    }

    public boolean inMap(Position position) {
        return inMap(position.getX(), position.getY(), position.getZ());
    }

    public boolean inMap(Vector2 vector) {
        return inMap(Math.round(vector.x), Math.round(vector.y), 0);
    }

    public boolean isBorder(int x, int y) {
        return x == 0 || y == 0 || x == xSize - 1 || y == ySize -1;
    }

    public void setBlocType(int x, int y, int z, byte type) {
        blockType[x][y][z] = type;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }

    public void setDesignatedBlockType(int x, int y, int z, byte blockType) {
        designatedBlockType[x][y][z] = blockType;
    }

    public void setLocalTileMapUpdater(LocalTileMapUpdater localTileMapUpdater) {
        this.localTileMapUpdater = localTileMapUpdater;
    }

    public void setPlantBlock(int x, int y, int z, PlantBlock block) {
        plantBlocks[x][y][z] = block;
    }

    public void setPlantBlock(Position pos, PlantBlock block) {
        plantBlocks[pos.getX()][pos.getY()][pos.getZ()] = block;
    }

    public PlantBlock getPlantBlock(int x, int y, int z) {
        return plantBlocks[x][y][z];
    }

    public void setBuildingBlock(int x, int y, int z, BuildingBlock building) {
        buildingBlocks[x][y][z] = building;
    }

    public BuildingBlock getBuildingBlock(int x, int y, int z) {
        return buildingBlocks[x][y][z];
    }

    public void setUnitBlock(int x, int y, int z, UnitBlock unit) {
        unitBlocks[x][y][z] = unit;
    }

    public void setUnitBlock(Position pos, UnitBlock unit) {
        unitBlocks[pos.getX()][pos.getY()][pos.getZ()] = unit;
    }

    public void freeUnitBlock(Position pos) {
        unitBlocks[pos.getX()][pos.getY()][pos.getZ()] = null;
    }

    public void freeUnitBlock(int x, int y, int z) {
        unitBlocks[x][y][z] = null;
    }

    public UnitBlock getUnitBlock(int x, int y, int z) {
        return unitBlocks[x][y][z];
    }

    public void setDesignatedBlockType(Position pos, byte blockType) {
        setDesignatedBlockType(pos.getX(), pos.getY(), pos.getZ(), blockType);
    }

    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isWalkPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(blockType[x][y][z]).getPassing() == 2;
    }

    public boolean isFlyPassable(Position pos) {
        return isFlyPassable(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isFlyPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(blockType[x][y][z]).getPassing() != 0;
    }


    public byte getTemperature(int x, int y, int z) {
        return temperature[x][y][z];
    }

    public int getMaterial(Position pos) {
        return material[pos.getX()][pos.getY()][pos.getZ()];
    }

    public int getMaterial(int x, int y, int z) {
        return material[x][y][z];
    }

    public byte getBlockType(Position pos) {
        return blockType[pos.getX()][pos.getY()][pos.getZ()];
    }

    public byte getBlockType(int x, int y, int z) {
        return blockType[x][y][z];
    }

    public void setBlocType(Position pos, byte type) {
        setBlocType(pos.getX(), pos.getY(), pos.getZ(), type);
    }

    public byte getDesignatedBlockType(int x, int y, int z) {
        return designatedBlockType[x][y][z];
    }


    public byte getFlooding(int x, int y, int z) {
        return flooding[x][y][z];
    }

    public void setFlooding(int x, int y, int z, int value) {
        flooding[x][y][z] = (byte) value;
    }

    public byte getLightLevel(int x, int y, int z) {
        return lightlevel[x][y][z];
    }

    public void setBlock(Position pos, BlockTypesEnum blockType, int materialId) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), blockType.getCode(), materialId);
    }

    public void setBlock(Position pos, byte blockType, int materialId) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), blockType, materialId);
    }

    public void setBlock(int x, int y, int z, BlockTypesEnum blockType, int materialId) {
        setBlock(x, y, z, blockType.getCode(), materialId);
    }

    public byte getArea(Position pos) {
        return area[pos.getX()][pos.getY()][pos.getZ()];
    }

    public byte getArea(int x, int y, int z) {
        return area[x][y][z];
    }

    public void setArea(Position pos, byte type) {
        setBlocType(pos.getX(), pos.getY(), pos.getZ(), type);
    }

    public void setArea(int x, int y, int z, byte area) {
        this.area[x][y][z] = area;
    }

    public PlantBlock getPlantBlock(Position pos) {
        return getPlantBlock(pos.getX(), pos.getY(), pos.getZ());
    }
}