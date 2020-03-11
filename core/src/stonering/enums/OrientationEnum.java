package stonering.enums;

public enum OrientationEnum {
    N("n"),
    S("s"),
    E("e"),
    W("w");

    public final String SPRITE_SUFFIX; // used to select sprite 

    OrientationEnum(String suffix) {
        SPRITE_SUFFIX = suffix;
    }
}
