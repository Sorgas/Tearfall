package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;

/**
 * @author Alexander on 06.10.2019.
 */
public class HungerParameter extends HealthParameter {
    private final int iconY = 1;

    public HungerParameter(String tag) {
        super(new int[]{20, 50, 60, 70, 80, 90, 101}, tag);
    }

    @Override
    public Buff getBuffForRange(int rangeIndex) {
        switch (rangeIndex) {
            case 0:
                return createBuffWithDelta(10, 0);
            case 1:
                return null;
            case 2:
                return createBuffWithDelta(-10, 1);
            case 3:
                return createBuffWithDelta(-15, 1);
            case 4:
                return createBuffWithDelta(-20, 2);
            case 5:
                return createBuffWithDelta(-25, 3);
            case 6:
                return createBuffWithDelta(-30, 4);
        }
        return null;
    }

    private Buff createBuffWithDelta(int delta, int iconX) {
        return new HealthBuff(HealthParameterEnum.HUNGER.TAG, delta, "performance", iconX, iconY);
    }
}
