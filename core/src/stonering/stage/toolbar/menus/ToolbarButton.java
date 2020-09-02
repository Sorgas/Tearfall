package stonering.stage.toolbar.menus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import stonering.util.lang.StaticSkin;

/**
 * Button with icon and text to be displayed in toolbar menus.
 * TODO add button for menus, and common ancestor.
 * TODO add screen size dependent size initialization and resizing.
 */
public class ToolbarButton extends ImageTextButton {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGTH = 20;
    private static final String DRAWABLE_UP = "default_icon:up";
    private static final String DRAWABLE_DOWN = "default_icon:down";

    public ToolbarButton(String text) {
        this(text, DEFAULT_WIDTH, DEFAULT_HEIGTH);
    }

    public ToolbarButton(String text, int width, int heigth) {
        super(text, createStyle());
        setSize(width, heigth);
        createButton();
    }

    public ToolbarButton(String text, int width, int heigth, String drawableUp, String drawableDown) {
        this(text, width, heigth);
        //TODO set drawables from DrawableMap
    }

    private void createButton() {
        TextureRegionDrawable drawable1 = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/map_tiles.png"), 0, 0, 8, 8));
        TextureRegionDrawable drawable2 = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/map_tiles.png"), 8, 8, 8, 8));
        drawable1.setMinHeight(20);
        Image image = new Image(drawable1, Scaling.fill);
        image.scaleBy(2);
        image.setHeight(20);

        getStyle().imageUp = image.getDrawable();
        getStyle().imageDown = drawable2;
    }

    private static ImageTextButtonStyle createStyle() {
        TextButton.TextButtonStyle style2 = StaticSkin.getSkin().get(TextButton.TextButtonStyle.class);
        ImageTextButtonStyle style = new ImageTextButtonStyle(style2);
        return style;
    }
}
