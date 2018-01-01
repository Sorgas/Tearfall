package stonering.game.core.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 10.12.2017.
 * <p>
 * Object selector and center for rendering.
 */
public class GameCamera {
    private Position camera;
    private GameContainer container;
    private LocalMap localMap;

    public GameCamera(GameContainer container) {
        this.container = container;
        localMap = container.getLocalMap();
        initCamera();
    }

    private void initCamera() {
        camera = new Position(localMap.getxSize() / 2, localMap.getySize() / 2, localMap.getzSize() - 1);
        while (localMap.getBlockType(camera.getX(), camera.getY(), camera.getZ()) <= BlockTypesEnum.SPACE.getCode()) {
            camera.setZ(camera.getZ() - 1);
        }
    }

    public Position getPosition() {
        return camera;
    }

    public void moveCamera(int dx, int dy, int dz) {
        if ((camera.getX() > 0 && dx < 0) || (camera.getX() < localMap.getxSize() - 1 && dx > 0)) {
            camera.setX(camera.getX() + dx);
        }
        if ((camera.getY() > 0 && dy < 0) || (camera.getY() < localMap.getySize() - 1 && dy > 0)) {
            camera.setY(camera.getY() + dy);
        }
        if ((camera.getZ() > 0 && dz < 0) || (camera.getZ() < localMap.getzSize() - 1 && dz > 0)) {
            camera.setZ(camera.getZ() + dz);
        }
    }
}
