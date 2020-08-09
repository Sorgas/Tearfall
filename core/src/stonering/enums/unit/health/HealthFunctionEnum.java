package stonering.enums.unit.health;

/**
 * @author Alexander on 8/8/2020
 */
public enum HealthFunctionEnum {
    // general activity of a unit.
    CONSCIOUSNESS, // low values cause unit to fall or die at 0
    SIGHT,
    HEARING,
    BREATHING, //  
    BLOOD, // production of blood. High values help resist poisons and recover from blood loss.
    MOVING, // motoric activity. High values increase work and movement speed
}
