package stonering.objects.aspects;

import java.util.List;

public class HealthAspect {
    private int hp;
    private List<Object> bodyParts;
    private List<Object> effects;

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
