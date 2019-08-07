package stonering.entity.environment.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;

/**
 * Aspect for entity which emit light.
 * Changes light map of local map on update.
 *
 * @author Alexander on 07.10.2018.
 */
public abstract class AbstractLightSourceAspect extends Aspect {
    protected float force;                  // [0,1]
    protected float previousForce;          // [0,1]

    public AbstractLightSourceAspect(Entity entity) {
        super(entity);
    }

    /**
     * Recreates light spot from this source.
     * Should be called on every source move or brightness change.
     */
    public abstract void updateLigtOnMap();

    /**
     * Saves current light source state to be removed on next update.
     */
    protected void saveCurrentSpot() {
        previousForce = force;
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }

//    private void unapplyPreviousSpot() {
//        LocalMap localMap = gameContainer.getLocalMap();
//        for (int x = spot.position.getX() - spot.radius; x <= spot.position.getX() + spot.radius; x++) {
//            for (int y = spot.position.getY() - spot.radius; y <= spot.position.getY() + spot.radius; y++) {
//                for (int z = spot.position.getY() - spot.radius; z <= spot.position.getY() + spot.radius; z--) {
//                    if (localMap.inMap(x, y, z)) {
////                        localMap.setLightLevel(z, y, z, (byte) (localMap.getLightLevel(z, y, z) - force * Byte.MAX_VALUE));
//                    }
//
//                    if (localMap.getBlockType(z, y, z) != BlockTypesEnum.SPACE.getCode()) {
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    private void addLightInArea() {
//        //TODO add lighting with ray tracing. Every source update cells lighted by source are stored.
//    }
//
//    /**
//     * Light spot emitted by this source is stored, to be removed on next source update (moving, changing).
//     */
//    private class LightSpot {
//        private int radius;
//        private float force;          // [0,1]
//        private Position position;

//    }
}
