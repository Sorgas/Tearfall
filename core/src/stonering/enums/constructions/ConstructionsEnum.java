package stonering.enums.constructions;

/**
 * @author Alexander Kuzyakov
 * on 26.06.2018.
 */
public enum ConstructionsEnum {
    WALL("wall"),
    FLOOR("floor"),
    RAMP("ramp"),
    STAIRS("stairs");

    private String title;

    ConstructionsEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
