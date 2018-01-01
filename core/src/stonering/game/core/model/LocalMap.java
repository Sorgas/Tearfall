package stonering.game.core.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.view.tilemaps.LocalTileMapUpdater;
import stonering.global.utils.Position;
import stonering.objects.local_actors.building.BuildingBlock;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.unit.UnitBlock;

/**
 * Created by Alexander on 10.06.2017.
 * <p>
 * Contains blocks, and physical parameters, and proxies to objects
 */
public class LocalMap {
    private int[][][] material;
    private byte[][][] blockType;
    private byte[][][] designatedBlockType;
    private byte[][][] flooding;
    private byte[][][] temperature;
    private byte[][][] lightlevel;
    private PlantBlock[][][] plantBlocks;
    private BuildingBlock[][][] buildingBlocks;
    private UnitBlock[][][] unitBlocks;

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

        flooding = new byte[xSize][ySize][zSize];
        temperature = new byte[xSize][ySize][zSize];
        lightlevel = new byte[xSize][ySize][zSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }


    public boolean isWalkPassable(Position pos) {
        return isWalkPassable(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean isWalkPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(blockType[x][y][z]).getPassing() == 2;
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

    public void setBlocType(int x, int y, int z, byte type) {
        blockType[x][y][z] = type;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }


    public byte getDesignatedBlockType(int x, int y, int z) {
        return designatedBlockType[x][y][z];
    }

    public byte getFlooding(int x, int y, int z) {
        return (byte) (temperature[x][y][z] & 0b00001111);
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

    public void setBlock(int x, int y, int z, byte type, int materialId) {
        blockType[x][y][z] = type;
        material[x][y][z] = materialId;
        if (localTileMapUpdater != null)
            localTileMapUpdater.updateTile(x, y, z);
    }

    public void setDesignatedBlockType(Position pos, byte blockType) {
        setDesignatedBlockType(pos.getX(), pos.getY(), pos.getZ(), blockType);
    }

    public void setDesignatedBlockType(int x, int y, int z, byte blockType) {
        designatedBlockType[x][y][z] = blockType;
    }

    public void updateBlock(int x, int y, int z) {
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

    public void setLocalTileMapUpdater(LocalTileMapUpdater localTileMapUpdater) {
        this.localTileMapUpdater = localTileMapUpdater;
    }

    public void setPlantBlock(int x, int y, int z, PlantBlock block) {
        plantBlocks[x][y][z] = block;
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

    public boolean checkAvailability(Position target) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                if (inMap(target.getX() + x, target.getY() + y, target.getZ()) && isWalkPassable(target.getX() + x, target.getY() + y, target.getZ()))
                    return true;
            }
        }
        return false;
    }

    private boolean inMap(int x, int y, int z) {
        return !(x < 0 || y < 0 || z < 0 ||
                x >= xSize || y >= ySize || z >= zSize);
    }
}