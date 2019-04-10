package stonering.game.view.render.ui.menus.toolbar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.util.global.StaticSkin;

/**
 * Button with icon and text to be displayed in toolbar menus.
 * TODO add button for menus, and common ancestor.
 * TODO add screen size dependent size initialization and resizing.
 */
public class ToolbarButton extends ImageTextButton {
    public ToolbarButton(String text) {
        super(text, createStyle());
        createButton();
    }

    private void createButton() {
        setSize(100, 20);
        getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/map_tiles.png"), 0, 0, 8, 8));
        getStyle().imageDown = getStyle().imageUp;
    }

    private static ImageTextButtonStyle createStyle() {
        TextButton.TextButtonStyle style2 = StaticSkin.getSkin().get(TextButton.TextButtonStyle.class);
        ImageTextButtonStyle style = new ImageTextButtonStyle(style2);
        return style;
    }
}
