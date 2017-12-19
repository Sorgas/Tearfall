/*
 * Created by Alexander on .
 */

package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import stonering.utils.global.StaticSkin;

/**
 * Created by Alexander on 19.12.2017.
 */
public class Toolbar extends Table {

    public Toolbar() {
        super();
        createTable();
    }

    private void createTable() {
//        this.align(Align.right);
//        this.debug();
//        this.defaults().align(Align.bottomRight);
//        this.right().bottom();
//        this.defaults().align(Align.left);
        this.align(Align.bottomRight);
        this.pad(10);

        this.add(new TextButton("digging", StaticSkin.getSkin())).row();
    }
}
