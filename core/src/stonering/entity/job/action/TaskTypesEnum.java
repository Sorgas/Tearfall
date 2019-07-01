package stonering.entity.job.action;

/**
 * Enumeration of task types.
 *
 * @author Alexander Kuzyakov on 26.12.2017.
 */
public enum TaskTypesEnum {
    DESIGNATION, // tasks have designation field
    EQUIPPING,
    CRAFTING, // tasks have itemOrder field
    OTHER
}
