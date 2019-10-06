package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.Buff;
import stonering.entity.unit.aspects.health.HealthBuff;

/**
 * Creates fatigue buffs.
 *
 * @author Alexander on 06.10.2019.
 */
public class FatigueParameter extends HealthParameter {

    public FatigueParameter() {
        super(new int[]{20, 50, 60, 70, 80, 90, 101});
        fillBuffs();
    }

    private void fillBuffs() {
        buffs[0] = new HealthBuff(10, "performance", 0, 0); // performance is increased after sleep
        buffs[1] = null; // no buff normally
        buffs[2] = new HealthBuff(-10, "performance", 0, 0); // performance decreased
        buffs[3] = new HealthBuff(-20, "performance", 0, 0);
        buffs[4] = new HealthBuff(-30, "performance", 1, 0);
        buffs[5] = new HealthBuff(-60, "performance", 2, 0);
        buffs[6] = new HealthBuff(-80, "performance", 3, 0);
    }

    @Override
    public void assignTags(String tag) {
        for (Buff buff : buffs) {
            if(buff != null) {
                buff.tags.add(tag);
            }
        }
    }
}
