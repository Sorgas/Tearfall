package stonering.enums.items;

/**
 * Categories of food are combination of item tags.
 * 
 * @author Alexander_Kuzyakov on 19.08.2020.
 */
public enum FoodCategoryEnum {
    READY_TO_EAT(0),
    UNPREPARED(1),
    RAW_MEAT(2),
    STALE_FOOD(3),
    CORPSE(4),
    SAPIENT(5);

    public final int WEIGHT;
    
    FoodCategoryEnum(int weight) {
        WEIGHT = weight;
    }
}
