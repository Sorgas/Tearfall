package stonering.enums.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements lazy-loading for {@link Drawable}s.
 * Loads {@link TextureRegionDescriptor}s from regions.json on startup.
 * Creates {@link TextureRegionDrawable}s from descriptors on demand.
 * Saves drawables in a maps by descriptor's name for faster access.
 * Works with non-tiled atlases.
 */
public class DrawableMap {
    private static DrawableMap instance;
    private static Map<String, TextureRegionDescriptor> descriptors;
    private static Map<String, Drawable> drawables;

    private DrawableMap() {
        descriptors = new HashMap<>();
        drawables = new HashMap<>();
        loadRegions();
    }

    public static DrawableMap instance() {
        if (instance == null) instance = new DrawableMap();
        return instance;
    }

    /**
     * Loads descriptors for drawables.
     */
    private void loadRegions() {
        Logger.LOADING.logDebug("loading drawables");
        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<TextureRegionDescriptor> elements = json.fromJson(ArrayList.class, TextureRegionDescriptor.class, FileLoader.getFile(FileLoader.REGIONS_PATH));
        for (TextureRegionDescriptor descriptor : elements) {
            descriptors.put(descriptor.name, descriptor);
        }
    }

    /**
     * Resolves {@link Image} by string key. Only drawables described in regions.json can be got.
     * After first creation of image object, saves it in a map.
     */
    public Drawable getDrawable(String key) {
        if (drawables.containsKey(key)) return drawables.get(key); // drawable exists
        if (descriptors.containsKey(key)) { // create drawable
            TextureRegionDescriptor descriptor = descriptors.get(key);
            TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("sprites/" + descriptor.texture), descriptor.bounds[0], descriptor.bounds[1], descriptor.bounds[2], descriptor.bounds[3]));
            drawables.put(key, drawable);
            return drawables.get(key);
        }
//        Logger.UI.logWarn("Drawable with key " + key + " not found");
        return getDrawable("order_status_icon:suspended");
    }

    public Drawable getTileAtlasDrawable(AtlasesEnum atlas, int x, int y) {
        return getDrawable("order_status_icon:suspended");
    }
}
