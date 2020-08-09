package stonering.enums.unit.health;

import stonering.game.model.system.unit.CreatureHealthSystem;

/**
 * All possible health parameters. Default range for all health parameters is [0, 100], and can differ between creatures.
 * Buffs and tasks are based on relative value of parameter.
 * Also stores ranges and corresponding buffs for ranges.
 * See {@link CreatureHealthSystem} for delta application details.
 *
 * @author Alexander on 06.10.2019.
 */
public enum NeedEnum {
    FATIGUE(new FatigueParameter("fatigue"), 1f / 16), // applied every minute. gives 60 points over 16 hours
    HUNGER(new HungerParameter("hunger"), 1f / 16),
    THIRST(new ThirstParameter("thirst"), 1f / 16);
    // TODO warmth, joy, social, worship

    public final HealthParameter PARAMETER;
    public final float DEFAULT_DELTA;
    public final String TAG;

    NeedEnum(HealthParameter parameter, float defaultDelta) {
        PARAMETER = parameter;
        TAG = parameter.TAG;
        DEFAULT_DELTA = defaultDelta;
    }
}
