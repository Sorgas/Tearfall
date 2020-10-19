package stonering.screen.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.entity.world.WorldMap;

/**
 * @author Alexander Kuzyakov on 23.04.2017.
 */
public class TileChooser {
    private WorldGenConfig config;
    private WorldMap map;
    private Texture tiles;
    private int tileSize;

    TextureRegion sea;
    TextureRegion ocean;
    TextureRegion mountain;
    TextureRegion pike;
    TextureRegion forest;
    TextureRegion hill;
    TextureRegion river;
    TextureRegion cross;

    public TileChooser(Texture tiles) {
        this.tiles = tiles;
        config = new WorldGenConfig(0, 0);
        initRegions();
    }

    public TextureRegion getTile(int x, int y) {
        int elevation = Math.round(map.getElevation(x, y));
        if (elevation < config.seaLevel) {
            if (elevation < config.seaLevel - 8) {
                return ocean;
            } else {
                return sea;
            }
        } else {
//            if (map.getRivers().containsKey(new Position(x, y, 0))) {
//                return river;
//            } else
                if (elevation < config.seaLevel + 10) {
                return hill;
            } else if (elevation < config.seaLevel + 20) {
                return forest;
            } else if (elevation < config.seaLevel + 40) {
                return mountain;
            } else {
                return pike;
            }
        }
    }

    public void setMap(WorldMap map) {
        this.map = map;
    }

    private void initRegions() {
        sea = new TextureRegion(tiles, 0, 0, 8, 8);
        ocean = new TextureRegion(tiles, 0, 8, 8, 8);

        pike = new TextureRegion(tiles, 8, 0, 8, 8);
        mountain = new TextureRegion(tiles, 8, 8, 8, 8);

        forest = new TextureRegion(tiles, 16, 0, 8, 8);
        hill = new TextureRegion(tiles, 16, 8, 8, 8);

        river = new TextureRegion(tiles, 0, 0, 8, 8);

        cross = new TextureRegion(tiles, 0, 16, 8, 8);
    }

    public TextureRegion getCross() {
        return cross;
    }
}
