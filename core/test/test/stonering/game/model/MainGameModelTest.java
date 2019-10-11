package test.stonering.game.model;

import org.junit.jupiter.api.Test;
import stonering.game.model.MainGameModel;
import stonering.game.model.system.items.ItemContainer;

import static org.mockito.Mockito.*;

/**
 * @author Alexander on 23.09.2019.
 */
public class MainGameModelTest {

    @Test
    public void testTurn() {
        MainGameModel model = mock(MainGameModel.class);
        ItemContainer itemContainer = mock(ItemContainer.class);
        model.put(itemContainer);
        model.init();
        model.setPaused(false);
        verify(itemContainer).turn();
    }
}
