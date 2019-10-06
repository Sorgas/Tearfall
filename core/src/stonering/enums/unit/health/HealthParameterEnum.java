package stonering.enums.unit.health;

/**
 * All possible health parameters.
 * Also stores ranges and corresponding buffs for ranges.
 *
 * @author Alexander on 06.10.2019.
 */
public enum HealthParameterEnum {
    FATIGUE(new FatigueParameter(), "fatigue"),
    HUNGER(new HungerParameter(), "hunger");
//    THIRST();

    public final HealthParameter PARAMETER;
    public final String TAG;

    HealthParameterEnum(HealthParameter parameter, String tag) {
        PARAMETER = parameter;
        TAG = tag;
    }
}