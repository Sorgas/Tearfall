package stonering.game.core.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Position frameStart;
    private Sprite sprite;

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
        return camera;
    }

    public void moveCamera(int dx, int dy, int dz) {
        camera.setX(Math.min(Math.max(0, camera.getX() + dx), localMap.getxSize() - 1));
        camera.setY(Math.min(Math.max(0, camera.getY() + dy), localMap.getySize() - 1));
        camera.setZ(Math.min(Math.max(0, camera.getZ() + dz), localMap.getzSize() - 1));
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
        System.out.println("camera sprite resetted");
    }

    public Position getFrameStart() {
        return frameStart;
    }

    public void setFrameStart(Position frameStart) {
        this.frameStart = frameStart;
    }
}
