package stonering.game.core.model;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.global.utils.Position;

/**
 * Object selector and center for rendering.
 *
 * @author Alexander Kuzyakov on 10.12.2017.
 */
public class GameCamera {
    private GameContainer container;
    private LocalMap localMap;
    private Position camera;
//    private ;

    private Position frameStart;
    private Position frameEnd;

    public final int IDLE_STATUS = 0;
    public final int GREEN_STATUS = 1;
    public final int RED_STATUS = 2;
    private int status;

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
        return camera.clone();
    }

    public void moveCamera(int dx, int dy, int dz) {
        camera.setX(camera.getX() + dx);
        camera.setY(camera.getY() + dy);
        camera.setZ(camera.getZ() + dz);
        localMap.normalizePosition(camera);
    }

    public int getStatus() {
        return status;
    }

    public void updateStatus(int status) {
        if (status >= 0 && status <= 2)
            this.status = status;
    }

    //TODO add actual sprite change for camera
    public void setValidSprite() {
        System.out.println("camera sprite valid");
    }

    public void setInvalidSprite() {
        System.out.println("camera sprite invalid");
    }

    public void resetSprite() {
        frameStart = null;
        frameEnd = null;
    }

    public Position getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Position frameStart) {
        this.frameStart = frameStart;
    }

    public Position getFrameEnd() {
        return frameEnd;
    }

    public void setFrameEnd(Position frameEnd) {
        this.frameEnd = frameEnd;
    }
}
