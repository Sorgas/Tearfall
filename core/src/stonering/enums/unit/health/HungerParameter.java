package stonering.enums.unit.health;

import stonering.entity.unit.aspects.health.HealthBuff;

/**
 * @author Alexander on 06.10.2019.
 */
public class HungerParameter extends HealthParameter {

    public HungerParameter() {
        super(new float[]{20, 50, 60, 70, 80, 90, 101});
        fillBuffs();
    }

    private void fillBuffs() {
        buffs[0] = new HealthBuff(10, "performance", -1, 0); // performance is increased after sleep
        buffs[1] = null; // no buff normally
        buffs[2] = new HealthBuff(-10, "performance", 0, 0); // performance decreased
        buffs[3] = new HealthBuff(-15, "performance", 1, 0);
        buffs[4] = new HealthBuff(-20, "performance", 2, 0);
        buffs[5] = new HealthBuff(-25, "performance", 3, 0);
        buffs[6] = new HealthBuff(-30, "performance", 4, 0);
    }
}
