package stonering.widget;

/**
 *
 *
 * @author Alexander on 06.09.2018.
 */
public interface MouseInvocable {
    int CLICK = 0;
    int UP = 1;
    int DOWN = 2;

    boolean invoke(int modelX, int modelY, int button, int action);
}
