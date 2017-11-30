package stonering.game.core.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.tilemaps.LocalTileMapUpdater;
import stonering.objects.plants.PlantBlock;

import java.util.ArrayList;

public class LocalMap {
    private int[][][] material;
    private byte[][][] blockType;
    private byte[][][] flooding;
    private byte[][][] temperature;
    private byte[][][] lightlevel;
    private PlantBlock[][][] plantBlocks;
    private LocalTileMapUpdater localTileMapUpdater;

    private int xSize;
    private int ySize;
    private int zSize;

    public LocalMap(int xSize, int ySize, int zSize) {
        material = new int[xSize][ySize][zSize];
        blockType = new byte[xSize][ySize][zSize];
        flooding = new byte[xSize][ySize][zSize];
        temperature = new byte[xSize][ySize][zSize];
        lightlevel = new byte[xSize][ySize][zSize];
        plantBlocks = new PlantBlock[xSize][ySize][zSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public byte getTemperature(int x, int y, int z) {
        return temperature[x][y][z];
    }

    public int getMaterial(int x, int y, int z) {
        return material[x][y][z];
    }

    public byte getBlockType(int x, int y, int z) {
        return blockType[x][y][z];
    }

    public byte getFlooding(int x, int y, int z) {
        return (byte) (temperature[x][y][z] & 0b00001111);
    }

    public byte getLightLevel(int x, int y, int z) {
        return lightlevel[x][y][z];
    }

    public void setBlock(int x, int y, int z, BlockTypesEnum blockType, int materialId) {
        setBlock(x,y,z, blockType.getCode(), materialId);
    }

    public void setBlock(int x, int y, int z, byte blockType, int materialId) {
        this.blockType[x][y][z] = blockType;
        material[x][y][z] = materialId;
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

    public boolean isPassable(int x, int y, int z) {
        if (blockType[x][y][z] == BlockTypesEnum.WALL.getCode()) {
            return false;
        }
        if (flooding[x][y][z] > 0) {
            return false;
        }
        if (blockType[x][y][z] == BlockTypesEnum.SPACE.getCode()) {
            return z > 0 && blockType[x][y][z] == BlockTypesEnum.WALL.getCode();
        }
        return true;
    }

    public void setPlantBlock(int x, int y, int z, PlantBlock block) {
        plantBlocks[x][y][z] = block;
    }

    public PlantBlock getPlantBlock(int x, int y, int z) {
        return plantBlocks[x][y][z];
    }
}