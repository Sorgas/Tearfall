package stonering.enums;

public enum OrientationEnum {
    N("n"),
    E("e"),
    S("s"),
    W("w");

    public final String SPRITE_SUFFIX; // used to select sprite 

    OrientationEnum(String suffix) {
        SPRITE_SUFFIX = suffix;
    }
}
