package stonering.enums.unit.health;

/**
 * @author Alexander on 8/8/2020
 */
public enum HealthFunctionEnum {
    // general activity of a unit.
    CONSCIOUSNESS(true), // low values cause unit to fall or die at 0
    VISION,
    HEARING,
    BREATHING(true), //  
    BLOOD, // production of blood. High values help resist poisons and recover from blood loss.
    WALKING, // High values increase movement speed
    MOTORIC, // All actions performed by hands
    ; 

    public final boolean LETHAL; // loosing cause death

    HealthFunctionEnum(boolean lethal) {
        LETHAL = lethal;
    }

    HealthFunctionEnum() {
        this(false);
    }
}
