package stonering.game.model.local_map;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.UtilByteArray;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Byte array extension to store block types of {@link LocalMap}.
 *
 * @author Alexander on 20.04.2020
 */
public class BlockTypeMap extends UtilByteArray {
    public final int[][][] material;
    private final Position cachePosition;

    public BlockTypeMap(int xSize, int ySize, int zSize) {
        super(xSize, ySize, zSize);
        material = new int[xSize][ySize][zSize];
        cachePosition = new Position();
    }

    @Override
    public void set(int x, int y, int z, int value) {
        super.set(x, y, z, value);
        if (value == WALL.CODE && withinBounds(x, y, z + 1) && get(x, y, z + 1) == SPACE.CODE) {
            setBlock(x, y, z + 1, FLOOR.CODE, material[x][y][z]);
        }
        cachePosition.set(x, y, z);
        if (value != FARM.CODE && value != FLOOR.CODE) { // remove plants if block becomes unsuitable for plants
            GameMvc.model().get(PlantContainer.class).removeBlock(cachePosition, false); 
        }
        // TODO destroy buildings if type != floor
        // TODO kill units if type == wall
        GameMvc.model().get(LocalMap.class).updateTile(cachePosition);
    }

    public void set(int x, int y, int z, BlockTypeEnum type) {
        set(x, y, z, type.CODE);
    }

    public void set(Position position, BlockTypeEnum type) {
        set(position.x, position.y, position.z, type);
    }

    public void setBlock(Position position, BlockTypeEnum type, int material) {
        setBlock(position, type.CODE, material);
    }

    public void setBlock(int x, int y, int z, BlockTypeEnum type, int material) {
        setBlock(x, y, z, type.CODE, material);
    }


    public void setBlock(Position position, int type, int material) {
        setBlock(position.x, position.y, position.z, type, material);
    }

    public void setBlock(int x, int y, int z, int type, int material) {
        this.material[x][y][z] = material;
        set(x, y, z, type);
    }

    public int getMaterial(Position pos) {
        return material[pos.x][pos.y][pos.z];
    }

    public int getMaterial(int x, int y, int z) {
        return material[x][y][z];
    }

    public BlockTypeEnum getEnumValue(Position position) {
        return getEnumValue(position.x, position.y, position.z);
    }

    public BlockTypeEnum getEnumValue(int x, int y, int z) {
        return BlockTypeEnum.getType(get(x, y, z));
    }

    @Override
    public byte get(int x, int y, int z) {
        return withinBounds(x, y, z) ? super.get(x, y, z) : BlockTypeEnum.SPACE.CODE;
    }
}
