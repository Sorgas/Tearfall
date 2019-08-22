package stonering.entity.unit.aspects.needs;

/**
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR(new WearNeed()),
    REST(new RestNeed());

    public final Need need;

    NeedEnum(Need need) {
        this.need = need;
    }
}
