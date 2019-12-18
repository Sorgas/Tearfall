package stonering.enums.unit.health;

import stonering.game.model.system.unit.CreatureHealthSystem;

/**
 * All possible health parameters.
 * Also stores ranges and corresponding buffs for ranges.
 * See {@link CreatureHealthSystem} for delta application details.
 *
 * @author Alexander on 06.10.2019.
 */
public enum HealthParameterEnum {
    FATIGUE(new FatigueParameter("fatigue"), 0.0625f), // applied every minute. gives 60 points over 16 hours
    HUNGER(new HungerParameter("hunger"), 1f),
    THIRST(new ThirstParameter("thirst"), 1f);

    public final HealthParameter PARAMETER;
    public final float DEFAULT_DELTA;
    public final String TAG;

    HealthParameterEnum(HealthParameter parameter, float defaultDelta) {
        PARAMETER = parameter;
        TAG = parameter.tag;
        DEFAULT_DELTA = defaultDelta;
    }
}
